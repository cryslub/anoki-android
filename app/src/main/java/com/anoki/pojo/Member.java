package com.anoki.pojo;


import com.anoki.common.Global;


public class Member {
	
	public String apiKey;
	public int team;
	public int user;
	public int role;
	public String state;

	public String name;
	public String picture;

	public Member(){
		this.apiKey = Global.apiKey;
	}
}
