package src;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;


public class LoadUsers {
	
	ArrayList<User> usersList = new ArrayList<User>();
	
	public void loadUserData() throws FileNotFoundException, IOException {
		try (BufferedReader br = new BufferedReader (new FileReader("src/User_names.csv"))) {
			String line = null;
			while ((line = br.readLine()) != null) {
				String[] values = line.split(",");
				User info = new User(values[0], values[1]);
				usersList.add(info);
			}
			
		} catch (NumberFormatException | IOException e) {
			e.printStackTrace();
		}
		
	}

	public int getLenght() {
		
		return usersList.size();
	}

	public Object getUser(int i) {
		// TODO Auto-generated method stub
		return usersList.get(i);
	}

	public void addUser(User newuser) {
		usersList.add(newuser);
		
	}

}
