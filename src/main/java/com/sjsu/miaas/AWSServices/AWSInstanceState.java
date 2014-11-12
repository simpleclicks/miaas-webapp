package com.sjsu.miaas.AWSServices;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.AmazonEC2Client;
import com.amazonaws.services.ec2.model.DescribeInstancesResult;
import com.amazonaws.services.ec2.model.Instance;
import com.amazonaws.services.ec2.model.InstanceState;
import com.amazonaws.services.ec2.model.Reservation;

public class AWSInstanceState {

	public AmazonEC2 amazonEC2;

	private AWSCredentials credentials = null;
	private String PROFILE_NAME = "miaas1";

	private final static Logger LOGGER = Logger
			.getLogger(AWSInstanceState.class.getName());

	public AWSInstanceState() {
		credentials = getCredentials();
		amazonEC2 = getAmazonEC2Client(credentials);
		setAmazonEC2ClientRegion(Regions.US_WEST_2);
	}

	protected BasicAWSCredentials getCredentials() {
		// return new
		// ProfileCredentialsProvider("/miaas/src/main/java/com/sjsu/miaas/AWSServices/",PROFILE_NAME).getCredentials();
		// return new EnvironmentVariableCredentialsProvider().getCredentials();
		// return new PropertiesCredentials(
		// AWSInstanceState.class.getResourceAsStream("credentials"));
		BasicAWSCredentials awsCredentials = new BasicAWSCredentials(
				"AKIAJWCCXYK6FOWWFMQQ",
				"44n9wOw1nRP09xlGj9ZzjEp7+nBTiXNc4fm8PB9I");
		return awsCredentials;
	}

	protected AmazonEC2Client getAmazonEC2Client(AWSCredentials credential) {
		return new AmazonEC2Client(credentials);
	}

	protected void setAmazonEC2ClientRegion(Regions region) {
		amazonEC2.setRegion(Region.getRegion(region));
	}

	/*
	 * This function returns the list of all the instance ID's associated to the
	 */
	protected ArrayList<Instance> getAllIntances() {

		System.out.println("Describe Current Instances");
		DescribeInstancesResult describeInstancesRequest = amazonEC2
				.describeInstances();

		List<Reservation> reservations = describeInstancesRequest
				.getReservations();

		Set<Instance> instances = new HashSet<Instance>();
		// add all instances to a Set.
		for (Reservation reservation : reservations) {
			instances.addAll(reservation.getInstances());
		}

		ArrayList<Instance> idr = new ArrayList<Instance>();

		for (Instance ins : instances) {

			idr.add(ins);

		}
		System.out.println("The instances running are:" + idr.size());
		for (int i = 0; i < idr.size(); i++) {
			System.out.println(idr.get(i).getInstanceId());
		}
		return idr;
	}

	// This function returns the running Instance ID's
	protected ArrayList<Instance> getRunningInstances() {

		DescribeInstancesResult describeInstancesRequest = amazonEC2
				.describeInstances();

		List<Reservation> reservations = describeInstancesRequest
				.getReservations();
		Set<Instance> instances = new HashSet<Instance>();

		// add all instances to a Set.
		for (Reservation reservation : reservations) {
			instances.addAll(reservation.getInstances());
		}

		ArrayList<Instance> idr = new ArrayList<Instance>();

		for (Instance ins : instances) {
			InstanceState is = ins.getState();
			if (ins.getState().toString().contains("running"))
				idr.add(ins);
		}

		return idr;
	}

	// This function returns the stopped Instance ID's
	protected ArrayList<Instance> getStoppedInstances() {

		DescribeInstancesResult describeInstancesRequest = amazonEC2
				.describeInstances();

		List<Reservation> reservations = describeInstancesRequest
				.getReservations();

		Set<Instance> instances = new HashSet<Instance>();
		// add all instances to a Set.
		for (Reservation reservation : reservations) {
			instances.addAll(reservation.getInstances());
		}

		ArrayList<Instance> idr = new ArrayList<Instance>();

		for (Instance ins : instances) {
			// instance state
			InstanceState is = ins.getState();
			if (ins.getState().toString().contains("stopped"))
				idr.add(ins);

		}

		return idr;

	}

}
