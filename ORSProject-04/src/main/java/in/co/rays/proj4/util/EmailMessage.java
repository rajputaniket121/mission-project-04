package in.co.rays.proj4.util;

public class EmailMessage {
	private String to;
	private String subject;
	private String message;
	private int messageType = TEXT_MSG;
	public static final int HTML_MSG = 1;
	public static final int TEXT_MSG = 2;
	public EmailMessage() {
		
	}
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public int getMessageType() {
		return messageType;
	}
	public void setMessageType(int messageType) {
		this.messageType = messageType;
	}
	public static int getHtmlMsg() {
		return HTML_MSG;
	}
	public static int getTextMsg() {
		return TEXT_MSG;
	}
	
	
	
	
	

}
