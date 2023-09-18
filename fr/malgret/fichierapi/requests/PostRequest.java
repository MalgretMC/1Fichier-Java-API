package fr.malgret.fichierapi.requests;

public enum PostRequest {
	
	DOWNLOAD(RequestType.DOWNLOAD, "/get_token.cgi"),
	LIST_FILES(RequestType.FILES, "/ls.cgi"),
	INFORMATIONS_FILES(RequestType.FILES, "/info.cgi"),
	SCAN_FILES(RequestType.FILES, "/scan.cgi"),
	DELETE_FILES(RequestType.FILES, "/rm.cgi"),
	MOVE_FILES(RequestType.FILES, "/mv.cgi"),
	RENAME_FILES(RequestType.FILES, "/rename.cgi"),
	COPY_FILES(RequestType.FILES, "/cp.cgi"),
	ATTRIBUTES_FILES(RequestType.FILES, "/chattr.cgi"),
	LIST_FOLDERS(RequestType.FOLDERS, "/ls.cgi"),
	CREATE_FOLDERS(RequestType.FOLDERS, "/mkdir.cgi"),
	SHARE_FOLDERS(RequestType.FOLDERS, "/share.cgi"),
	MOVE_FOLDERS(RequestType.FOLDERS, "/mv.cgi"),
	DELETE_FOLDERS(RequestType.FOLDERS, "/rm.cgi"),
	ALTER_ACCOUNT(RequestType.USER, "/info.cgi"),
	DEMAND_FTP(RequestType.FTP, "/process.cgi"),
	LISTE_FTP_ACCOUNTS(RequestType.FTP_ACCOUNTS, "/ls.cgi"),
	CREATE_FTP_ACCOUNTS(RequestType.FTP_ACCOUNTS, "/add.cgi"),
	DELETE_FTP_ACCOUNTS(RequestType.FTP_ACCOUNTS, "/rm.cgi"),
	LIST_REMOTE_UPLOAD(RequestType.REMOTE_UPLOAD, "/ls.cgi"),
	INFORMATIONS_REMOTE_UPLOAD(RequestType.REMOTE_UPLOAD, "/info.cgi"),
	REQUEST_REMOTE_UPLOAD(RequestType.REMOTE_UPLOAD, "/request.cgi");

	private final String URL_START = "https://api.1fichier.com/v1/";
	
	private final RequestType reqType;
	private final String urlEnd;

	PostRequest(RequestType reqType, String urlEnd) {
		this.reqType = reqType;
		this.urlEnd = urlEnd;
	}

	public String getUrl() {
		return URL_START + reqType.getUrlPart() + urlEnd;
	}

}
