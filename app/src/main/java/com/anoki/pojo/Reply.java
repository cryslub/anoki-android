package com.anoki.pojo;

import com.anoki.common.Global;

public class Reply {
	
	public String apiKey;
	public String name;
	public String picture;
	public String text;
	public String time;
	public String type;
	public String pub;
	public int prayer;
	public int userId;

	public Reply(){
		apiKey = Global.apiKey;
	}
	
}
