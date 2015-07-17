package com.anoki.pojo;

import com.anoki.common.Global;

import java.io.Serializable;

public class Search {
	public int id;
	public String apiKey;
	public String searchKey;
	public String searchType;
	public int searchId;
	public int page;
	public int size;

	public Search(){
		this.apiKey = Global.apiKey;
	}
}
