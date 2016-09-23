package com.mosimann.pdftoolset.task.image;

import javax.swing.text.StyleContext.SmallAttributeSet;

import com.mosimann.pdftoolset.JobContext;
import com.mosimann.pdftoolset.Page;

public class CalculateImageStandardDeviation {

	public void calculateImageStandardDeviation(JobContext jobContext) {
		for(Page page : jobContext.getArrayListPages()) {
			double sumFrequency = 0;
			double sumVarianceWeighted = 0;
			
			
			for (long [] histogram :  page.getHistogram().values()){
				for(int i = 0; i < histogram.length; i++) {
					sumFrequency += histogram[i];
					
					if(histogram[i]!=0){
						double mean =  page.getImageMean().get("gray");
//						System.out.println("Histogram I: " + histogram[i]);
//						System.out.println("Mean stdv: " + mean);
//						System.out.println("I: " + i);
						double subMean = i - mean ;
//						System.out.println("Subtraktion Mean: " + subMean);
						double variance = subMean*=subMean;
//						System.out.println("Varianz: " + variance);
						double varianceWeighted = variance * histogram[i];
//						System.out.println("Varianz gewichtet:" + varianceWeighted);
						
						sumVarianceWeighted += varianceWeighted;
					}
				}
			}
			
//			System.out.println("Summe Varianz gewichtet: " + sumVarianceWeighted);
//			System.out.println("Summe Frequenz: "+ sumFrequency);
			double standardDeviation = Math.sqrt(sumVarianceWeighted/(sumFrequency-1));
			page.addImageStandardDeviation("gray", standardDeviation);
			System.out.println("Standard Deviation: " + standardDeviation);
			
		}
	}
	
	
}

