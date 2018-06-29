package org.app.shared.dispatch;

import com.gwtplatform.dispatch.rpc.shared.ActionImpl;

public class ModifyUserAction extends ActionImpl<ModifyUserResult> {

	private String name;
	private String surname;
	private String email;
	private String password;
	private String date;
	private String clientsName;
	private String status;
	private String profile;
	private String typeDeContrat;
	private boolean myPassword;


	public ModifyUserAction(String name, String surname, String email, String password, String date, String clientsName,String status,String profile, String typeDeContrat) {
		super();
		this.name = name;
		this.surname = surname;
		this.email = email;
		this.password = password;
		this.date = date;
		this.clientsName = clientsName;
		this.status = status;
		this.profile = profile;
		this.typeDeContrat = typeDeContrat;
		this.myPassword = false;
	}
	public ModifyUserAction(String email, String password){
		this.email = email;
		this.password = password;
		this.myPassword = true;
	}
	@Override
	public boolean isSecured() {
		return false;
	}
	@SuppressWarnings("unused")
	private ModifyUserAction() {
		// For serialization only
	}
	public String getName() {
		return name;
	}
	public String getSurname() {
		return surname;
	}
	public String getEmail() {
		return email;
	}
	public String getPassword() {
		return password;
	}
	public String getDate() {
		return date;
	}
	public String getClientsName() {
		return clientsName;
	}
	public String getStatus() {
		return status;
	}
	public String getProfile() {
		return profile;
	}
	public String getTypeDeContrat() {
		return typeDeContrat;
	}
	public boolean getMyPassword(){
		return myPassword;
	}

	
}
