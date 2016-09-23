package com.mosimann.pdftoolset.task.image;

import com.mosimann.pdftoolset.JobContext;
import com.mosimann.pdftoolset.Page;

public class CalculateImageMean {

	public void calculateImageMean(JobContext jobContext) { 
		for(Page page : jobContext.getArrayListPages()) {
			
			double sumFrequency = 0;
			double sumWeightedFrequency = 0;
			
			for (long [] histogram :  page.getHistogram().values()){
				for(int i = 0; i < histogram.length; i++) {
					if(histogram[i]!=0) {
//						System.out.println("Histrogram I: "+ histogram[i]);
//						System.out.println("I: " + i);
						sumFrequency += histogram[i];
						sumWeightedFrequency += i * histogram[i];
//						System.out.println("Gewichtet Schlaufe: " + sumWeightedFrequency);
					}
				}
			}
			
//			System.out.println(255L*8697201L);
			
//			System.out.println("Summe der Pixel: " + sumFrequency);
//			System.out.printf("Summe der gewichteten Pixel: %f\n", sumWeightedFrequency);
			
			double mean = (sumWeightedFrequency / sumFrequency) ;
			System.out.println("Page mean: " +mean);
			page.addImageMean("gray", mean);
		}
	}
}
