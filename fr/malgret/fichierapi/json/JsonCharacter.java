package fr.malgret.fichierapi.json;

public enum JsonCharacter {
	
	QUOT_MARK('"', '"'), BRACKET('[', ']'), ACCOLADE('{', '}'), NO_MARK(',', ',');

	private final char start, end;

	JsonCharacter(char start, char end) {
		this.start = start;
		this.end = end;
	}

	public char getStart() {
		return start;
	}

	public char getEnd() {
		return end;
	}
	
	public static JsonCharacter fromStart(char start)
	{
		for(JsonCharacter c : values())
		{
			if(c.start == start) return c;
		}
		
		return null;
	}
	
	public static JsonCharacter fromEnd(char end)
	{
		for(JsonCharacter c : values())
		{
			if(c.end == end) return c;
		}
		
		return null;
	}

}
