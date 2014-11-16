package com.sjsu.miaas.service;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sjsu.miaas.AWSServices.AWSInstanceAction;
import com.sjsu.miaas.AWSServices.AWSMetric;
import com.sjsu.miaas.domain.AmazonInstance;
import com.sjsu.miaas.repository.AmazonInstanceRepository;

@Service
@Transactional
public class AmazonInstanceService {

	@Inject
	AmazonInstanceRepository amazonRepo;

	public List<AWSMetric> getAmazonMetrics() {
		List<AWSMetric> resp = new ArrayList<AWSMetric>();
		List<AWSMetric> retresp = new ArrayList<AWSMetric>();
		AWSInstanceAction awsHelper = new AWSInstanceAction();
		resp = awsHelper.getallmonitoring();
		
		for (AWSMetric awsMetric : resp) {
			AmazonInstance awsInst = amazonRepo.getAmazonInstancebyId(awsMetric
					.getinstanceid());
			if (awsInst != null) {
				awsMetric
						.setAvailableResources(awsInst.getAvailableResources());
				retresp.add(awsMetric);
			}
		}
		return retresp;
	}

}
