package model;

public class JPBlogDto {

	private int bno;
	private String title;
	private String content;
	private String userid;
	private int views;
	private int likeCount;
	private String imgfile;
	private String regdate;
	private int commentCount;
	private String category; // CITY/FESTIVAL/FOOD/ETC
	private String tags; // 화면용: "#도쿄 #라멘" 처럼 합쳐서 보여줄 때
	private String type; // 세부 카테고리: 도시명/축제명/음식명 등
	private String city; // 도시명
	private String festival;
	private String food;
	private Double lat;
	private Double lng;

	
	
	

	public Double getLat() {
		return lat;
	}

	public void setLat(Double lat) {
		this.lat = lat;
	}

	public Double getLng() {
		return lng;
	}

	public void setLng(Double lng) {
		this.lng = lng;
	}

	public String getFestival() {
		return festival;
	}

	public void setFestival(String festival) {
		this.festival = festival;
	}

	public String getFood() {
		return food;
	}

	public void setFood(String food) {
		this.food = food;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public int getLikeCount() {
		return likeCount;
	}

	public void setLikeCount(int likeCount) {
		this.likeCount = likeCount;
	}

	public String getImgfile() {
		return imgfile;
	}

	public void setImgfile(String imgfile) {
		this.imgfile = imgfile;
	}

	public int getCommentCount() {
		return commentCount;
	}

	public void setCommentCount(int commentCount) {
		this.commentCount = commentCount;
	}

	public int getBno() {
		return bno;
	}

	public void setBno(int bno) {
		this.bno = bno;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public int getViews() {
		return views;
	}

	public void setViews(int views) {
		this.views = views;
	}

	public String getRegdate() {
		return regdate;
	}

	public void setRegdate(String regdate) {
		this.regdate = regdate;
	}

}
