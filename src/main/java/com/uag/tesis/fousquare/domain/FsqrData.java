package com.uag.tesis.fousquare.domain;

public class FsqrData {
	
	private String runDate;
	private String pointName;
	private Integer users;
	private Integer checkins;
	private Integer likes;
	
	public FsqrData(String runDate, String pointName, Integer users, Integer checkins, Integer likes) {
		super();
		this.runDate = runDate;
		this.pointName = pointName;
		this.users = users;
		this.checkins = checkins;
		this.likes = likes;
	}
	
	public String getRunDate() {
		return runDate;
	}
	public void setRunDate(String runDate) {
		this.runDate = runDate;
	}
	public String getPointName() {
		return pointName;
	}
	public void setPointName(String pointName) {
		this.pointName = pointName;
	}
	public Integer getUsers() {
		return users;
	}
	public void setUsers(Integer users) {
		this.users = users;
	}
	public Integer getCheckins() {
		return checkins;
	}
	public void setCheckins(Integer checkins) {
		this.checkins = checkins;
	}
	public Integer getLikes() {
		return likes;
	}
	public void setLikes(Integer likes) {
		this.likes = likes;
	}
}
