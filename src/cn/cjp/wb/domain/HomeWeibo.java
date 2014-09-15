package cn.cjp.wb.domain;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Ö÷Ò³Î¢²©
 * @author REAL
 */
public class HomeWeibo {

	int maxPage = 0;
	
	long previous_cursor = 0L;
	
	long next_cursor = 0L;

	List<Weibo> weibos = new ArrayList<Weibo>();
	
	public int getMaxPage() {
		return maxPage;
	}

	public void setMaxPage(int maxPage) {
		this.maxPage = maxPage;
	}

	public long getPrevious_cursor() {
		return previous_cursor;
	}

	public void setPrevious_cursor(long previous_cursor) {
		this.previous_cursor = previous_cursor;
	}

	public long getNext_cursor() {
		return next_cursor;
	}

	public void setNext_cursor(long next_cursor) {
		this.next_cursor = next_cursor;
	}

	public List<Weibo> getWeibos() {
		return weibos;
	}

	public void setWeibos(List<Weibo> weibos) {
		this.weibos = weibos;
	}
	
	public static HomeWeibo fromJson(String json) throws JSONException
	{
		HomeWeibo homeWeibo = new HomeWeibo();
		List<Weibo> weibos = new ArrayList<Weibo>();
		
		JSONObject jo = new JSONObject(json);
		homeWeibo.setMaxPage(jo.getInt("maxPage"));
		homeWeibo.setNext_cursor(jo.getLong("next_cursor"));
		homeWeibo.setPrevious_cursor(jo.getLong("previous_cursor"));
		
		JSONArray jsonArray = jo.getJSONArray("cards").getJSONObject(0).getJSONArray("card_group");
		
		for (int i=0; i<jsonArray.length(); i++) {
			JSONObject obj = jsonArray.getJSONObject(i);
			
			Weibo weibo = new Weibo();
			JSONObject weiboJsonObj = obj.getJSONObject("mblog");
			weibo.setMid(weiboJsonObj.getLong("mid"));
			weibo.setSource(weiboJsonObj.getString("source"));
			weibo.setText(weiboJsonObj.getString("text"));
			
			JSONObject userJsonObj = weiboJsonObj.getJSONObject("user");
			User user = new User();
			user.setHeadUrl(userJsonObj.getString("profile_image_url"));
			user.setScreenName(userJsonObj.getString("screen_name"));
			weibo.setUser(user);
			
			weibos.add(weibo);
		}
		
		homeWeibo.setWeibos(weibos);
		
		return homeWeibo;
	}
	
	
}
