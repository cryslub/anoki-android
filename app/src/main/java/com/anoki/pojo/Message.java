package com.anoki.pojo;

import com.anoki.common.Global;

import java.io.Serializable;

public class Message implements Serializable {
	
	public String apiKey;
	public String text; //내용
	public String userPicture; //전송자 사진경로
	public String picture;
	public String time; //시간
	public String sender; //전송자 이름
	public int id; //id
	public String back; //배경색
	public int user;

	public Message(){
		this.apiKey = Global.apiKey;
	}
}
