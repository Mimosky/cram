package org.app.shared.wrapper;

import java.util.Date;

import com.google.gwt.user.client.rpc.IsSerializable;

public class CongesWrapper implements IsSerializable {
	private int congesId;
	private Date start;
	private String startD;
	private Date end;
	private String endD;
	private String type;
	private String status;
	private String name;

	public int getCongesId() {
		return congesId;
	}

	public void setCongesId(int congesId) {
		this.congesId = congesId;
	}

	public CongesWrapper() {
		super();
	}

	public CongesWrapper(String type, Date start, Date end, String startD, String endD) {
		this.start = start;
		this.end = end;
		this.type = type;
		this.startD = startD;
		this.endD = endD;
	}

	public CongesWrapper(int congesId, String type, Date start, Date end, String startD, String endD, String status,
			String name) {
		this.congesId = congesId;
		this.start = start;
		this.end = end;
		this.type = type;
		this.startD = startD;
		this.endD = endD;
		this.status = status;
		this.name = name;
	}

	public CongesWrapper(String type, Date start, Date end, String startD, String endD, String status) {
		this.start = start;
		this.end = end;
		this.type = type;
		this.startD = startD;
		this.endD = endD;
		this.status = status;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStartD() {
		return startD;
	}

	public void setStartD(String startD) {
		this.startD = startD;
	}

	public String getEndD() {
		return endD;
	}

	public void setEndD(String endD) {
		this.endD = endD;
	}

	public Date getStartDate() {
		return start;
	}

	public Date getEndDate() {
		return end;
	}

	public String getStart() {
		String day;
		String month;
		if (start.getDate() < 10)
			day = "0" + Integer.toString(start.getDate());
		else
			day = Integer.toString(start.getDate());
		if (start.getMonth() < 9)
			month = "0" + Integer.toString(start.getMonth() + 1);
		else
			month = Integer.toString(start.getMonth() + 1);
		return day + "/" + month + "/" + Integer.toString(start.getYear() + 1900);
	}

	public void setStart(Date start) {
		this.start = start;
	}

	public String getEnd() {
		String day;
		String month;
		if (end.getDate() < 10)
			day = "0" + Integer.toString(end.getDate());
		else
			day = Integer.toString(end.getDate());
		if (end.getMonth() < 9)
			month = "0" + Integer.toString(end.getMonth() + 1);
		else
			month = Integer.toString(end.getMonth() + 1);
		return day + "/" + month + "/" + Integer.toString(end.getYear() + 1900);
	}

	public void setEnd(Date end) {
		this.end = end;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

}
