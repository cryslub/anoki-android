package com.anoki.pojo;

import java.util.List;

public class Prayer {

	public String apiKey;
	public String pub;
	public String userPicture;//사용자 사진
	public String userName; //사용자 이름
	public String time; //시간
	public String back;//기도 배경
	public String text;//기도 제목
	public List<String> media; //미디어 list
	public String prayCount;//중보기도 횟수
	public String replyCount;//댓글 수
	public int id;//id
	public String lastPrayed;//마지막 기도시간
	public int userId;//사용자 id
	public List<Integer> friends;
	public List<Phone> phone;	
	public String scrapd;
	public String responseCount;
	public List<Reply> reply; //미디어 list
	public int team = -1;
}
