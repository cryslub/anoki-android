package com.anoki.pojo;


import com.anoki.common.Global;


public class Member {
	
	public String apiKey;
	public int team;
	public int user;
	public String role;
	public String state;

	public Member(){
		this.apiKey = Global.apiKey;
	}
}
