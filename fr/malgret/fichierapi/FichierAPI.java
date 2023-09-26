package fr.malgret.fichierapi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

import fr.malgret.fichierapi.json.FastMap;
import fr.malgret.fichierapi.json.Json;
import fr.malgret.fichierapi.requests.GetRequest;
import fr.malgret.fichierapi.requests.PostRequest;
import fr.malgret.fichierapi.requests.Request;

public class FichierAPI {
	
	// Token
	
	private final String BEARER_TOKEN;
	
	// Upload Fields
	
	private final String BOUNDARY = "" + System.currentTimeMillis();
	private final int[] SKIP = new int[] { 66, 24, 33, 24 };
	private final String[] KEYS_INFOS = new String[] { "file_name", "file_length", "file_url", "delete_file_url" };
	
	public FichierAPI(String BEARER_TOKEN)
	{
		this.BEARER_TOKEN = BEARER_TOKEN;
	}
	
	public FastMap makeRequest(Request req, FastMap parameters) throws IOException
	{
		URL url = new URL(req.getUrl());
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();

		conn.setRequestProperty("Authorization", "Bearer " + BEARER_TOKEN);
		
		boolean post = req instanceof PostRequest;
	        
		conn.setRequestMethod(post ? "POST" : "GET");
		conn.setDoOutput(true);
		conn.setRequestProperty("Content-Type", "application/json");
		conn.setRequestProperty("Accept", "application/json");
		
		if(post) {
			String jsonString = Json.encode(parameters);
			byte[] jsonBytes = jsonString.getBytes("utf-8");
		        
			OutputStream os = conn.getOutputStream();
			os.write(jsonBytes);
			os.close();
		}

		conn.connect();

		BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
		 
		String responseLine = br.readLine();
		
		br.close();
		conn.disconnect();
		 
		return Json.decode(responseLine);
	}
	
	public FastMap[] upload(FastMap parameters, FastMap[] files) throws MalformedURLException, IOException {
		
		FastMap uploadInfos = makeRequest(GetRequest.SERVER_UPLOAD, null); // Get upload server
		
		URL url = new URL("https://" + uploadInfos.get("url") + "/upload.cgi?id=" + uploadInfos.get("id"));
		
		HttpURLConnection conn = (HttpURLConnection) url.openConnection(); // Prepare connection
		conn.setDoOutput(true);
		conn.setUseCaches(false);
		conn.setRequestMethod("POST");
        conn.setRequestProperty("Connection", "Keep-Alive");
        conn.setRequestProperty("Cache-Control", "no-cache");
        conn.setRequestProperty("Authorization", "Bearer " + BEARER_TOKEN);
        conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);
		
		String CRLF = "\r\n";
		
		OutputStream out = conn.getOutputStream();
		
		PrintWriter writer = new PrintWriter(new OutputStreamWriter(out, "UTF-8"));
		
		for(byte i = 0; i < parameters.length; i++)
		{
			writer.append("--" + BOUNDARY).append(CRLF);
	        writer.append("Content-Disposition: form-data; name=\"" + parameters.getKey(i) + "\"").append(CRLF);
	        writer.append(CRLF);
	        writer.append("" + parameters.getValue(i)).append(CRLF);
		}
		
	    for(short i = 0; i < files.length; i++)
	    {
	    	FastMap file = files[i];
	    	
	    	writer.append("--" + BOUNDARY).append(CRLF);
	        writer.append("Content-Disposition: form-data; name=\"file[]\"; filename=\"" + file.getValue((short) 0) + "\"").append(CRLF);
	        writer.append("Content-Type: application/octet-stream").append(CRLF);
	        writer.append(CRLF).flush();
	        
	        Files.copy((Path) file.getValue((short) 1), out);
	        out.flush();
	        
	        writer.append(CRLF);
	    }
        
	    writer.append("--" + BOUNDARY + "--").flush();
	    
	    writer.close();
	    out.close();
	    
	    conn.connect();
	    
	    // Get response
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		
		for(byte i = 0; i < 71; reader.readLine(), i++);
		
		FastMap[] filesInfos = new FastMap[files.length];
		
		for(short i = 0; i < filesInfos.length; i++)
		{
			FastMap currentInfos = new FastMap((short) 4);
			
			reader.readLine();
			reader.readLine();
			
			for(byte j = 0; j < 4; j++) {
				
				String line = reader.readLine();
				
				String info = line.substring(SKIP[j]);
				
				if(j == 2) {
					currentInfos.insert(KEYS_INFOS[j], info.split("\"")[0]);
				} else {
					currentInfos.insert(KEYS_INFOS[j], info.split("<")[0]);
				}
			}
			
			filesInfos[i] = currentInfos;
			
			reader.readLine();
		}
		
		reader.close();
		conn.disconnect();
		
		return filesInfos;
	}

}
