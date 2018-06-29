package org.app.shared.dispatch;

import java.util.Date;
import java.util.List;

import org.app.shared.wrapper.WorkLoadWrapper;

import com.gwtplatform.dispatch.rpc.shared.ActionImpl;

public class GeneratePdfAction extends ActionImpl<GeneratePdfResult> {

	private String email;
	private String client;
	private Date date;
	private List<WorkLoadWrapper> work;
	
	@SuppressWarnings("unused")
	private GeneratePdfAction(){
	}
	
	@Override
	public boolean isSecured() {
		return false;
	}
	
	public GeneratePdfAction(String email, List<WorkLoadWrapper> work, String client, Date date) {
		this.email = email;
		this.client = client;
		this.work = work;
		this.date = date;
	}
	
	public String getEmail() {
		return email;
	}
	public String getClient() {
		return client;
	}
	public List<WorkLoadWrapper> getWork() {
		return work;
	}
	public Date getDate() {
		return date;
	}
}
