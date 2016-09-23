package com.mosimann.pdftoolset;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.mosimann.pdftoolset.task.image.CalculateImageMean;
import com.mosimann.pdftoolset.task.image.CalculateImageStandardDeviation;
import com.mosimann.pdftoolset.task.image.ConvertImageToBW;
import com.mosimann.pdftoolset.task.image.CutImageRoi;
import com.mosimann.pdftoolset.task.image.DetectImageColor;
import com.mosimann.pdftoolset.task.image.HoughCircleDetection;
import com.mosimann.pdftoolset.task.image.MakeImageHistogram;
import com.mosimann.pdftoolset.task.pdf.PdfMergeTogether;
import com.mosimann.pdftoolset.task.pdf.PdfToImage;
import com.mosimann.pdftoolset.task.pdf.PdfToPdfa;

public class Start {
	
	public Start(){
		
		
		
//		try {
		
		Path inputDir = Paths.get("C:/Users/matmos/Documents/PdfToolSet/inbox/");
		Path outputDir =  Paths.get("C:/Users/matmos/Documents/PdfToolSet/output/");
		Path workDir =  Paths.get("C:/Users/matmos/Documents/PdfToolSet/work/");
		
		 
		DirectoryHelper dirHelper = DirectoryHelper.getInstance();
		
		dirHelper.chkDirExistAndCreate(inputDir);
		dirHelper.chkDirExistAndCreate(outputDir);
		dirHelper.chkDirExistAndCreate(workDir);

		JobContext jobContext = new JobContext();
		jobContext.setOutputDirectory(outputDir);
		jobContext.setInputDirectory(inputDir);
		jobContext.setWorkingDirectory(workDir);
		
		List<Path> fileList = dirHelper.listDir(jobContext.getInputDirectory());
		
		
		
		for (Path path : fileList) {
//            System.out.println(path.toString());
//            System.out.println(path.getFileName().toString());
//            System.out.println(FilenameUtils.removeExtension(path.getFileName().toString()));
 
            Page page = new Page(path,path.getFileName());
            jobContext.addPage(page);
            
        }
		
		System.out.println(jobContext.getArrayListPages().get(1).getPdfFileNameWithoutExtension().toString());
		
//		new Page(file.getAbsolutePath(),file.getName().substring(0, file.getName().length()-4))
		
//		jobContext.readFiles();
		
		
		PdfToImage pdfToImage = new PdfToImage();
		pdfToImage.extractImageImageIOUtil(jobContext);
		
		MakeImageHistogram makeImageHistogram = new MakeImageHistogram();
		makeImageHistogram.makeHistogram(jobContext);
		
		DetectImageColor detectImageColor = new DetectImageColor();
		detectImageColor.detectImageColor(jobContext);
		
		CalculateImageMean calculateImageMean = new CalculateImageMean();
		calculateImageMean.calculateImageMean(jobContext);
				
		CalculateImageStandardDeviation calculateImageStandardDeviation = new CalculateImageStandardDeviation();
		calculateImageStandardDeviation.calculateImageStandardDeviation(jobContext);
		
		CutImageRoi cutImageRoi = new CutImageRoi();
		cutImageRoi.cutImageRoiFromRight(jobContext);
		
		ConvertImageToBW convertImageToBlackAndWhite = new ConvertImageToBW();
		convertImageToBlackAndWhite.convertImageToBW(jobContext);
		
		HoughCircleDetection houghCircle = new HoughCircleDetection(68, 72, 1, 0, 350);
		houghCircle.run(jobContext);
		
		double thresholdStandardDeviation = 7.0;
		int thresholdCountCircles = 1;
		
		for(Page page:jobContext.getArrayListPages()){
			
			if(page.getImageStandardDeviation().get("gray") <= thresholdStandardDeviation)
				page.setPdfIsEmpty(true);
			
			if(page.getImageCircleCount() >= thresholdCountCircles)
				page.setPdfHasCircle(true);
		
		}

		PdfMergeTogether pdfMergeTogether = new PdfMergeTogether();
		ArrayList <File> mergedPDFFiles = pdfMergeTogether.PdfMergeTogether(jobContext);
		
		PdfToPdfa pdfToPdfa = new PdfToPdfa();
		pdfToPdfa.pdfToPdfa(jobContext, mergedPDFFiles);
		
		
		
		
		
		
		
		
		
		
		
		
//		try {
//			JobContext jobContextTemp = new JobContext();
//			jobContextTemp.setWorkingDirectory(jobContext.getWorkingDirectory());
//			Page page = new Page("","");
//			BufferedImage image = ImageIO.read(new File("C:/Users/mosimannmat/Documents/PdfToolSet/work/binaryHough.png"));
//
//			page.setRoiBufferedImage(image);
//			jobContextTemp.addPage(page);
//		
//			Hough_Circles houghCircle = new Hough_Circles(68, 72, 1, 1, 0);
//			houghCircle.run(jobContextTemp);
//		
//		
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		
		
		// contrast leverage
//		try {
//			BufferedImage image = ImageIO.read(new File("C:/Users/mosimannmat/Documents/PdfToolSet/work/[Untitled]_2-22.jpg"));
//			RescaleOp rescaleOp = new RescaleOp(1.01f,-15f,null);
//			image = rescaleOp.filter(image, image);
//			
//			ImageIO.write(image, "jpg", new File("C:/Users/mosimannmat/Documents/PdfToolSet/work/test.jpg"));
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
		

		
//			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
//			Date now = new Date();
//		    System.out.println(sdf.format(now));
//
//			System.out.println(sdf.format(new Date()));
		
			//Gray Filter Method
//			ImageFilter filter = new GrayFilter(true, 50);  
//			ImageProducer producer = new FilteredImageSource(newBF.getSource(), filter);  
//			Image grayImage = Toolkit.getDefaultToolkit().createImage(producer);
//			
//			BufferedImage bufGray = new BufferedImage(grayImage.getWidth(null), grayImage.getHeight(null),
//			        BufferedImage.TYPE_INT_RGB);
//
//			    Graphics g = bufGray.createGraphics();
//			    g.drawImage(grayImage, 0, 0, null);
//			    g.dispose(); 
//			    File outputfile = new File("C:/Users/mosimannmat/Documents/PdfToolSet/work/saved.jpg");
//			    ImageIO.write(bufGray, "jpg", outputfile);
//			Color c = new Color(254,254,254);
//			System.out.println("Farbe: " +c.getRGB());
			
//			BufferedImageOp opBuf = new ColorConvertOp(ColorSpace.getInstance(ColorSpace.CS_GRAY), null);
//		    
//			    File outputfile = new File("C:/Users/mosimannmat/Documents/PdfToolSet/work/saved.jpg");
//			    ImageIO.write( opBuf.filter(newBF, null), "jpg", outputfile);
			
			// access all pixels
//			byte[] pixels = ((DataBufferByte) newBF.getRaster().getDataBuffer()).getData();
//	        for (byte b : pixels) {
//	        	if(b!=0 || b!=-1)
//	        		System.out.println(b + ",");
//	        }
			
	    
	        
	        
	        
	
		
		//"C:\Users\mosimannmat\Documents\PdfToolSet\1.JPG"
		
//		 final File file = new File("C:/Users/mosimannmat/Documents/1.jpg");
//	      BufferedImage originalImage;

//			originalImage = ImageIO.read(file);
//
//	      
//	      int originalHeight = originalImage.getHeight();
//	      int originalWidth = originalImage.getWidth();
//		
//		cutRectangeFromRight(originalImage,250,250,originalWidth);
//		
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
			
		new Start();
		
//		System.out.println("Start ... ");
//		
//		// Thread Test
//		for(int i=0; i<=10; i++){
//			Runnable whitePage = new ThreadWhitePage(i);
//			
//			Thread thread = new Thread(whitePage);
//			//thread.setDaemon(true);
//			thread.setName("Name:"+i);
//			thread.start();
//		}
//		
//		try {
//			Thread.sleep(5000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		HashMap <String,String> hMap = Container.getContainter();
//		for(String key : hMap.keySet()){
//			System.out.println(key + " " +hMap.get(key));
//		}
//		
//		
//		System.out.println("End ...");
		
		
	}
	

	

	
	static {System.setProperty("com.sun.media.jai.disableMediaLib", "true");}
}

