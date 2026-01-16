package model;

public class JPCommentDto {

	private int cno;
	private int bno;
	private String userid;
	private String content;
	private String regdate;
    private String blogTitle;

    
    public String getBlogTitle() {
		return blogTitle;
	}
    
	public void setBlogTitle(String blogTitle) {
		this.blogTitle = blogTitle;
	}
	
	public int getCno() {
		return cno;
	}
	public void setCno(int cno) {
		this.cno = cno;
	}
	public int getBno() {
		return bno;
	}
	public void setBno(int bno) {
		this.bno = bno;
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getRegdate() {
		return regdate;
	}
	public void setRegdate(String regdate) {
		this.regdate = regdate;
	}
	
	
}
