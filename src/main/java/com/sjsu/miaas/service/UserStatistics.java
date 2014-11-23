package com.sjsu.miaas.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import javax.inject.Inject;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sjsu.miaas.domain.Request;
import com.sjsu.miaas.repository.RequestRepository;

@Service
@Transactional

public class UserStatistics {
	
	@Inject
	private RequestRepository requestRepository;
	
	

	public JSONObject userData(String id) throws JSONException{
		
		HashMap<String, Integer> apiMap = new HashMap<String, Integer>();
		HashMap<String, Integer> memoryMap = new HashMap<String, Integer>();
		JSONObject userData = new JSONObject();
		JSONObject api = new JSONObject();
		JSONObject memory = new JSONObject();
		List<Request> reqs = requestRepository.getRequestsByUserId(id);
		String tempApi = "api";
		String tempMem = "mem";
		for (Request request : reqs) {
			tempApi = request.getResourceVersion();
			tempMem = request.getResourceMemory();
			if(apiMap.containsKey(tempApi)){
				apiMap.put(tempApi, apiMap.get(tempApi)+request.getRequestPrice());
			}else{
				apiMap.put(tempApi, request.getRequestPrice());
			}
			if(memoryMap.containsKey(tempMem)){
				memoryMap.put(tempMem, memoryMap.get(tempMem)+request.getRequestPrice());
			}else{
				memoryMap.put(tempMem, request.getRequestPrice());
			}
		}	
		
		userData.put("userId", id);
		for (Entry<String, Integer> entry : apiMap.entrySet()) {
		    String key = entry.getKey();
		    int value = entry.getValue();
		    api.put(key, value);
		}
		userData.put("api", api);
		for (Entry<String, Integer> entry : memoryMap.entrySet()) {
		    String key = entry.getKey();
		    int value = entry.getValue();
		    memory.put(key, value);
		}
		userData.put("memory", memory);
		
		return userData;
				
	}
	
	
	
	
}