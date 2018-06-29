package org.app.shared;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;



@Entity
@Table (name="USER")
public class User {
	

	@Id
	@Column (name ="email")
	private String email;
	@Column (name ="name")
	private String name;
	@Column (name ="surname")
	private String surname;
	@Column (name = "password")
	private String password;
	@Column (name= "entyDate")
	private Date entryDate;
	@Column (name = "status")
	private String status;
	@Column (name = "profile")
	private String profile;
	@Column (name = "contractType")
	private String typeDeContrat;
	@OneToMany(fetch = FetchType.EAGER)
	@Column (name="conges")
	private Collection<Conges> conges = new ArrayList<Conges>();
	@OneToMany(fetch = FetchType.EAGER)
	@Column (name="cram")
	private Collection<Cram> cram = new ArrayList<Cram>();
	@ManyToMany(fetch = FetchType.EAGER)
	private Collection<Client> client = new ArrayList<Client>();
	private double currentCP;
	private double previousCP;
	private double oldCP;
	private double rTT;
	private double extas;
	private double epargne;
	
	public double getCurrentCP() {
		return currentCP;
	}
	public void setCurrentCP(double currentCP) {
		this.currentCP = currentCP;
	}
	public double getPreviousCP() {
		return previousCP;
	}
	public void setPreviousCP(double previousCP) {
		this.previousCP = previousCP;
	}
	public double getOldCP() {
		return oldCP;
	}
	public void setOldCP(double oldCP) {
		this.oldCP = oldCP;
	}
	public double getRTT() {
		return rTT;
	}
	public void setRTT(double rTT) {
		this.rTT = rTT;
	}
	public double getExtas() {
		return extas;
	}
	public void setExtas(double extas) {
		this.extas = extas;
	}
	public double getEpargne() {
		return epargne;
	}
	public void setEpargne(double epargne) {
		this.epargne = epargne;
	}

	
	public Collection<Cram> getCram() {
		return cram;
	}


	public void setCram(Collection<Cram> cram) {
		this.cram = cram;
	}

	public User(){
		super();
	}

	public User(String email, String name, String surname, String password, Date entryDate) {
		super();
		this.email = email;
		this.name = name;
		this.surname = surname;
		this.password = password;
		this.entryDate = entryDate;
	}
	
	public void setClient(Collection<Client> client) {
		this.client = client;
	}
	public Collection<Client> getClient() {
		return client;
	}
	
	public Collection<Conges> getConges() {
		return conges;
	}

	public void setProfile(String profile) {
		this.profile = profile;
	}
	public String getProfile() {
		return profile;
	}

	public void setTypeDeContrat(String typeDeContrat) {
		this.typeDeContrat = typeDeContrat;
	}
	public String getTypeDeContrat() {
		return typeDeContrat;
	}
	public void setConges(Collection<Conges> conges) {
		this.conges = conges;
	}


	public void setStatus(String status) {
		this.status = status;
	}
	public String getStatus() {
		return status;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSurname() {
		return surname;
	}
	public void setSurname(String surname) {
		this.surname = surname;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Date getEntryDate() {
		return entryDate;
	}
	public void setEntryDate(Date entryDate) {
		this.entryDate = entryDate;
	}
	public String getFomatedDate() {
		String day;
		String month;
		if (entryDate.getDate()<10) day="0"+Integer.toString(entryDate.getDate()); else day=Integer.toString(entryDate.getDate());
		if(entryDate.getMonth()<9) month="0"+Integer.toString(entryDate.getMonth()+1); else month=Integer.toString(entryDate.getMonth()+1);
		return day+"/"+month+"/"+Integer.toString(entryDate.getYear()+1900);
	}


	public User(String email, String name, String surname, String password, Date entryDate, String status,
			String profile, String typeDeContrat) {
		super();
		this.email = email;
		this.name = name;
		this.surname = surname;
		this.password = password;
		this.entryDate = entryDate;
		this.status = status;
		this.profile = profile;
		this.typeDeContrat = typeDeContrat;
	}
	public User(String email, String name, String surname, String password, Date entryDate, String status,
			String profile, String typeDeContrat, Collection<Conges> conges, Collection<Cram> cram,
			Collection<Client> client, double currentCP, double previousCP, double oldCP, double rTT, double extas,
			double epargne) {
		super();
		this.email = email;
		this.name = name;
		this.surname = surname;
		this.password = password;
		this.entryDate = entryDate;
		this.status = status;
		this.profile = profile;
		this.typeDeContrat = typeDeContrat;
		this.conges = conges;
		this.cram = cram;
		this.client = client;
		this.currentCP = currentCP;
		this.previousCP = previousCP;
		this.oldCP = oldCP;
		this.rTT = rTT;
		this.extas = extas;
		this.epargne = epargne;
	}
	

}
