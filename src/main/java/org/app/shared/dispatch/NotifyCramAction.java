package org.app.shared.dispatch;

import java.util.Date;
import java.util.List;

import org.app.shared.wrapper.WorkLoadWrapper;

import com.gwtplatform.dispatch.rpc.shared.ActionImpl;

public class NotifyCramAction extends ActionImpl<NotifyCramResult> {

	private String email;
	private List<WorkLoadWrapper> work;
	private Date date;

	@SuppressWarnings("unused")
	private NotifyCramAction() {
	}

	@Override
	public boolean isSecured() {
		return false;
	}

	public NotifyCramAction(String email, List<WorkLoadWrapper> work, Date date) {
		this.email = email;
		this.work = work;
		this.date = date;
	}

	public List<WorkLoadWrapper> getWork() {
		return work;
	}
	public String getEmail() {
		return email;
	}
	public Date getDate() {
		return date;
	}
}
