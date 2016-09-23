package com.mosimann.pdftoolset.task.image;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.LookupOp;
import java.awt.image.ShortLookupTable;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.imageio.ImageIO;

import com.mosimann.pdftoolset.JobContext;
import com.mosimann.pdftoolset.Page;

public class ConvertImageToBW{

	public void convertImageToBW(JobContext jobContext) {
		
		System.out.println("Hello from convertImageToBW");
	
		for(Page page : jobContext.getArrayListPages()) {
		
//			BufferedImage inputImage = page.getRoiBufferedImage();
			
			
			BufferedImage inputImage = null;
			try {
				inputImage = ImageIO.read(Files.newInputStream(page.getImageRoiPath()));
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
//			BufferedImage outputBufferedImage;
//			outputBufferedImage = new BufferedImage(inputBufferedImage.getWidth(), inputBufferedImage.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
//		    Graphics2D g2d = outputBufferedImage.createGraphics();
//		    g2d.drawImage(inputBufferedImage,null, 0, 0);
//		    g2d.dispose();
		    
		    
		    BufferedImage invertedImage = new BufferedImage(inputImage.getWidth(), inputImage.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
		    for (int x = 0; x < inputImage.getWidth(); x++) {
		    	  for (int y = 0; y < inputImage.getHeight(); y++) {
		              
		    		  int clr = inputImage.getRGB(x, y);
		              int red = (clr & 0x00ff0000) >> 16;
		              int green = (clr & 0x0000ff00) >> 8;
		              int blue = clr & 0x000000ff;
		              
		              if(red > 135) {
		            	  Color black = new Color(0, 0, 0); 
		            	  invertedImage.setRGB(x, y, black.getRGB());
		              }
		              else {
		            	  Color white = new Color(255,255,255); 
		            	  invertedImage.setRGB(x, y, white.getRGB());
		              }
		              
//		              if(red == 255){
//		            	  Color black = new Color(0, 0, 0); 
//		            	  invertedImage.setRGB(x, y, black.getRGB());
//		              }
//		              else {
//		            	  Color white = new Color(255,255,255); 
//		            	  invertedImage.setRGB(x, y, white.getRGB());
//		              }
		              
//		              System.out.print(red+":"+green+":"+blue+"  ");
		      
		    	  }
//		    	  System.out.print("");
		      }
		    
		    
		    
//		    short[] invertTable = new short[256];
//			for (int i = 0; i < 256; i++) {
//				invertTable[0] = (short) (255-i);	
//				}
//
//
//	
//			int w = outputBufferedImage.getWidth();
//			int h = outputBufferedImage.getHeight();
//			BufferedImage dst = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
//
//			BufferedImageOp invertOp = new LookupOp(new ShortLookupTable(0, invertTable), null);
//			invertOp.filter(outputBufferedImage, dst);
//		    
//		    
		    
		    
		    Path imageRoiBWPath = Paths.get(jobContext.getWorkingDirectory().toString(), page.getImageFileNameWithoutExtension().toString() + "_roi_bw.jpg" );
			System.out.println(imageRoiBWPath.toString());
		    
			page.setImageRoiBWPath(imageRoiBWPath);
		    
		    
			try {
				ImageIO.write(invertedImage, "jpg", Files.newOutputStream(imageRoiBWPath));
			} catch (IOException e) {
			// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}

}
