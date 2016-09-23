package com.mosimann.pdftoolset;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class AnalysePage {

		public void analysePage(){
			
		}
		
		public void fillMetaInformation(String fileName){
			File file = new File("C:/Users/mosimannmat/Documents/PdfToolSet/1.jpg");
		    BufferedImage bufferedImage;
			try {
				bufferedImage = ImageIO.read(file);

		      
		      int imageHeight = bufferedImage.getHeight();
		      int imageWidth = bufferedImage.getWidth();
		      int totalPixel = imageHeight * imageWidth;
		      long fileSize = file.length();
		      
		      
		      new Page(imageHeight, imageWidth, totalPixel, fileSize, bufferedImage);
		      
	
			} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			}
		      
			
		}
}
