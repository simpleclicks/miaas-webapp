package com.sjsu.miaas.util;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import org.joda.time.LocalDate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.sjsu.miaas.domain.Request;
import com.sjsu.miaas.repository.RequestRepository;
import com.sjsu.miaas.service.ProcessRequestService;

@EnableScheduling
@Component
public class ProcessRequestUtil {

	@Inject
	private ProcessRequestService requestService;

	@Inject
	private RequestRepository requestRepository;

	@Scheduled(fixedRate = 15000)
	public void startRequestStartProcessor() {
		try {
			while (true) {
				List<Request> reqs = requestRepository.findAll();
				for (Request request : reqs) {
					if (LocalDate.now().isAfter(request.getRequestStartDate())
							|| LocalDate.now().isEqual(
									request.getRequestStartDate())) {
						requestService.processRequest(request);
						System.out.println("Processing request "
								+ request.toString());
					}
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void startRequestEndProcessor() {

	}

}
