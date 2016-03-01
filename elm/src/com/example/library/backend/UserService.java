package com.example.library.backend;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;
	
/* this class will handle features for the different users
 * and will be fully implemented during the SECOND ITERATION */
public class UserService {

	public static UserService instance;
	public JPAContainer<User> Users = JPAContainerFactory.make(User.class, JPAContainerFactory.createEntityManagerForPersistenceUnit("library_db"));
	/**
	 */
	public static UserService createDemoService() {
		if (instance == null) {
			final UserService UserService = new UserService();
			instance = UserService;
		}

		return instance;
	}


	public synchronized long count() {
		return Users.size();
	}

	public synchronized void delete(User value) {
		// Users.remove(value.getId());
	}

	public synchronized void save(User entry) {
		// if (entry.getId() == null) {
		//
		// }
		// try {
		// entry = (User) entry.clone();
		// } catch (Exception ex) {
		// throw new RuntimeException(ex);
		// }
		// Users.put(entry.getId(), entry);
	}
}