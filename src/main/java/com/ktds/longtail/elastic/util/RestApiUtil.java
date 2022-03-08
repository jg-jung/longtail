package com.ktds.longtail.elastic.util;


import java.util.Map;
import java.util.HashMap;

import java.nio.charset.Charset;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;


public class RestApiUtil {

	//화주ID 검색
	public static Object getFreightMemberById(String url,  JsonObject param) {

		Map<String, Object> result = new HashMap<>();
		String destUrl = url + "";
		String response = Send(destUrl, param);
		
		JsonParser jsonParser = new JsonParser();
		JsonObject jsonObject = (JsonObject) jsonParser.parse(response);

		result.put("resultCode", jsonObject.get("fd_result_code").toString().replace("\"", ""));
		result.put("resultMsg", jsonObject.get("fd_result_msg").toString().replace("\"", ""));
		result.put("payAuthCode", jsonObject.get("fd_pay_auth_code").toString().replace("\"", ""));
		result.put("payDateTime", jsonObject.get("fd_billing_date").toString().replace("\"", ""));
		result.put("tid", jsonObject.get("fd_tid").toString().replace("\"", ""));
		result.put("prtcCode", jsonObject.get("fd_prt_code").toString().replace("\"", ""));
		result.put("price", jsonObject.get("fd_billing_price").toString().replace("\"", ""));
		result.put("cardCode", jsonObject.get("fd_card_code").toString().replace("\"", ""));
		result.put("fk_pg_billing", jsonObject.get("pk_pg_billing").toString().replace("\"", "")); // 추가
		
		return result;
	}
	
	//차주ID 검색
	public static Object getCarOwnerMemberById(String url,  JsonObject param) {

		Map<String, Object> result = new HashMap<>();
		String destUrl = url + "";
		String response = Send(destUrl, param);
		
		JsonParser jsonParser = new JsonParser();
		JsonObject jsonObject = (JsonObject) jsonParser.parse(response);

		result.put("resultCode", jsonObject.get("fd_result_code").toString().replace("\"", ""));
		result.put("resultMsg", jsonObject.get("fd_result_msg").toString().replace("\"", ""));
		result.put("payAuthCode", jsonObject.get("fd_pay_auth_code").toString().replace("\"", ""));
		result.put("payDateTime", jsonObject.get("fd_billing_date").toString().replace("\"", ""));
		result.put("tid", jsonObject.get("fd_tid").toString().replace("\"", ""));
		result.put("prtcCode", jsonObject.get("fd_prt_code").toString().replace("\"", ""));
		result.put("price", jsonObject.get("fd_billing_price").toString().replace("\"", ""));
		result.put("cardCode", jsonObject.get("fd_card_code").toString().replace("\"", ""));
		result.put("fk_pg_billing", jsonObject.get("pk_pg_billing").toString().replace("\"", "")); // 추가
		
		return result;
	}
	
	//RestTemplate SEND
	public static String Send(String destUrl,  JsonObject param) {
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(new MediaType("application","json",Charset.forName("UTF-8")));    //Response Header to UTF-8  

		HttpEntity<String> entity = new HttpEntity<String>(param.toString(), headers);
		ResponseEntity<String> response = restTemplate.postForEntity(destUrl, entity, String.class);

		String body = "";
		try {

			body = response.getBody();
			JsonParser jsonParser = new JsonParser();
			JsonObject jsonObject = (JsonObject) jsonParser.parse(body);

		} catch (Exception e) {
			//body부분 널값일 경우 익셉션 처리 필요
			e.printStackTrace();
		}

		return body;
	}
	
}
