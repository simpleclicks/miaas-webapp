package com.sjsu.miaas.service;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.List;

import javax.inject.Inject;

import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.amazonaws.util.json.JSONException;
import com.sjsu.miaas.domain.AmazonInstance;
import com.sjsu.miaas.domain.Device;
import com.sjsu.miaas.repository.AmazonInstanceRepository;
import com.sjsu.miaas.repository.DeviceRepository;

@Service
@Transactional

public class StopEmulatorService {

	@Inject
	private AmazonInstanceRepository amaInstanceRepository;

	@Inject
	private DeviceRepository devRepository;

	public String stop(String id)throws MalformedURLException, IOException, ProtocolException, JSONException{
		String devId = null;
	//	Device deviceInfo=null;
		
		Device device = devRepository.getDevicesByDeviceId(id);
		
		//devId = deviceInfo.getDeviceId();

		BigInteger amzInsId = device.getAmazoninstance_id();

		AmazonInstance amzInst = amaInstanceRepository.findOne(amzInsId.longValue());


		URL targetUrl = new URL("http://" +amzInst.getPublicDnsName() + ":8080/simpleapp/webapi/androidcontrol/stopemulator/id");
//		URL targetUrl = new URL("http://ec2-54-148-4-92.us-west-2.compute.amazonaws.com:8080/simpleapp/webapi/androidcontrol/startemulator");
		HttpURLConnection httpConnection = (HttpURLConnection) targetUrl.openConnection();
		httpConnection.setDoOutput(true);
		httpConnection.setRequestMethod("GET");
		//httpConnection.setRequestProperty("Content-Type", "application/json");


		//String input = "{\"id\":1,\"firstName\":\"Liam\",\"age\":22,\"lastName\":\"Marco\"}";
//		JSONObject jsonObj = new JSONObject();
//		
//			try {
//				jsonObj.put("deviceid", id);
//			//	jsonObj.put("deviceid", "3a043084-35ac-4e53-8b88-aa59a2c848dd");
//			} catch (org.json.JSONException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
		
		
//		OutputStreamWriter outputStream = new  OutputStreamWriter(httpConnection.getOutputStream());
//		outputStream.write(jsonObj.toString());
//		outputStream.flush();
		InputStream is = httpConnection.getInputStream();
		StringBuffer sb = new StringBuffer();
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


		}
		
		if((sb.toString()).equalsIgnoreCase("stopped")){
			device.setDeviceStatus("stopped");
		}else{
			device.setDeviceStatus("error");
		}
		httpConnection.disconnect();
		return sb.toString();



	}


}