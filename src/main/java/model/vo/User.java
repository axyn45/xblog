package model.vo;

public class User {
	private String userId = "";
	private String passWord = "";
	private String userName = "";
	private String userEmail = "";
	private String userMajor = "";
	private String userGroup = "user";
	
	public User() {}
	
	public User(String userId,String passWord) {
		this.userId = userId;
		this.passWord = passWord;
	}
	
	public User(String userId,String passWord,String userName,String userEmail,String userMajor) {
		this.userId = userId;
		this.passWord = passWord;
		this.userName = userName;
		this.userEmail = userEmail;
		this.userMajor = userMajor;
		this.userGroup = "user";
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPassWord() {
		return passWord;
	}

	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public String getUserMajor() {
		return userMajor;
	}

	public void setUserMajor(String userMajor) {
		this.userMajor = userMajor;
	}
}
