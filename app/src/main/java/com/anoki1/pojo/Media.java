package com.anoki1.pojo;

import java.io.Serializable;

public class Media implements Serializable {
	public String id;
	public String type;

	public Media(String id) {
		this.id = id;
	}
}
