package com.quest.etna.model;
import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonProperty;



/**
 * @author abdal
 *
 	@GeneratedValue(strategy=GenerationType.AUTO)*/
@Entity
public class User {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	int id;
	
	@Column(nullable=false, unique=true)
	String username;
	
	@Column(nullable=false)
	String password;
	
    @OneToMany(mappedBy = "user")
    List<Address> addressesList;
	
    
    @Enumerated(EnumType.STRING)
    @Column(name = "role", columnDefinition = "varchar(255) default 'ROLE_USER'")
    private UserRole role = UserRole.ROLE_USER;
    
	LocalDateTime creation_date;
	
    @UpdateTimestamp
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	LocalDateTime updated_date;
	


	public User() {}
	
	public User(String username, String password) {
		this.username = username.substring(0, 1).toUpperCase()+ username.substring(1).toLowerCase();
		this.password = password;
		this.creation_date = LocalDateTime.now();
	}
	
	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public UserRole getRole() {
		return role;
	}

	
	public void setRole(UserRole role) {
		this.role = role;
	}

	public LocalDateTime getcreation_date() {
		return creation_date;
	}

	public void setcreation_date(LocalDateTime creation_date) {
		this.creation_date = creation_date;
	}

	public LocalDateTime getupdated_date() {
		return updated_date;
	}

	public void setupdated_date(LocalDateTime updated_date) {
		this.updated_date = updated_date;
	}
	
	public void update_date() {
		this.updated_date = LocalDateTime.now();;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((creation_date == null) ? 0 : creation_date.hashCode());
		result = prime * result + id;
		result = prime * result + ((password == null) ? 0 : password.hashCode());
		result = prime * result + ((role == null) ? 0 : role.hashCode());
		result = prime * result + ((updated_date == null) ? 0 : updated_date.hashCode());
		result = prime * result + ((username == null) ? 0 : username.hashCode());
		return result;
	}

	

	@Override
	public String toString() {
		return "User [id=" + id + ", username=" + username + ", password=" + password + ", role=" + role
				+ ", creation_date=" + creation_date + ", updated_date=" + updated_date + "]";
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (creation_date == null) {
			if (other.creation_date != null)
				return false;
		} else if (!creation_date.equals(other.creation_date))
			return false;
		if (id != other.id)
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		if (role == null) {
			if (other.role != null)
				return false;
		} else if (!role.equals(other.role))
			return false;
		if (updated_date == null) {
			if (other.updated_date != null)
				return false;
		} else if (!updated_date.equals(other.updated_date))
			return false;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}

	public void setAddress(Address mAddress) {
		// TODO Auto-generated method stub
		this.addressesList.add(mAddress);
	}

	public List<Address> getAddressesList() {
		// TODO Auto-generated method stub
		return addressesList;
	}
	
}



