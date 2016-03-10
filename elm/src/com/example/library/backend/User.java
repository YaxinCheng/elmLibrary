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
	private String account;
	private String password;
	private String name;
	private String email;
	private String phone;
	private List<Book> borrowed;
	private List<Book> waiting;

	@OneToMany(mappedBy = "user")
	private Set<Book> book;

	// Above this part, we made some change on the database part
	// The constructor of User
	public User(String name, String email, String phone, List<Book> borrowed, List<Book> waiting) {

		this.name = name;
		this.email = email;
		this.phone = phone;
		this.borrowed = borrowed;
		this.waiting = waiting;

	}
	
	public User(String account, String password) {
		this.account = account;
		this.password = password;
	}

	public User() {

	}

	// setter and getter
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

	public List<Book> getBorrowed() {
		return borrowed;
	}

	public void setBorrowed(ArrayList<Book> borrowed) {
		this.borrowed = borrowed;
	}

	public List<Book> getWaiting() {
		return waiting;
	}

	public void setWaiting(ArrayList<Book> waiting) {
		this.waiting = waiting;
	}

	/**
	 * @return the account
	 */
	public String getAccount() {
		return account;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param account The account to set
	 */
	public void setAccount(String account){
		this.account = account;
	}
	
	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	
	public boolean isInformationFilled() {
		try {
			return !name.isEmpty() && !phone.isEmpty() && !email.isEmpty();
		} catch (NullPointerException e) {
			return false;
		}
	}

	@Override
	public String toString() {
		return "User [name=" + name + ", email=" + email + ", phone=" + phone + ", borrowed=" + borrowed + ", waiting="
				+ waiting + "]";
	}

	// The method that can check whether the users are same or not
	@Override
	public boolean equals(Object object) {
		if (object instanceof User && ((User) object).getEmail() == this.email) {
			return true;
		}
		return false;
	}

	// The method that can compare two users
	public int compareTo(User o) {
		return this.email.compareTo(o.getEmail());
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		return new User(name, email, phone, borrowed, waiting);
	}
}