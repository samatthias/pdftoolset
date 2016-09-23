package com.mosimann.pdftoolset.task;

import java.util.ArrayList;

import com.mosimann.pdftoolset.JobContext;
import com.mosimann.pdftoolset.Page;

public class Task {
	

	private JobContext jobContext;
	
	public void setJobContext(JobContext jobContext){
		this.jobContext = jobContext;
	}
	
	public JobContext getJobContext(){
		return this.jobContext;
	}
}
