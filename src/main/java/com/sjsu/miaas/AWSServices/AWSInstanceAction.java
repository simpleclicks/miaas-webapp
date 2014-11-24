package com.sjsu.miaas.AWSServices;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.cloudwatch.AmazonCloudWatchClient;
import com.amazonaws.services.cloudwatch.model.Datapoint;
import com.amazonaws.services.cloudwatch.model.Dimension;
import com.amazonaws.services.cloudwatch.model.GetMetricStatisticsRequest;
import com.amazonaws.services.cloudwatch.model.GetMetricStatisticsResult;
import com.amazonaws.services.ec2.model.AllocateAddressRequest;
import com.amazonaws.services.ec2.model.AllocateAddressResult;
import com.amazonaws.services.ec2.model.AssociateAddressRequest;
import com.amazonaws.services.ec2.model.AssociateAddressResult;
import com.amazonaws.services.ec2.model.DescribeInstancesRequest;
import com.amazonaws.services.ec2.model.DescribeInstancesResult;
import com.amazonaws.services.ec2.model.DomainType;
import com.amazonaws.services.ec2.model.Instance;
import com.amazonaws.services.ec2.model.InstanceState;
import com.amazonaws.services.ec2.model.Reservation;
import com.amazonaws.services.ec2.model.RunInstancesRequest;
import com.amazonaws.services.ec2.model.RunInstancesResult;
import com.amazonaws.services.ec2.model.StartInstancesRequest;
import com.amazonaws.services.ec2.model.StopInstancesRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.sjsu.miaas.domain.AmazonInstance;
import com.sjsu.miaas.repository.AmazonInstanceRepository;

public class AWSInstanceAction extends AWSInstanceState {

	private final static Logger log = Logger.getLogger(AWSInstanceAction.class
			.getName());

	AmazonInstance ama = new AmazonInstance();

	@Inject
	AmazonInstanceRepository amazonInstRepo;

	public AWSInstanceAction() {
		super();

	}

	public boolean startInstance(ArrayList<Instance> instanceId)
			throws Exception {
		boolean bRet = false;
		// Code to start instance
		ArrayList<String> idr = new ArrayList<String>();
		int i;

		for (i = 0; i < instanceId.size(); i++) {
			if (instanceId.get(i).getState().toString().contains("stopped")) {
				idr.add(instanceId.get(i).getInstanceId());
			}
		}
		if (idr.size() >= 1) {
			StartInstancesRequest startir = new StartInstancesRequest();
			startir.setInstanceIds(idr);
			amazonEC2.startInstances(startir);
			bRet = true;

		}

		return bRet;
	}

	public boolean stopInstance(String instanceId) throws Exception {

		boolean bRet = false;
		// Code to stop instance

		ArrayList<String> idr = new ArrayList<String>();
		AmazonInstance awl = new AmazonInstance();

		awl = amazonInstRepo.getAmazonInstancebyId(instanceId);

		int i;
		idr.add(instanceId);
		/*
		 * for (i = 0; i < instanceId.size(); i++) { if
		 * (instanceId.get(i).getState().toString().contains("running")) {
		 * idr.add(instanceId.get(i).getInstanceId());
		 * 
		 * } }
		 */
		if (idr.size() >= 1) {
			StopInstancesRequest startir = new StopInstancesRequest();
			startir.setInstanceIds(idr);
			// amazonEC2.stopInstances(startir);
			awl.setInstanceStatus("stopped");
			amazonInstRepo.save(awl);
			bRet = true;

		}

		return bRet;
	}

	public AmazonInstance CreateInstance() throws InterruptedException {
		Instance aws = new Instance();
		RunInstancesRequest rir = new RunInstancesRequest()
		.withInstanceType("g2.2xlarge").withKeyName("webserver1")
		.withImageId("ami-996633a9").withMinCount(1).withMaxCount(1)
		.withSecurityGroupIds("sg-e7bad882");

		RunInstancesResult run = amazonEC2.runInstances(rir);
		String newInstance = run.getReservation().getInstances().get(0)
				.getInstanceId();
		System.out.println(newInstance);
		InstanceState is = run.getReservation().getInstances().get(0)
				.getState();
		System.out.println(is.toString());
		
		Instance newInst = null;
		DescribeInstancesRequest req = new DescribeInstancesRequest();
		while (!is.toString().contains("running")) {
			DescribeInstancesResult dir = amazonEC2.describeInstances();
			List<Reservation> reservations = dir.getReservations();
			Set<Instance> instances = new HashSet<Instance>();
			for (Reservation reservation : reservations) {
				instances.addAll(reservation.getInstances());

			}
			ArrayList<Instance> idr = new ArrayList<Instance>();

			for (Instance ins : instances) {
				if (ins.getInstanceId().contains(newInstance)) {
					newInst = ins;
					break;
				}
			}
			is = newInst.getState();
			System.out.println("Entered:");
			Thread.sleep(15000);
			//is = newInst.getState();
			System.out.println(is.toString());
		}

		AmazonInstance newDbObj = new AmazonInstance();
		newDbObj.setInstanceId(newInst.getInstanceId());
		newDbObj.setInstanceImageId(newInst.getImageId());
		newDbObj.setInstanceRegion(newInst.getPlacement().getAvailabilityZone());
		newDbObj.setInstanceStatus(newInst.getState().getName());
		newDbObj.setInstanceType(newInst.getInstanceType());
		newDbObj.setAvailableResources(new BigDecimal(20));

		newDbObj.setPublicDnsName(newInst.getPublicDnsName());
		return newDbObj;
	}

	public List<Datapoint> monitorInstance(String instanceId) {
		try {
			AWSInstanceState awi = new AWSInstanceState();
			BasicAWSCredentials bas = awi.getCredentials();
			AmazonCloudWatchClient cw = new AmazonCloudWatchClient(bas);
			cw.setRegion(Region.getRegion(Regions.US_WEST_2));
			long offsetInMilliseconds = 1000 * 60 * 60 * 24;
			GetMetricStatisticsRequest request = new GetMetricStatisticsRequest()
			.withStartTime(
					new Date(new Date().getTime()
							- offsetInMilliseconds))
							.withNamespace("AWS/EC2")
							.withPeriod(60 * 60)
							.withDimensions(
									new Dimension().withName("InstanceId").withValue(
											instanceId))
											.withMetricName("CPUUtilization")
											.withStatistics("Average", "Maximum")
											.withEndTime(new Date());
			GetMetricStatisticsResult getMetricStatisticsResult = cw
					.getMetricStatistics(request);
			System.out.println(getMetricStatisticsResult.toString());
			double avgCPUUtilization = 0;
			List<Datapoint> dataPoint = new ArrayList<Datapoint>();
			dataPoint = getMetricStatisticsResult.getDatapoints();
			List<Double> avgcpulist = new ArrayList<Double>();

			return dataPoint;

		} catch (AmazonServiceException ase) {
			log.severe("Caught an AmazonServiceException, which means the request was made  "
					+ "to Amazon EC2, but was rejected with an error response for some reason.");
			log.severe("Error Message:    " + ase.getMessage());
			log.severe("HTTP Status Code: " + ase.getStatusCode());
			log.severe("AWS Error Code:   " + ase.getErrorCode());
			log.severe("Error Type:       " + ase.getErrorType());
			log.severe("Request ID:       " + ase.getRequestId());

		}
		return null;

	}

	public ArrayList<AWSMetric> getallmonitoring() {
		AWSInstanceState awi = new AWSInstanceState();
		BasicAWSCredentials bas = awi.getCredentials();
		AmazonCloudWatchClient cw = new AmazonCloudWatchClient(bas);
		cw.setRegion(Region.getRegion(Regions.US_WEST_2));
		long offsetInMilliseconds = 1000 * 60 * 60 * 24;

		DescribeInstancesResult dir = amazonEC2.describeInstances();

		List<Reservation> reservations = dir.getReservations();

		Set<Instance> instances = new HashSet<Instance>();

		for (Reservation reservation : reservations) {
			instances.addAll(reservation.getInstances());
		}

		ArrayList<AWSMetric> awsMetric = new ArrayList<AWSMetric>();

		AWSMetric awm1 = new AWSMetric();
		
		ArrayList<String> inst = new ArrayList<String>();

		for (Instance ins : instances) 
		{
			inst.add(ins.getInstanceId());
		}

		
		System.out.println("i have the size to be the size is: "
				+ awsMetric.size());
		int size = awsMetric.size();
		for (int j = 0; j < inst.size(); j++) {
			awm1 = new AWSMetric();
			awm1.instanceID = inst.get(j);
			String InstanceID = inst.get(j);
			GetMetricStatisticsRequest request = new GetMetricStatisticsRequest()
			.withStartTime(
					new Date(new Date().getTime()
							- offsetInMilliseconds))
							.withNamespace("AWS/EC2")
							.withPeriod(60 * 60)
							.withDimensions(
									new Dimension().withName("InstanceId").withValue(
											InstanceID))
											.withMetricName("CPUUtilization").withStatistics("Average")
											.withEndTime(new Date());
			GetMetricStatisticsResult getMetricStatisticsResult = cw
					.getMetricStatistics(request);
			// double avgCPUUtilization = 0;
			List<Datapoint> dataPoint = new ArrayList<Datapoint>();
			dataPoint = getMetricStatisticsResult.getDatapoints();
			List<Double> avgcpulist = new ArrayList<Double>();
			for (Object aDataPoint : dataPoint) {
				Datapoint dp = (Datapoint) aDataPoint;
				awm1.CPUUtilization = dp.getAverage();
				System.out
				.println("what is the value here :" + dp.getAverage());
				System.out.println("here" + awm1.CPUUtilization);
				// avgCPUUtilization = dp.getAverage();
				// avgcpulist.add(avgCPUUtilization);

				System.out.println(InstanceID
						+ " instance's average CPU utilization : "
						+ dp.getAverage());
			}
			GetMetricStatisticsRequest request1 = new GetMetricStatisticsRequest()
			.withStartTime(
					new Date(new Date().getTime()
							- offsetInMilliseconds))
							.withNamespace("AWS/EC2")
							.withPeriod(60 * 60 * 60)
							.withDimensions(
									new Dimension().withName("InstanceId").withValue(
											InstanceID))
											.withMetricName("DiskWriteBytes").withStatistics("Average")
											.withEndTime(new Date());
			GetMetricStatisticsResult getMetricStatisticsResult1 = cw
					.getMetricStatistics(request1);
			// double avgCPUUtilization = 0;
			List<Datapoint> dataPoint1 = new ArrayList<Datapoint>();
			dataPoint1 = getMetricStatisticsResult1.getDatapoints();
			System.out.println("whats here: " +dataPoint1.toString());
			for (Object aDataPoint1 : dataPoint1) {
				Datapoint dp1 = (Datapoint) aDataPoint1;
						double db1 = dp1.getAverage();	
						System.out.println("value" +db1);
				awm1.DiskWriteBytes = db1;
				//System.out.println("The disk write byte is: " +awm1.DiskWriteBytes);

			}
			//
			GetMetricStatisticsRequest request2 = new GetMetricStatisticsRequest()
			.withStartTime(
					new Date(new Date().getTime()
							- offsetInMilliseconds))
							.withNamespace("AWS/EC2")
							.withPeriod(60 * 60 * 60)
							.withDimensions(
									new Dimension().withName("InstanceId").withValue(
											InstanceID))
											.withMetricName("NetworkIn").withStatistics("Average")
											.withEndTime(new Date());
			GetMetricStatisticsResult getMetricStatisticsResult2 = cw
					.getMetricStatistics(request2);
			
			List<Datapoint> dataPoint2 = new ArrayList<Datapoint>();
			dataPoint2 = getMetricStatisticsResult2.getDatapoints();
			System.out.println("whats here: " +dataPoint2.toString());
			for (Object aDataPoint2 : dataPoint2) {
				Datapoint dp2 = (Datapoint) aDataPoint2;
						double db2 = dp2.getAverage();	
						System.out.println("value" +db2);
				awm1.NetworkIn = db2;
				
			}
			//
			GetMetricStatisticsRequest request3 = new GetMetricStatisticsRequest()
			.withStartTime(
					new Date(new Date().getTime()
							- offsetInMilliseconds))
							.withNamespace("AWS/EC2")
							.withPeriod(60 * 60 * 60)
							.withDimensions(
									new Dimension().withName("InstanceId").withValue(
											InstanceID))
											.withMetricName("NetworkOut").withStatistics("Average")
											.withEndTime(new Date());
			GetMetricStatisticsResult getMetricStatisticsResult3 = cw
					.getMetricStatistics(request3);
			
			List<Datapoint> dataPoint3 = new ArrayList<Datapoint>();
			dataPoint3 = getMetricStatisticsResult3.getDatapoints();
			System.out.println("whats here: " +dataPoint3.toString());
			for (Object aDataPoint3 : dataPoint3) {
				Datapoint dp3 = (Datapoint) aDataPoint3;
						double db3 = dp3.getAverage();	
						System.out.println("value" +db3);
				awm1.NetworkOut = db3;
				
			}
			
			//
			awsMetric.add(j, awm1);
			//System.out.println("The size of the list is" + awsMetric.size());
		}

		//System.out.println(awsMetric.get(0).instanceID.toString());
		Gson gson = new GsonBuilder().create();
		 String aws1 = gson.toJson(awsMetric);
		// System.out.println("The values are :" +aws1);
		JsonArray js = gson.toJsonTree(awsMetric).getAsJsonArray();

		return awsMetric;

	}

}
