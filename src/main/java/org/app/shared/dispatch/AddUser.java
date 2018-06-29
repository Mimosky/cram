package org.app.shared.dispatch;

import com.gwtplatform.dispatch.rpc.shared.ActionImpl;

public class AddUser extends ActionImpl<AddUserResult> {

	private String name;
	private String surname;
	private String email;
	private String password;
	private String date;
	private String clientsName;
	private String typeDeContrat;
	private String profile;


	public AddUser(String name, String surname, String email, String password, String date, String clientsName) {
		super();
		this.name = name;
		this.surname = surname;
		this.email = email;
		this.password = password;
		this.date = date;
		this.clientsName = clientsName;
	}
	public AddUser(String name, String surname, String email, String password, String date, String clientsName,String profile, String typeDeContrat) {
		super();
		this.name = name;
		this.surname = surname;
		this.email = email;
		this.password = password;
		this.date = date;
		this.clientsName = clientsName;
		this.profile = profile;
		this.typeDeContrat = typeDeContrat;
	}
	@Override
	public boolean isSecured() {
		return false;
	}
	@SuppressWarnings("unused")
	private AddUser() {
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
	public String getProfile() {
		return profile;
	}
	public String getTypeDeContrat() {
		return typeDeContrat;
	}
	
}
