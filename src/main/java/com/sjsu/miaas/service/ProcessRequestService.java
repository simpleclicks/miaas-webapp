package com.sjsu.miaas.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import net.minidev.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sjsu.miaas.domain.AmazonInstance;
import com.sjsu.miaas.domain.Request;
import com.sjsu.miaas.repository.AmazonInstanceRepository;
import com.sjsu.miaas.repository.UserRepository;
import com.sjsu.miaas.web.rest.AmazonInstanceResource;

@Service
@Transactional
public class ProcessRequestService {

    private final Logger log = LoggerFactory.getLogger(ProcessRequestService.class);
    
    @Inject
    private AmazonInstanceRepository amaInstanceRepository;
    
    public void processRequest(Request req) throws IOException {
    	
    	List<AmazonInstance> allInstances = amaInstanceRepository.findAll();
    	
    	for (AmazonInstance amazonInstance : allInstances) {
    		BigDecimal resrcQuantity = new BigDecimal(req.getResourceQuantity().toString());
			if(amazonInstance.getAvailableResources().compareTo(resrcQuantity) >= 0){
				//sendRequestToAmazonInstance(req);
			}
		}
    	
    	sendRequestToAmazonInstance(req);

	}
    
    private JSONObject mockDevicesonInstance(Request req) {
    	 return null;
    }
    
	private void sendRequestToAmazonInstance(Request req)
			throws MalformedURLException, IOException, ProtocolException {
		ObjectMapper mapper = new ObjectMapper();
    	String data = null;
    	 
    	try {
     
    		// convert user object to json string, and save to a    file
    		 data = mapper.writeValueAsString(req);
     
    		// display to console
    		System.out.println(data);
     
    	} catch (JsonGenerationException e) {
     
    		e.printStackTrace();
     
    	} catch (JsonMappingException e) {
     
    		e.printStackTrace();
     
    	} catch (IOException e) {
     
    		e.printStackTrace();
     
    	}
    	
    	URL targetUrl = new URL("http://ec2-54-69-182-165.us-west-2.compute.amazonaws.com:8080/simpleapp/webapi/androidcontrol/myresource");

		HttpURLConnection httpConnection = (HttpURLConnection) targetUrl.openConnection();
		httpConnection.setDoOutput(true);
		httpConnection.setRequestMethod("POST");
		httpConnection.setRequestProperty("Content-Type", "application/json");

		//String input = "{\"id\":1,\"firstName\":\"Liam\",\"age\":22,\"lastName\":\"Marco\"}";

		OutputStream outputStream = httpConnection.getOutputStream();
		outputStream.write(data.getBytes());
		outputStream.flush();

		if (httpConnection.getResponseCode() != 200) {
			throw new RuntimeException("Failed : HTTP error code : "
				+ httpConnection.getResponseCode());
		}
		else {
			System.out.println("Success!");
		}

		httpConnection.disconnect();
	}

}
