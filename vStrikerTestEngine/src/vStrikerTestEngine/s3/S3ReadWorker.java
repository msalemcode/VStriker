package vStrikerTestEngine.s3;

import java.util.concurrent.Callable;

import org.apache.commons.io.FilenameUtils;

import vStrikerEntities.Api;
import vStrikerEntities.ExecutionReportData;

import com.emc.vipr.s3.s3api;

//@author Sanjeev Chauhan

public class S3ReadWorker implements Callable<ExecutionReportData> {

	private String objectLocation;
	private Api api;
	ExecutionReportData reportData = new ExecutionReportData();

	/*
	 * Constructor
	 */
	public S3ReadWorker() {
	}

	public S3ReadWorker(String objectLocation, Api api) {
		this.objectLocation = objectLocation;
		this.api = api;
	}

	@Override
	public ExecutionReportData call() throws Exception {
		// Read object from S3
		reportData.setDataKey("S3");
		reportData.setCrudValue("Read");
		try{
		System.out.println("Process S3 read "+FilenameUtils.getName(objectLocation));
		long startTime = System.nanoTime();
		s3api.ReadObject(api.getSubtenant(), api.getSecretKey(), api.getUrl(),
				null, api.getBucket(), FilenameUtils.getName(objectLocation));
		long endTime = System.nanoTime();
		reportData.setThreadValue(Thread.currentThread().getName());
		reportData.setDataValue(Long.toString((endTime - startTime) / 1000000));
	} catch (Exception e) {
		reportData.setDataValue(e.getMessage());
	}
		return reportData;
	}
}
