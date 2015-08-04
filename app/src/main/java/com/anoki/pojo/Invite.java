package com.anoki.pojo;

import com.anoki.common.Global;

import java.util.List;

public class Invite {
	
	public String apiKey;
	public int id;
	public int team;
	public List<Integer> friends;
	public List<Phone> phone;
	public Invite(){
		this.apiKey = Global.apiKey;
	}
}
