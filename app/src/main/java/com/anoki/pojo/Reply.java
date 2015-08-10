package com.anoki.pojo;

import com.anoki.common.Global;

import java.io.Serializable;

public class Reply implements Serializable {
	
	public String apiKey;
	public String name;
	public String picture;
	public String text;
	public String time;
	public String type;
	public String pub;
	public int prayer;
	public int userId;
	public String userPicture;

	public Reply(){
		apiKey = Global.apiKey;
	}
	
}
