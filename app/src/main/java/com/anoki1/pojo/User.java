package com.anoki1.pojo;

import com.anoki1.common.Global;

import java.io.Serializable;

public class User implements Serializable{
	public int id;
	public String apiKey;
	public String name;
	public String text;
	public String media;
	public String showPhone;
	public String account;
	public String pass;
	public Integer picture;
	public String country;
	public String phone;
	public int dalant;

	public String regid;

	public User(){
		this.apiKey = Global.apiKey;
	}
	
}
