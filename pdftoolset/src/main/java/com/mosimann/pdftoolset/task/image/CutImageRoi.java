package com.mosimann.pdftoolset.task.image;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.imageio.ImageIO;

import com.mosimann.pdftoolset.JobContext;
import com.mosimann.pdftoolset.ObjectSizeFetcher;
import com.mosimann.pdftoolset.Page;

public class CutImageRoi {
	
	
	

	public void cutImageRoiFromRight(JobContext jobContext){
		for(Page page : jobContext.getArrayListPages()) {
			BufferedImage inputBufferedImage = null;
			try {
				inputBufferedImage = ImageIO.read(new File(page.getImagePathToFile()));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (inputBufferedImage == null)
				System.out.println("Error inputImage is null.");
				
			System.out.println("Image Width: " +page.getImageWidth() );
			
			int roiWidth = page.getRoiWidth();
			int roiHeight = page.getRoiHeight();
			int roiStartX = page.getImageWidth()-page.getRoiWidth();
			int roiStartY = 0;
			
			System.out.println("Roi start x: " + roiStartX);
			System.out.println("Roi start y: " + roiStartY);
			System.out.println("Roi Width: " + roiWidth);
			System.out.println("Roi Height: " + roiHeight);

			Path imageRoiPath = Paths.get(jobContext.getWorkingDirectory().toString(), page.getImageFileNameWithoutExtension().toString() + "_roi.jpg" );
			System.out.println(imageRoiPath.toString());
			
			page.setImageRoiPath(imageRoiPath);
			
			BufferedImage roiBufferedImage = this.cutRectangle(inputBufferedImage,roiStartX,roiStartY,roiWidth,roiHeight);
			try {
				ImageIO.write(roiBufferedImage, "jpg", Files.newOutputStream(imageRoiPath));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
//			page.setRoiBufferedImage(roiBufferedImage);
			
			
			
			inputBufferedImage = null;
		
		}
	}
	
	
	/**
	 * 
	 */
	public BufferedImage cutRectangle(BufferedImage originalImage,int rectangleStartX, int rectangleStartY,int rectangleWidth,int rectangleHeight){
	      BufferedImage rectImage = originalImage.getSubimage(rectangleStartX,rectangleStartY, rectangleWidth,rectangleHeight);     
	      return rectImage;
	}
}
