package fr.malgret.fichierapi.json;

public class FastMap {
	
	private final String[] keys;
	private final Object[] values;
	
	public short length;
	
	public static FastMap of(Object... objects)
	{
		FastMap map = new FastMap((short) (objects.length/2));
		
		for(short i = 0; i < objects.length; i++)
		{
			map.insert((String) objects[i++], objects[i]);
		}
		
		return map;
	}
	
	public FastMap(short maxLength)
	{
		keys = new String[maxLength];
		values = new Object[maxLength];
	}
	
	public void insert(String key, Object value)
	{
		keys[length] = key;
		values[length++] = value;
	}
	
	public String getKey(short index)
	{
		return keys[index];
	}
	
	public Object getValue(short index)
	{
		return values[index];
	}
	
	public Object get(String key)
	{
		for(short i = 0; i < length; i++)
		{
			if(keys[i].equalsIgnoreCase(key))
			{
				return values[i];
			}
		}
		
		return null;
	}
	
	/*
	 * Encode value information in JSON
	 * 
	 */
	
	public Object getEncodedValue(short index)
	{
		Object current = values[index];
		
		if(current instanceof FastMap[])
		{
			StringBuilder builder = new StringBuilder();
			
			for(FastMap map : (FastMap[]) current)
			{
				builder.append("," + Json.encode((FastMap) map));
			}
			
			return "[" + builder.substring(1).toString() + "]";
		}
		else if(current instanceof FastMap)
		{
			return Json.encode((FastMap) current);
		}
		else if(current instanceof String[])
		{
			StringBuilder builder = new StringBuilder();
			
			for(String str : (String[]) current)
			{
				builder.append("," + Json.QUOT_MARK + String.valueOf(str) + Json.QUOT_MARK);
			}
			
			return "[" + builder.substring(1).toString() + "]";
		}
		else if(current instanceof String)
		{
			return Json.QUOT_MARK + String.valueOf(current) + Json.QUOT_MARK;
		}
		
		return current;
	}

}
