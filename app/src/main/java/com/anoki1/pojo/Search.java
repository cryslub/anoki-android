package com.anoki1.pojo;

import com.anoki1.common.Global;

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
