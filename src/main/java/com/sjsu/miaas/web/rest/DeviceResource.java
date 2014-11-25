package com.sjsu.miaas.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.sjsu.miaas.domain.Device;
import com.sjsu.miaas.repository.DeviceRepository;
import com.sjsu.miaas.service.NetworkAdminService;
import com.sjsu.miaas.service.StartEmulatorService;
import com.sjsu.miaas.service.UserStatistics;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.util.List;

/**
 * REST controller for managing Device.
 */
@RestController
@RequestMapping("/app")
public class DeviceResource {

	private final Logger log = LoggerFactory.getLogger(DeviceResource.class);

	@Inject
	private DeviceRepository deviceRepository;

	@Inject
	private StartEmulatorService startEmulator; 
	
	@Inject
	private NetworkAdminService netAdminServ;

	@Inject
	private UserStatistics userStat; 


	/**
	 * POST  /rest/devices -> Create a new device.
	 */
	@RequestMapping(value = "/rest/devices",
			method = RequestMethod.POST,
			produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public void create(@RequestBody Device device) {
		log.debug("REST request to save Device : {}", device);
		deviceRepository.save(device);
	}

	/**
	 * GET  /rest/devices -> get all the devices.
	 */
	@RequestMapping(value = "/rest/devices",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public List<Device> getAll() {
		log.debug("REST request to get all Devices");
		return deviceRepository.findAll();
	}

	/**
	 * GET  /rest/devices -> get all the devices.
	 */
	@RequestMapping(value = "/rest/requestdevices/{requestId}",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public List<Device> getAllRequestDevices(@PathVariable BigInteger requestId) {
		log.debug("REST request to get all Devices");
		return deviceRepository.getDevicesByRequestId(requestId);
	}

	/**
	 * GET  /rest/devices/:id -> get the "id" device.
	 */
	@RequestMapping(value = "/rest/devices/{id}",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<Device> get(@PathVariable Long id, HttpServletResponse response) {
		log.debug("REST request to get Device : {}", id);
		Device device = deviceRepository.findOne(id);
		if (device == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(device, HttpStatus.OK);
	}

	/**
	 * START Emulator  /rest/devices/:id -> emulator with the "id".
	 */
	@RequestMapping(value = "/rest/start/device/{id}",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public String start(@PathVariable String id) throws JSONException {
		log.info("REST request to get Start Emulator : {}", id);
		//        Device device = deviceRepository.findOne(id);
		//        if (device == null) {
		//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		//        }else{
		//        	
		//        }
		//        return new ResponseEntity<>(device, HttpStatus.OK);

		String result =null;
		try {
			result = startEmulator.start(id);
			
			
		} catch (MalformedURLException e) {

			e.printStackTrace();
		} catch (ProtocolException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		} catch (com.amazonaws.util.json.JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;

	}

	
	@RequestMapping(value = "/rest/userStatistics/{id}",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public String getUserStatistics(@PathVariable String id) throws JSONException {
		log.info("REST request to get user statistics : {}", id);
		//        Device device = deviceRepository.findOne(id);
		//        if (device == null) {
		//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		//        }else{
		//        	
		//        }
		//        return new ResponseEntity<>(device, HttpStatus.OK);
		JSONObject result = userStat.userData(id);
		
		return result.toString();
		
	}

	
	
	

	/**
	 * DELETE  /rest/devices/:id -> delete the "id" device.
	 */
	@RequestMapping(value = "/rest/devices/{id}",
			method = RequestMethod.DELETE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public void delete(@PathVariable Long id) {
		log.debug("REST request to delete Device : {}", id);
		deviceRepository.delete(id);
	}
	
	/**
	 * Return the total price of all the android api instances
	 * Returnall  /rest/getpriceAll
	 * @throws JSONException 
	 */
	@RequestMapping(value = "/rest/getpriceAll",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public String getpriceAll() throws JSONException{
		log.info("REST request to get all the network statistics of API Instances: ");
		
		String result = netAdminServ.getSumofRequestPrice();
		return result;
		
	}
	
}
