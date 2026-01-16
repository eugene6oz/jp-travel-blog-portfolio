package model;

public class MemberDto {

	private String userid;
	private String password;
	private String writer;
	private String phone;
	private String email;
	private String role;
	private String profile_Img;
	private String regdate;
	
	
	
	public String getProfile_Img() {
		return profile_Img;
	}
	public void setProfile_Img(String profile_Img) {
		this.profile_Img = profile_Img;
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getWriter() {
		return writer;
	}
	public void setWriter(String writer) {
		this.writer = writer;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public String getRegdate() {
		return regdate;
	}
	public void setRegdate(String regdate) {
		this.regdate = regdate;
	}

	
}
