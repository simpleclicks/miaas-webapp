package com.sjsu.miaas.service;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import javax.inject.Inject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.amazonaws.services.ec2.model.Instance;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.sjsu.miaas.AWSServices.AWSInstanceAction;
import com.sjsu.miaas.domain.AmazonInstance;
import com.sjsu.miaas.domain.Device;
import com.sjsu.miaas.domain.Request;
import com.sjsu.miaas.repository.AmazonInstanceRepository;
import com.sjsu.miaas.repository.DeviceRepository;
import com.sjsu.miaas.repository.RequestObject;
import com.sjsu.miaas.repository.RequestRepository;

@Service
@Transactional
public class NetworkAdminService {
	
	private final Logger log = LoggerFactory.getLogger(NetworkAdminService.class);


	@Inject
	private AmazonInstanceRepository amaInstanceRepository;

	@Inject
	private DeviceRepository devRepository;
	
	
	@Inject
	private RequestRepository reqRepository;
	
	public String getSumofRequestPrice() throws JSONException{
//		List<RequestObject> r1 = new ArrayList<RequestObject>();
//		
//		List<Object[]> r2 = reqRepository.getPricebyRequestVersions();
//		
//		for(Object[] obj : r2){
//			RequestObject r = new RequestObject();
//			r.setResourceVersion((String)obj[0]);
//			r.setResourcePrice(((Long)obj[1]).intValue());
//			r1.add(r);
//		}
//		Gson gson = new GsonBuilder().create();
//		String re1 = gson.toJson(r1);
//		
//		return re1;
		
		List<Request> reqs = reqRepository.findAll();
		
		
		HashMap<String, Integer> apiMap = new HashMap<String, Integer>();
		HashMap<String, Integer> memoryMap = new HashMap<String, Integer>();
		JSONObject userData = new JSONObject();
		JSONObject api = new JSONObject();
		JSONObject memory = new JSONObject();
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
		
		return userData.toString();
		
		

		


				
	}
	
	
	
	
	
	
}
