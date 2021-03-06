package com.hc9.web.main.common.messagepush;

import java.util.Arrays;
import java.util.HashSet;

import com.alibaba.fastjson.JSONObject;

public abstract class IOSNotification extends UmengNotification {

	// Keys can be set in the aps level
	protected static final HashSet<String> APS_KEYS = new HashSet<String>(Arrays.asList(new String[]{
			"alert", "badge", "sound", "content-available"
	}));
	
	@Override
	public boolean setPredefinedKeyValue(String key, Object value) {
		if (ROOT_KEYS.contains(key)) {
			// This key should be in the root level
			rootJson.put(key, value);
		} else if (APS_KEYS.contains(key)) {
			// This key should be in the aps level
			JSONObject apsJson = null;
			JSONObject payloadJson = null;
			if (rootJson.containsKey("payload")) {
				payloadJson = rootJson.getJSONObject("payload");
			} else {
				payloadJson = new JSONObject();
				rootJson.put("payload", payloadJson);
			}
			if (payloadJson.containsKey("aps")) {
				apsJson = payloadJson.getJSONObject("aps");
			} else {
				apsJson = new JSONObject();
				payloadJson.put("aps", apsJson);
			}
			apsJson.put(key, value);
		} else if (POLICY_KEYS.contains(key)) {
			// This key should be in the body level
			JSONObject policyJson = null;
			if (rootJson.containsKey("policy")) {
				policyJson = rootJson.getJSONObject("policy");
			} else {
				policyJson = new JSONObject();
				rootJson.put("policy", policyJson);
			}
			policyJson.put(key, value);
		} 
//		else {
//			if (key == "payload" || key == "aps" || key == "policy") {
//				throw new Exception("You don't need to set value for " + key + " , just set values for the sub keys in it.");
//			} else {
//				throw new Exception("Unknownd key: " + key);
//			}
//		}
		
		return true;
	}
	// Set customized key/value for IOS notification
	public boolean setCustomizedField(String key, String value) {
		//rootJson.put(key, value);
		JSONObject payloadJson = null;
		if (rootJson.containsKey("payload")) {
			payloadJson = rootJson.getJSONObject("payload");
		} else {
			payloadJson = new JSONObject();
			rootJson.put("payload", payloadJson);
		}
		payloadJson.put(key, value);
		return true;
	}

	public void setAlert(String token) {
    	setPredefinedKeyValue("alert", token);
    }
	
	public void setBadge(Integer badge) {
    	setPredefinedKeyValue("badge", badge);
    }
	
	public void setSound(String sound) {
    	setPredefinedKeyValue("sound", sound);
    }
	
	public void setContentAvailable(Integer contentAvailable) {
    	setPredefinedKeyValue("content-available", contentAvailable);
    }
}
