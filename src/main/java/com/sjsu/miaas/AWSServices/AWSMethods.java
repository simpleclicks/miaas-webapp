package com.sjsu.miaas.AWSServices;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.amazonaws.services.ec2.model.Instance;
import com.sjsu.miaas.domain.AmazonInstance;

@Service
@Transactional
public class AWSMethods {

	private final AWSInstanceAction awsInstanceAction;

	private final static Logger LOGGER = Logger.getLogger(AWSMethods.class.getName());

	public AWSMethods()
	{
		awsInstanceAction = new AWSInstanceAction();		
	}
	
	protected ArrayList<Instance> getAllInstances() throws Exception
	{
		return awsInstanceAction.getAllIntances();
	}

	protected ArrayList<Instance> getAllRunningInstances() throws Exception
	{
		return awsInstanceAction.getRunningInstances();
	}

	protected ArrayList<Instance> getAllStoppedInstances() throws Exception
	{
		return awsInstanceAction.getStoppedInstances();
	}
	
	protected boolean stopAWSInstance(String Instance) throws Exception {
		return awsInstanceAction.stopInstance(Instance);
	}

	protected boolean startAWSInstance(ArrayList<Instance> arrayListOfInstances) throws Exception {
		return awsInstanceAction.startInstance(arrayListOfInstances);
	}

	public void execute() throws Exception {

	//	LOGGER.log(Level.INFO, "Entered into Amazon Start Stop Methods");
		
	//	ArrayList<Instance> arrayListOfInstances = getAllInstances();
	//	LOGGER.log(Level.INFO, "List of All Instances : "+arrayListOfInstances.toString());

		//ArrayList<Instance> arrayListOfRunningInstances = getAllRunningInstances();
		//LOGGER.log(Level.INFO, "List of All Running Instances : "+arrayListOfRunningInstances.toString());

//		ArrayList<Instance> arrayListOfStoppedInstances = getAllStoppedInstances();
		//LOGGER.log(Level.INFO, "List of All Stopped Instances : "+arrayListOfStoppedInstances.toString());

		//stopAWSInstance("i-ba25f4b0");
//		LOGGER.log(Level.INFO, "Stopping running instances : "+stopAWSInstance(arrayListOfRunningInstances));

	//	AmazonInstance aws1 = awsInstanceAction.CreateInstance();
	//	System.out.println(aws1);
		
		List<Double> monitor = awsInstanceAction.monitorInstance("");
		System.out.println(monitor);
	}

	public static void main(String args[]) throws Exception {
		AWSMethods awsMethods = new AWSMethods();
		awsMethods.execute();

	}

}
