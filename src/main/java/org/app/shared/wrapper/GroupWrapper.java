package org.app.shared.wrapper;

import java.util.ArrayList;
import java.util.Collection;

import com.google.gwt.user.client.rpc.IsSerializable;

public class GroupWrapper implements IsSerializable{

	public GroupWrapper(){
		//For serialization
	}
	private String name;
	private UserWrapper manager;
	private Collection<UserWrapper> collaborateurs = new ArrayList<UserWrapper>();
	
	
	public GroupWrapper(String name, UserWrapper manager, Collection<UserWrapper> collaborateurs ){
		this.name = name;
		this.manager = manager;
		this.collaborateurs = collaborateurs;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public UserWrapper getManager() {
		return manager;
	}

	public void setManager(UserWrapper manager) {
		this.manager = manager;
	}

	public Collection<UserWrapper> getCollaborateurs() {
		return collaborateurs;
	}

	public void setCollaborateurs(Collection<UserWrapper> collaborateurs) {
		this.collaborateurs = collaborateurs;
	}
}
