package org.app.shared;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table (name="GROUPE")
public class Groupe {
	@Id
	@Column (name ="groupName")
	private String groupName;
	@OneToOne(fetch = FetchType.EAGER)
	private User manager;
	@OneToMany(fetch = FetchType.EAGER)
	private Collection<User> collaborateurs = new ArrayList<User>();
	
	public Groupe(){
		super();
	}
	
	public Groupe(String groupName, User manager, Collection<User> collaborateurs ){
		this.groupName = groupName;
		this.manager = manager;
		this.collaborateurs = collaborateurs;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public User getManager() {
		return manager;
	}

	public void setManager(User manager) {
		this.manager = manager;
	}

	public Collection<User> getCollaborateurs() {
		return collaborateurs;
	}

	public void setCollaborateurs(Collection<User> collaborateurs) {
		this.collaborateurs = collaborateurs;
	}
	
}
