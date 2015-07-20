package com.anoki.pojo;

import com.anoki.common.Global;

import java.io.Serializable;

public class Search {
	public int id;
	public String apiKey;
	public String searchKey;
	public String searchType;
	public int searchId;
	public int page = 0;
	public int size = 100;

	public Search(){
		this.apiKey = Global.apiKey;
	}
}
