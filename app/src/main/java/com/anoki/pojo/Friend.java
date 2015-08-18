package com.anoki.pojo;

import android.net.Uri;
import android.provider.Settings;
import com.anoki.common.Global;

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
