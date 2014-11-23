package com.sjsu.miaas.util;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

import javax.inject.Inject;

import org.springframework.stereotype.Component;

import com.sjsu.miaas.domain.Request;
import com.sjsu.miaas.repository.RequestRepository;

@Component
public class RequestPriorityUtil {
	
	@Inject
	RequestRepository requestRepo;
	
	public PriorityQueue<Request> setRequestPriority(){
		PriorityQueue<Request> response = new PriorityQueue<Request>();
		List<Request> allRequests = new ArrayList<Request>(); 
		allRequests = requestRepo.getRequestsByStatus("Inactive");
		for (Request request : allRequests) {
			response.add(request);
		}
		return response;
	}

}
