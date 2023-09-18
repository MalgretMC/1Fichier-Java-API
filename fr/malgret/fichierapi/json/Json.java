package fr.malgret.fichierapi.json;

public class Json {
	
	public static final char QUOT_MARK = '"';
	
	/*
	 * Encode FastMap object in JSON
	 * 
	 */
	
	public static String encode(FastMap parameters)
	{
		StringBuilder builder = new StringBuilder();
		
		for(short i = 0; i < parameters.length; i++)
		{
			builder.append("," + QUOT_MARK + parameters.getKey(i) + QUOT_MARK + ":" 
					+ parameters.getEncodedValue(i));
		}
		
		if(builder.length() == 0) return "";
		
		return "{" + builder.substring(1).toString() + "}";
	}
	
	/*
	 * Json Decoding :
	 * 
	 * Works for Arrays (Recursively)
	 * 
	 */
	
	public static FastMap decode(String json)
	{
		String[] parametersTab = json.substring(1, json.length()-1).split(","); // Get all key : value
		FastMap parametersMap = new FastMap((byte) parametersTab.length); // Presume max size
		
		for(short i = 0, j = 0, k = 0; i < parametersTab.length; i++) // j and k count the numbers of array
		{
			String[] parameter = parametersTab[i].split(QUOT_MARK + "\\:"); // Get current parameter
			
			String key = parameter[0].substring(1);
			Object value = parameter[1].replace(String.valueOf(QUOT_MARK), ""); // Replace QUOT MARK if a String
			
			if(parameter[1].startsWith("[")) // Array
			{
				String arrayStr = json.split("\\:\\[")[++j].split("\\]")[0]; // Get Array's content
				
				i--;
				
				if(arrayStr.startsWith("{")) // If there are json contents
				{
					arrayStr = arrayStr.substring(1, arrayStr.length()-1);
					
					String[] jsonArray = arrayStr.split("\\},\\{");
					FastMap[] mapsArray = new FastMap[jsonArray.length];
					
					for(short l = 0; l < jsonArray.length; l++)
					{
						mapsArray[l] = decode("{" + jsonArray[l] + "}");
						i+= mapsArray[l].length;
					}
					
					value = mapsArray;
				}
				else // If not
				{
					Object[] array = arrayStr.replace(String.valueOf(QUOT_MARK), "").split(",");
					value = array;
					
					i += array.length;
				}
			} 
			else if(parameter[1].startsWith("{")) // if there are json contents
			{
				FastMap map = decode("{" + json.split(key + "\\:\\{")[++k].split("}")[0] + "}");
				value = map;
				
				i += map.length;
			}
			
			parametersMap.insert(key, value);
		}
		
		return parametersMap;
	}

}
