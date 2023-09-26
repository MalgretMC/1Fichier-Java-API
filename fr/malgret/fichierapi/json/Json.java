package fr.malgret.fichierapi.json;

public class Json {
	
	public static final char QUOT_MARK = '"';
	
	/*
	 * 
	 * Encode Specific Object in Json
	 * 
	 */
	
	private static Object encodedValue(Object value)
	{
		if(value instanceof Object[])
		{
			StringBuilder builder = new StringBuilder();
			Object[] array = (Object[]) value;
			
			builder.append(encodedValue(array[0]));
			
			for(short i = 1; i < array.length; i++)
			{
				builder.append("," + encodedValue(array[i]));
			}
			
			return "[" + builder.toString() + "]";
		}
		else if(value instanceof FastMap)
		{
			return Json.encode((FastMap) value);
		}
		else if(value instanceof String)
		{
			return Json.QUOT_MARK + String.valueOf(value) + Json.QUOT_MARK;
		}
		
		return value;
	}
	
	/*
	 * 
	 * Encode a FastMap Entry
	 * 
	 */
	
	private static String encodeEntry(String key, Object value)
	{
		return QUOT_MARK + key + QUOT_MARK + ":" + encodedValue(value);
	}
	
	/*
	 * 
	 * Encode FastMap object in JSON
	 * 
	 */
	
	public static String encode(FastMap parameters)
	{
		StringBuilder builder = new StringBuilder();
		short i = 0;
		
		builder.append(encodeEntry(parameters.getKey(i), parameters.getValue(i)));
		
		for(i++; i < parameters.length; i++)
		{
			builder.append("," + encodeEntry(parameters.getKey(i), parameters.getValue(i)));
		}
		
		if(builder.length() == 0) return "";
		
		return "{" + builder.toString() + "}";
	}
	
	/*
	 * Json Decoding :
	 * 
	 * Works for Arrays (Recursively)
	 * 
	 */
	
	public static FastMap decode(String json)
	{
		char[] chars = json.toCharArray();
		Object[] objects = new Object[128];
		byte objectsSize = 0;
		
		JsonCharacter waitingFor = null;
		
		for(short i = (short) 2, j = i; i < chars.length; i++)
		{
			char current = chars[i];
			
			if(waitingFor != null)
			{
				if(current != waitingFor.getEnd() && (waitingFor != JsonCharacter.NO_MARK || JsonCharacter.fromEnd(current) == null)) continue;
				
				Object toAdd = null;
				
				switch(waitingFor)
				{
					case ACCOLADE:
						
						String toDecode = new String(chars, j, i++-j);
						
						toAdd = decode("{" + toDecode + "}");
						break;
					case BRACKET:
						String decoding = new String(chars, j+1, i++-j-2);
						
						if(chars[j] == '{') {
							String[] splited = decoding.split("\\},\\{");
							
							FastMap[] map = new FastMap[splited.length];
							
							for(short s = 0; s < splited.length; s++) {
								
								map[s] = decode("{" + splited[s] + "}");
							}
							
							toAdd = map;
						} else {
							toAdd = decoding.split(",");
						}
						break;
					case NO_MARK:
						toAdd = Long.parseLong(new String(chars, j, i-j));
						break;
					default:
						toAdd = new String(chars, j, i++-j);

						break;
				}
				
				objects[objectsSize++] = toAdd;
				waitingFor = null;
				
				j = (short) ((++i) + 1);
			}
			
			if(current == ':')
			{
				String key = new String(chars, j, i-j-1);
				
				objects[objectsSize++] = key;
				
				if((waitingFor = JsonCharacter.fromStart(chars[++i])) == null) {
					waitingFor = JsonCharacter.NO_MARK;
					i--;
				}
				
				j = (short) (i+1);
			}
		}
		
		FastMap parametersMap = new FastMap((short) (objects.length/2));
		
		for(byte i = 0; i < objectsSize; i++)
		{
			parametersMap.insert((String) objects[i++], objects[i]);
		}
		
		objects = null;
		
		return parametersMap;
	}

}
