import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import fr.malgret.fichierapi.FichierAPI;
import fr.malgret.fichierapi.json.FastMap;
import fr.malgret.fichierapi.json.Json;

public class FichierUploader {
	
	private static final FichierAPI fichierAPI = new FichierAPI(" BEARER_TOKEN ");

	public static void main(String[] args) {
		
		FichierUploader main = new FichierUploader();
		
		FastMap parameters = new FastMap((short) 2);
		parameters.insert("did", 0); // Upload at root
		parameters.insert("dpass", "w12345678x"); // Password to access files
		
		FastMap[] response = main.uploadFile(new File("D:/serv/"), parameters);
		
		/*
		* Used for the output
		*
		for(short i = 0; i < response.length; i++)
		{
			System.out.println(Json.encode(response[i]));
		}*/
	}
	
	private void listFiles(List<Path> paths, File file)
	{
		if(file.isFile()) {
			if(file.length() != 0) paths.add(file.toPath());
			return;
		}
		
		for(File child : file.listFiles())
		{
			listFiles(paths, child);
		}
	}
	
	public FastMap[] uploadFile(File file, FastMap parameters)
	{
		final List<Path> paths = new ArrayList<>();
		
		listFiles(paths, file);
		
		FastMap[] uploadFiles = new FastMap[paths.size()];
		short currentIndex = 0;
		Path originalPath = file.getParentFile().toPath();
		
		for(Path path : paths)
		{
			String fileName = originalPath.relativize(path).toString().replace("\\", "/");
			
			uploadFiles[currentIndex++] = FastMap.of(
					"file_name", fileName,
					"file_path", path
					);
		}
		
		try {
			return fichierAPI.upload(parameters, uploadFiles);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
}
