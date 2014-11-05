package com.sjsu.miaas.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
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
import com.sjsu.miaas.AWSServices.AWSInstanceAction;
import com.sjsu.miaas.domain.AmazonInstance;
import com.sjsu.miaas.domain.Device;
import com.sjsu.miaas.domain.Request;
import com.sjsu.miaas.repository.AmazonInstanceRepository;
import com.sjsu.miaas.repository.DeviceRepository;

@Service
@Transactional
public class ProcessRequestService {

    private final Logger log = LoggerFactory.getLogger(ProcessRequestService.class);
    
    @Inject
    private AmazonInstanceRepository amaInstanceRepository;
    
    @Inject
    private DeviceRepository devRepository;
    
    
    public void processRequest(Request req) throws IOException {
    	
    	List<AmazonInstance> allInstances = amaInstanceRepository.findAll();
    	BigDecimal resrcQuantity = new BigDecimal(req.getResourceQuantity().toString());
    	try{
    	for (AmazonInstance amazonInstance : allInstances) {
    		
			if(amazonInstance.getAvailableResources().compareTo(resrcQuantity) >= 0){
				assignDevicesOnAmazonInstance(req, amazonInstance,
						resrcQuantity);
				break;
			} 
			else if(amazonInstance.getAvailableResources().compareTo(new BigDecimal(0)) > 0){
				BigDecimal assignResrcs = amazonInstance.getAvailableResources();
				assignDevicesOnAmazonInstance(req, amazonInstance,
						assignResrcs);
				resrcQuantity = resrcQuantity.subtract(assignResrcs);
				if(resrcQuantity.compareTo(new BigDecimal(0)) == 0) {
					break;
				}
			}
		}
    	
    	if(resrcQuantity.compareTo(new BigDecimal(0))>0){
    		AWSInstanceAction aia = new AWSInstanceAction();
    		AmazonInstance i = aia.CreateInstance();
    		amaInstanceRepository.save(i);
    		assignDevicesOnAmazonInstance(req, i, resrcQuantity);
    	}
    	}
    	catch(Exception e){
    		e.printStackTrace();
    	}
    	
    	//sendRequestToAmazonInstance(req);

	}

	private void assignDevicesOnAmazonInstance(Request req,
			AmazonInstance amazonInstance, BigDecimal resrcQuantity)
			throws MalformedURLException, IOException, ProtocolException,
			JSONException {
		JSONArray devices = sendRequestToAmazonInstance(req);
		if(devices!=null){
			for(int i=0;i<devices.length();i++){
				JSONObject dev = devices.getJSONObject(i);
				Device d = new Device();
				d.setDeviceId(dev.getString("deviceId"));
				d.setAmazonInstance(amazonInstance);
				d.setAmazoninstance_id(new BigInteger(amazonInstance.getId().toString()));
				d.setDeviceImageName(dev.getString("deviceImage"));
				d.setDeviceMemory( String.valueOf(dev.getInt("deviceMemory")));
				d.setDeviceStatus(dev.getString("deviceStatus"));
				d.setDeviceType(dev.getString("deviceType"));
				d.setDeviceVersion(String.valueOf(dev.getInt("deviceVersion")));
				d.setRequest(req);
				d.setRequest_id(new BigInteger(req.getId().toString()));
				devRepository.save(d);
			}
			amazonInstance.setAvailableResources(amazonInstance.getAvailableResources().subtract(resrcQuantity));
			amaInstanceRepository.save(amazonInstance);
		}
	}
    
    private JSONArray mockDevicesonInstance(Request req) {
    	String devs = "[{deviceType:'Android',deviceImage:'ARM',deviceVersion:19,deviceMemory:512,deviceId:'dev2',deviceStatus:'running'},{deviceType:'Android',deviceImage:'ARM',deviceVersion:19,deviceMemory:512,deviceId:'dev3',deviceStatus:'stopped'},{deviceType:'Android',deviceImage:'ARM',deviceVersion:19,deviceMemory:1024,deviceId:'dev4',deviceStatus:'running'},{deviceType:'Android',deviceImage:'ARM',deviceVersion:19,deviceMemory:512,deviceId:'dev5',deviceStatus:'running'}]";
    	JSONArray devices = null;
    	try {
			devices = new JSONArray(devs);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return devices;
    }
    
	private JSONArray sendRequestToAmazonInstance(Request req)
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
    	
    	URL targetUrl = new URL("http://ec2-54-187-12-86.us-west-2.compute.amazonaws.com:8080/simpleapp/webapi/androidcontrol/assign");

		HttpURLConnection httpConnection = (HttpURLConnection) targetUrl.openConnection();
		httpConnection.setDoOutput(true);
		httpConnection.setRequestMethod("POST");
		httpConnection.setRequestProperty("Content-Type", "application/json");

		//String input = "{\"id\":1,\"firstName\":\"Liam\",\"age\":22,\"lastName\":\"Marco\"}";

		OutputStream outputStream = httpConnection.getOutputStream();
		outputStream.write(data.getBytes());
		outputStream.flush();
		InputStream is = httpConnection.getInputStream();
		StringBuffer sb = new StringBuffer();
		 JSONArray devices = null;
		if (httpConnection.getResponseCode() != 200) {
			throw new RuntimeException("Failed : HTTP error code : "
				+ httpConnection.getResponseCode());
		}
		else {
			int ch;
		      while ((ch = is.read()) != -1) {
		        sb.append((char) ch);
		      }
//		      httpConnection.disconnect();
		     
		    	try {
					devices = new JSONArray(sb.toString());
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    	
		      
		}

		httpConnection.disconnect();
		return devices;
	}

}
