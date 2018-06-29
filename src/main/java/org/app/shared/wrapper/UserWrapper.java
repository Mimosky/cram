package org.app.shared.wrapper;

import java.util.Collection;
import java.util.Date;

import com.google.gwt.user.client.rpc.IsSerializable;

public class UserWrapper implements IsSerializable {

	private String userName;
	private String userSurname;
	private String userPasswd;
	private String userEmail;
	private Date userEntryDate;
	private String status;
	private String profile;
	private String typeDeContrat;
	private Collection<ClientWrapper> clients;
	private Collection<CramWrapper> crams;
	private String currentCP;
	private String previousCP;
	private String oldCP;
	private String rTT;
	private String extras;
	private String epargne;
	
	public UserWrapper(){
		super();
	}
	public UserWrapper(String userName, String userSurname, String userEmail, Date userEntryDate, Collection<ClientWrapper> clients){
		super();
		this.userName = userName;
		this.userSurname = userSurname;
		this.userEmail = userEmail;
		this.userEntryDate = userEntryDate;
		this.clients = clients;
	}
	public UserWrapper(String userName, String userSurname, String userEmail){
		super();
		this.userName = userName;
		this.userSurname = userSurname;
		this.userEmail = userEmail;
	}
	public UserWrapper(String userName, String userSurname, String userEmail, Date userEntryDate, Collection<ClientWrapper> clients,String status, String profile, String typeDeContrat){
		super();
		this.userName = userName;
		this.userSurname = userSurname;
		this.userEmail = userEmail;
		this.userEntryDate = userEntryDate;
		this.clients = clients;
		this.status = status;
		this.typeDeContrat = typeDeContrat;
		this.profile = profile;
	}

	public UserWrapper(String userName, String userSurname, String userEmail, Date userEntryDate, Collection<ClientWrapper> clients, Collection<CramWrapper> crams){
		super();
		this.userName = userName;
		this.userSurname = userSurname;
		this.userEmail = userEmail;
		this.userEntryDate = userEntryDate;
		this.clients = clients;
		this.crams = crams;
	}
	
	public UserWrapper(String userName, String userSurname, String userPasswd, String userEmail, Date userEntryDate,
			String status, String profile, String typeDeContrat, Collection<ClientWrapper> clients,
			Collection<CramWrapper> crams) {
		super();
		this.userName = userName;
		this.userSurname = userSurname;
		this.userPasswd = userPasswd;
		this.userEmail = userEmail;
		this.userEntryDate = userEntryDate;
		this.status = status;
		this.profile = profile;
		this.typeDeContrat = typeDeContrat;
		this.clients = clients;
		this.crams = crams;
	}

	
	
	public UserWrapper(String userName, String userSurname, String userPasswd, String userEmail, Date userEntryDate,
			String status, String profile, String typeDeContrat, Collection<ClientWrapper> clients,
			Collection<CramWrapper> crams, String currentCP, String previousCP, String oldCP, String rTT, String extras,
			String epargne) {
		super();
		this.userName = userName;
		this.userSurname = userSurname;
		this.userPasswd = userPasswd;
		this.userEmail = userEmail;
		this.userEntryDate = userEntryDate;
		this.status = status;
		this.profile = profile;
		this.typeDeContrat = typeDeContrat;
		this.clients = clients;
		this.crams = crams;
		this.currentCP = currentCP;
		this.previousCP = previousCP;
		this.oldCP = oldCP;
		this.rTT = rTT;
		this.extras = extras;
		this.epargne = epargne;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getProfile() {
		return profile;
	}
	public void setProfile(String profile) {
		this.profile = profile;
	}
	public String getTypeDeContrat() {
		return typeDeContrat;
	}
	public void setTypeDeContrat(String typeDeContrat) {
		this.typeDeContrat = typeDeContrat;
	}
	public Collection<CramWrapper> getCrams() {
		return crams;
	}
	public Collection<ClientWrapper> getClients() {
		return clients;
	}
	public void setClients(Collection<ClientWrapper> clients) {
		this.clients = clients;
	}
	public String getUserPasswd() {
		return userPasswd;
	}
	public void setUserPasswd(String userPasswd) {
		this.userPasswd = userPasswd;
	}
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserSurname() {
		return userSurname;
	}

	public void setUserSurname(String userSurname) {
		this.userSurname = userSurname;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public Date getUserEntryDate() {
		return userEntryDate;
	}

	public void setUserEntryDate(Date userEntryDate) {
		this.userEntryDate = userEntryDate;
	}
	public String getCurrentCP() {
		return currentCP;
	}
	public void setCurrentCP(String currentCP) {
		this.currentCP = currentCP;
	}
	public String getPreviousCP() {
		return previousCP;
	}
	public void setPreviousCP(String previousCP) {
		this.previousCP = previousCP;
	}
	public String getOldCP() {
		return oldCP;
	}
	public void setOldCP(String oldCP) {
		this.oldCP = oldCP;
	}
	public String getRTT() {
		return rTT;
	}
	public void setRTT(String rTT) {
		this.rTT = rTT;
	}
	public String getExtras() {
		return extras;
	}
	public void setExtras(String extras) {
		this.extras = extras;
	}
	public String getEpargne() {
		return epargne;
	}
	public void setEpargne(String epargne) {
		this.epargne = epargne;
	}
}
