package org.app.shared.dispatch;

import com.gwtplatform.dispatch.rpc.shared.UnsecuredActionImpl;

public class SendEmailAction extends UnsecuredActionImpl<SendEmailResult>{
	
	private String email;
	private String subject;
	private String body;
	private int type;
	private String start;
	private String end;
	private String sender;
	@SuppressWarnings("unused")
	private SendEmailAction(){
	}
	
	public SendEmailAction(String email, String subject, String body, int type){
		this.email = email;
		this.subject = subject;
		this.body = body;
		this.type = type;
	}
	
	public SendEmailAction(String email, String subject, String body, int type, String start, String end, String sender){
		this.email = email;
		this.subject = subject;
		this.body = body;
		this.type = type;
		this.start = start;
		this.end = end;
		this.sender = sender;
		
	}

	public String getEmail() {
		return email;
	}

	public String getSubject() {
		return subject;
	}

	public String getBody() {
		return body;
	}

	public int getType() {
		return type;
	}
	public String getStart() {
		return start;
	}
	public String getEnd() {
		return end;
	}
	public String getSender() {
		return sender;
	}
	
}
