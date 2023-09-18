package fr.malgret.fichierapi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import fr.malgret.fichierapi.json.FastMap;
import fr.malgret.fichierapi.json.Json;
import fr.malgret.fichierapi.requests.PostRequest;

public class FichierAPI {
	
	private final String BEARER_TOKEN;
	
	public FichierAPI(String BEARER_TOKEN)
	{
		this.BEARER_TOKEN = BEARER_TOKEN;
	}
	
	public FastMap postRequest(PostRequest req, FastMap parameters) throws IOException
	{
		URL url = new URL(req.getUrl());
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();

		conn.setRequestProperty("Authorization", "Bearer " + BEARER_TOKEN);
	        
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Content-Type", "application/json");
		conn.setRequestProperty("Accept", "application/json");
		conn.setDoOutput(true);
	        
		String jsonString = Json.encode(parameters);
		byte[] jsonBytes = jsonString.getBytes("utf-8");
	        
		OutputStream os = conn.getOutputStream();
		os.write(jsonBytes);
		os.close();

		conn.connect();

		BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
		 
		String responseLine = br.readLine();
		
		br.close();
		conn.disconnect();
		 
		return Json.decode(responseLine);
	}

}
