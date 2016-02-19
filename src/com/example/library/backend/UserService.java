package com.example.library.backend;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
	
/* this class will handle features for the different users
 * and will be fully implemented during the SECOND ITERATION */
public class UserService {

	private static UserService instance;

	/**
	 */
	public static UserService createDemoService() throws FileNotFoundException, IOException {
		if (instance == null) {
			final UserService UserService = new UserService();

			// Read from a config file and populate the UserService
//			File file = new File("UsersConfigure.txt");
//			try (BufferedReader br = new BufferedReader(new FileReader(file))) {
//				String line;
//				while ((line = br.readLine()) != null) {
//					String[] userInfo = line.split("%%%%");
//					String name = userInfo[0];
//					String email = userInfo[1];
//					String phone = userInfo[2];
//					String borrowedDelimitedByComma = userInfo[3];
//					List<Book> borrowed = Arrays.asList(borrowedDelimitedByComma.split(","));
//					String holdsDelimitedByComma = userInfo[4];
//					List<String> holds = Arrays.asList(holdsDelimitedByComma.split(","));
//					User user = new User(name, email, phone, borrowed, holds);
//					UserService.save(user);
//				}
//			}

			instance = UserService;
		}

		return instance;
	}

	private HashMap<Long, User> Users = new HashMap<>();

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