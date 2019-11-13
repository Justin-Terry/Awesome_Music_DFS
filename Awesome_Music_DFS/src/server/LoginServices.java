package server;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import models.User;

/**
 * LoginServices Class
 * @author Justin Terry
 * 
 */

public class LoginServices {

	private static User[] users;
	private static final String USER_PATH = "./src/resources/users.json";

	public LoginServices() {
		users = getUsers();
	}
	
	/**
	 * Creates an array of Users from the users.json file
	 * @param None
	 * @return User[]
	 */
	private User[] getUsers() {
		try {
			// BufferedReader file = new BufferedReader(new FileReader(USER_PATH));
			Gson gson = new Gson();
			JsonReader reader = new JsonReader(new FileReader(USER_PATH));
			User[] data = gson.fromJson(reader, User[].class);
			return data;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Compares email address passed in as a parameter to the email addresses of already registered users.
	 * If found, compares the password, if password matches it returns the user as a string.
	 * @param user email, user password
	 * @return JSON User object in string form, or error string.
	 */
	public String signInUser(String email, String password) {
		System.out.println("Email = " + email + "\nPassword = " + password);
		for (int i = 0; i < users.length; i++) {
			System.out.println(users[i].getEmail() + " " + users[i].getPassword());
			if (users[i].getEmail().equals(email)) {
				if (users[i].getPassword().equals(password)) {
					Gson gson = new Gson();
					String str = gson.toJson(users[i]).toString();
					System.out.println("User: " + email + " was logged in.");
					return str;
				}
			}
		}
		return "Email not found or incorrect password used.";
	}

	/**
	 * Registers a new user so long as the email has not already been registered.
	 * @param user's name, user's email, user's password
	 * @return JSON User object in string form, or error string.
	 */
	public String signUpUser(String name, String email, String password) {
		try {
			// Check if provided email is already registered to a user
			for (User user : users) {
				// Iterate through every user
				if (user.getEmail().equalsIgnoreCase(email))
					return "Email already exists";
			}

			// Generate unique userID
			int userID = users.length;
			// Create new user object with provided name, email, & password
			User newUser = new User(userID, name, email, password);
			Gson gson = new Gson();
			// Create ArrayList to store all current users plus the newly created user
			ArrayList<User> newUserJSON = new ArrayList<User>();
			// Open users.json
			JsonReader reader = new JsonReader(new FileReader(USER_PATH));
			// Retrieve all user data and add to newUserJSON
			User[] data = gson.fromJson(reader, User[].class);
			for (User u : data) {
				// Add each user
				newUserJSON.add(u);
			}
			// Add newly created user to newUserJSON
			newUserJSON.add(newUser);
			// Open users.json
			FileWriter file = new FileWriter(USER_PATH);
			// Replace contents of users.json with newUserJSON
			file.append(gson.toJson(newUserJSON));

			file.close();
			
			System.out.println("New user " + newUser.getEmail() + " was created.");
			return new Gson().toJson(newUser);
		} catch (Exception e) {

		}
		return "Error creating user.";
	}

}
