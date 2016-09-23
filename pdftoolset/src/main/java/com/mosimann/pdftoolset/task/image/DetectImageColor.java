package com.mosimann.pdftoolset.task.image;

import java.util.Arrays;

import com.mosimann.pdftoolset.ImageColor;
import com.mosimann.pdftoolset.JobContext;
import com.mosimann.pdftoolset.Page;

public class DetectImageColor {
	
	public void detectImageColor(JobContext jobContext){
		
		for(Page page : jobContext.getArrayListPages()) {
			long [] redHistogram = page.getHistogram().get("red");
			long [] greenHistogram = page.getHistogram().get("green");
			long [] blueHistogram = page.getHistogram().get("blue");
		
		
//			System.out.println("Red equals Blue: " + Arrays.equals(redHistogram, blueHistogram));
//			System.out.println("Red equals Green: " + Arrays.equals(redHistogram, greenHistogram));
	      
//			System.out.println(redHistogram[0]+redHistogram[255]);
//			System.out.println(page.getImageHeight()*page.getImageWidth());
	      
		     if (Arrays.equals(redHistogram, blueHistogram) && Arrays.equals(redHistogram, greenHistogram)){
		    	  int checkValue = 0;
		    	  for (int i=1; i<255; i++){
		    		  checkValue += redHistogram[i];
//		    		  System.out.println(redHistogram[i]);
		    	  }
//		    	  System.out.println("SumPixel: " +checkValue);
		    	  if(checkValue==0){
//		    		  System.out.println("BlackAndWhite");
		    		  page.getHistogram().put("gray",page.getHistogram().get("red"));
		    		  page.setImageColor(ImageColor.BlackAndWhite);
		    		  page.getHistogram().remove("red");
		    		  page.getHistogram().remove("green");
		    		  page.getHistogram().remove("blue");
		    	  }
		    	  else {
//		    		  System.out.println("Gray");
		    		  page.setImageColor(ImageColor.Gray);
		    		  page.getHistogram().put("gray",page.getHistogram().get("red"));
		    		  page.getHistogram().remove("red");
		    		  page.getHistogram().remove("green");
		    		  page.getHistogram().remove("blue");
		    	  }
		    	  
		      }
		      else {
//		    	  System.out.println("Color");
		    	  page.setImageColor(ImageColor.Color);
		      }
		}
	}

}
