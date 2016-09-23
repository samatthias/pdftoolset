package com.mosimann.pdftoolset.task.pdf;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.tools.imageio.ImageIOUtil;

import com.mosimann.pdftoolset.JobContext;
import com.mosimann.pdftoolset.Page;
import com.mosimann.pdftoolset.task.Task;



public class PdfToImage extends Task{
	

	public PdfToImage(){
	}
	
	public void extractImageImageIOUtil(JobContext jobContext){
		
		for(Page page : jobContext.getArrayListPages()) {
			try {
	            
	            PDDocument document = PDDocument.load(page.getPdfPathToFile().toFile());
	            PDFRenderer pdfRenderer = new PDFRenderer(document);

	          
	            
	            int counter = 0;
	            for (PDPage pdPage : document.getPages()) {
	            	
	            	
	            	//imageType - the image type (see BufferedImage.TYPE_*)
	            	//resolution - the resolution in dpi (dots per inch)
	            	//BufferedImage bufferedImage = pdPage.convertToImage(BufferedImage.TYPE_INT_RGB,300);
	            	BufferedImage bufferedImage = pdfRenderer.renderImageWithDPI(counter, 300, ImageType.RGB);
//	            	BufferedImage bf = page.convertToImage();
	            	
//	            	File outputfile = new File(jobContext.getWorkingDirectory()+ +".jpg");
//	            	String imageFilenName = "PdfToImage_" + page.getPdfFileName() +".jpg";
//	            	String imagePathToFile = jobContext.getWorkingDirectory() + imageFilenName;
	            	Path imageFileName = Paths.get(page.getPdfFileNameWithoutExtension().toString() + ".jpg");
	            	System.out.println(imageFileName.toString());
	            	Path imagePathToFile = Paths.get(jobContext.getWorkingDirectory().toString(),imageFileName.toString()); 
	            	
	            	ImageIOUtil.writeImage(bufferedImage, imagePathToFile.toString(),300);
	            	
	            	page.setImageHeight(bufferedImage.getHeight());
	            	page.setImageWidth(bufferedImage.getWidth());
	            	page.setImageFileName(imageFileName.toString());
	            	page.setImagePathToFile(imagePathToFile.toString());
	            	
	            	counter++;
	            	
	            }
	            
	     
	            document.close();
			 } catch (Exception e) {
			        e.printStackTrace();
			 }
         }   
	}
	


}
