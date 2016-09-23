package com.mosimann.pdftoolset.task.image;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import com.mosimann.pdftoolset.JobContext;
import com.mosimann.pdftoolset.Page;

public class MakeImageHistogram {
		
public void makeHistogram(JobContext jobContext){
		
	for(Page page : jobContext.getArrayListPages()) {
		
        long [] redHistogram = new long[256];
        long [] greenHistogram = new long[256];
        long [] blueHistogram = new long[256];
        
        BufferedImage inputImage = null;
        	try {
        		inputImage = ImageIO.read(new File(page.getImagePathToFile()));
//        		page.setBufferedImage(inputImage);
        	} catch (IOException e) {
        	}
		
	      for (int x = 0; x < inputImage.getWidth(); x++) {
	    	  for (int y = 0; y < inputImage.getHeight(); y++) {
	              
	    		  final int clr = inputImage.getRGB(x, y);
	              final int red = (clr & 0x00ff0000) >> 16;
	              final int green = (clr & 0x0000ff00) >> 8;
	              final int blue = clr & 0x000000ff;
	              
	              redHistogram[red] += 1;
	              greenHistogram[green] += 1;
	              blueHistogram[blue]+= 1;
	    	  }
	      }
	      
	      page.addImageHistogram("red", redHistogram);
	      page.addImageHistogram("green", greenHistogram);
	      page.addImageHistogram("blue", blueHistogram);
	      
//	      for (int i=0; i<256; i++)
//	    	  System.out.println("Farbwert: " + i +" Red: " + redHistogram[i] + " Green: " + greenHistogram[i] + " Blue: " + blueHistogram[i]);
//	      
	      inputImage = null;
	    	  
	}
	
	
	      
	  

	}

}
