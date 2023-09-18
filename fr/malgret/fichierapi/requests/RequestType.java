package fr.malgret.fichierapi.requests;

enum RequestType {
	
	DOWNLOAD("download"), FILES("file"), FOLDERS("folder"), USER("user"), FTP("ftp"), FTP_ACCOUNTS("ftp/users"), REMOTE_UPLOAD("remote");

	private final String urlPart;

	RequestType(String urlPart) {
		this.urlPart = urlPart;
	}

	public String getUrlPart() {
		return urlPart;
	}

}
