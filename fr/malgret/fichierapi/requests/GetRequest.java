package fr.malgret.fichierapi.requests;

public enum GetRequest implements Request {
	
	SERVER_UPLOAD("upload/get_upload_server.cgi");

	private final String urlEnd;

	GetRequest(String urlEnd) {
		this.urlEnd = urlEnd;
	}
	
	public String getUrl() {
		return URL_START + urlEnd;
	}

}
