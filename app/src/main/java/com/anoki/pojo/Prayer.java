package com.anoki.pojo;

import com.anoki.common.Global;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class Prayer implements Serializable{

	public String apiKey;
	public String pub;
	public String userPicture;//사용자 사진
	public String userName; //사용자 이름
	public String time; //시간
	public String back;//기도 배경
	public String text;//기도 제목
	public List<Media> media; //미디어 list
	public String prayCount;//중보기도 횟수
	public String replyCount;//댓글 수
	public int id;//id
	public String lastPrayed;//마지막 기도시간
	public int userId;//사용자 id
	public List<Integer> friends;
	public List<String> phone;
	public String scrapd;
	public int responseCount;
	public List<Reply> reply; //미디어 list
	public int team = -1;
	public int dalant;
	public String completed;
	public String raw_time;
	public String long_time;
	public String requestId;
	public int idx;

	public Prayer(){
		apiKey = Global.apiKey;
	}

	public int getTotal(){
		return friends.size() + phone.size();
	}

	public String simpleText(){
		if(text.length() > 0) return text;
		else  return back;
	}


	public boolean checkPrayable() {

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.S");

		boolean pass = false;

		try {
			if (!"null".equals(lastPrayed) && lastPrayed != null) {
				Calendar date = Calendar.getInstance();
				date.setTime(format.parse(lastPrayed));
				Calendar now = Calendar.getInstance();

				double diff = now.getTimeInMillis() - date.getTimeInMillis();
				if (diff > 60 * 60 * 1000) {
					pass = true;
				}
			} else {
				pass = true;
			}

		} catch (ParseException e) {

		}

		return pass;
	}
}
