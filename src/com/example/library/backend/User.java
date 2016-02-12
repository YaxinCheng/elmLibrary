package com.example.library.backend;

import java.util.*;

import javax.persistence.*;

/**
 * <h1>User</h1> This class contains the data for library patrons (users),
 * including their personal information, a list of books they've borrowed, and a
 * list of books they are on a hold list for.
 * 
 * @author Team-Elm
 * @version 1.0
 * @since 2016-02-01
 */
@Entity
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	private String name;
	private String email;
	private String phone;
	private ArrayList borrowed;
	private ArrayList holds;
	
	@OneToMany(mappedBy = "user")
	private Set<Book> book;

	//above this part, we made some change on the database part
	public User(String name, String email, String phone, ArrayList borrowed, ArrayList holds) {

		this.name = name;
		this.email = email;
		this.phone = phone;
		this.borrowed = borrowed;
		this.holds = holds;

	}
	public User(){

	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public ArrayList getBorrowed() {
		return borrowed;
	}

	public void setBorrowed(ArrayList borrowed) {
		this.borrowed = borrowed;
	}

	public ArrayList getHolds() {
		return holds;
	}

	public void setHolds(ArrayList holds) {
		this.holds = holds;
	}

	@Override
	public String toString() {
		return "User [name=" + name + ", email=" + email + ", phone=" + phone + ", borrowed=" + borrowed + ", holds="
				+ holds + "]";
	}

	@Override
	public boolean equals(Object object) {
		if (object instanceof User && ((User) object).getEmail() == this.email) {
			return true;
		}
		return false;
	}

	public int compareTo(User o) {
		return this.email.compareTo(o.getEmail());
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		return new User(name, email, phone, borrowed, holds);
	}

}
