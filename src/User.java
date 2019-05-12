package src;

public class User {
	String userid;
	String password;
	
	
	public String toString() {
		return userid;
	}
	
	User(String userid, String password) {
		this.userid = userid;
		this.password = password;
	}

	public String getUserName() {
		
		return userid;
	}
	
	public String getUPassword() {
		
		return password;
	}

}
