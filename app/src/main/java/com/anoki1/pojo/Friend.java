package com.anoki1.pojo;

import android.net.Uri;

import com.anoki1.common.Global;

import java.io.Serializable;

public class Friend implements Serializable{

	public int id;
	public String apiKey;
	public int user;
	public int friend;	
	public String picture;
	public String name;
	public String state;
	public String phone;
	public Uri photo;

	public Friend(){
		this.apiKey = Global.apiKey;
	}
}
