package com.incresol.app.models;

import java.util.HashMap;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HttpStatusResponse {
	private Map<String,Object> data = new HashMap<>();
	private int statusCode;
	private int errorCode;
	private String message;
	
}
