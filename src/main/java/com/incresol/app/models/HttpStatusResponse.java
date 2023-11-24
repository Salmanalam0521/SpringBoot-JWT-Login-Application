package com.incresol.app.models;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class HttpStatusResponse {
	
	private Map<String,Object> data = new HashMap<>();
	private int statusCode;
	private int errorCode;
	private String message;
	
//	public String convertTOJson(HttpStatusResponse data) {
//		ObjectMapper map=new ObjectMapper();
//		String jsonString = null;
//		try {
//			jsonString = map.writeValueAsString(data);
//		} catch (JsonProcessingException e) {
//			e.printStackTrace();
//		}
//		return jsonString; 
//		
//	}
	
}
