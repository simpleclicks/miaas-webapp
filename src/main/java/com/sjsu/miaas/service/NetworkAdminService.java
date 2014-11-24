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
		List<RequestObject> r1 = new ArrayList<RequestObject>();
		
		List<Object[]> r2 = reqRepository.getPricebyRequestVersions();
		
		for(Object[] obj : r2){
			RequestObject r = new RequestObject();
			r.setResourceVersion((String)obj[0]);
			r.setResourcePrice((Integer)obj[1]);
			r1.add(r);
		}
		Gson gson = new GsonBuilder().create();
		String re1 = gson.toJson(r1);
		
		return re1;
				
	}
	
	
	
	
	
	
}
