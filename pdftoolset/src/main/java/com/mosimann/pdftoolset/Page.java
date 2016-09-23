package com.mosimann.pdftoolset;

import java.awt.image.BufferedImage;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

import org.apache.commons.io.FilenameUtils;

/**
 * @author mosimannmat
 *
 */
public class Page {

	private Path pdfPathToFile;
	private Path pdfFileName;
	private Path pdfFileNameWithoutExtension;
	private Path imageRoiPath;
	private Path imageRoiBWPath;
	private boolean pdfIsEmpty = false;
	private boolean pdfHasCircle = false;
	private boolean pdfIsMerged = false;
	private String imagePathToFile;
	private String imageFileName;
	private int imageHeight;
	private int imageWidth;
	private int imageTotalPixel;
	private long imageFileSize;
	private HashMap <String, Double> imageMean = new HashMap <String, Double>();
	private HashMap <String, Double> imageStandardDeviation = new HashMap <String, Double>();
	private ImageColor imageColor;
//	private BufferedImage bufferedImage;
	private HashMap <String, long[]> histogramHashMap = new HashMap <String, long[]>();
	
	private int roiWidth = 500;
	private int roiHeight = 500;
//	private BufferedImage roiBufferedImage;
	
	private int imageCircleCount;
			
	
	public void addImageStandardDeviation(String channelColor, double standardDeviation){
		this.imageStandardDeviation.put(channelColor, standardDeviation);
	}
	
	public  HashMap <String, Double> getImageStandardDeviation(){
		return this.imageStandardDeviation;
	}
	
	public void addImageMean(String channelColor, double mean){
		this.imageMean.put(channelColor, mean);
	}
	
	public HashMap <String, Double> getImageMean(){
		return this.imageMean;
	}
	
	
	public void addImageHistogram(String channelColor, long [] histogram){
		this.histogramHashMap.put(channelColor, histogram);
	}
	
	public HashMap <String, long[]> getHistogram(){
		return histogramHashMap;	
	}
	
	public Page(Path pdfPathToFile, Path pdfFileName) {
		this.pdfPathToFile = pdfPathToFile;
		this.pdfFileName = pdfFileName;
	}
	
	public Page(int imageHeight, int imageWidth,
			int imageTotalPixel, long fileSize, BufferedImage bufferedImage) {
		
		this.imageHeight = imageHeight;
		this.imageWidth = imageWidth;
		this.imageTotalPixel = imageTotalPixel;
		this.imageFileSize = fileSize;
		
	}
	
//	public void setBufferedImage(BufferedImage bufferedImage){
//		this.bufferedImage = bufferedImage;
//	}
//	
//	public BufferedImage getBufferedImage() {
//		return this.bufferedImage;
//	}

	public int getImageHeight() {
		return imageHeight;
	}
	
	public void setImageHeight(int imageHeight) {
		this.imageHeight = imageHeight;
	}
	
	public int getImageWidth() {
		return imageWidth;
	}
	
	public void setImageWidth(int imageWidth) {
		this.imageWidth = imageWidth;
	}
	
	public int getImageTotalPixel() {
		return imageTotalPixel;
	}
	
	public void setTotalPixel(int totalPixel) {
		this.imageTotalPixel = totalPixel;
	}
	
	public long getImageFileSize() {
		return imageFileSize;
	}
	
	public void setimageFileSize(int fileSize) {
		this.imageFileSize = fileSize;
	}

	public String getImagePathToFile() {
		return imagePathToFile;
	}

	public void setImagePathToFile(String imagePathToFile) {
		this.imagePathToFile = imagePathToFile;
	}

	public ImageColor getImageColor() {
		return imageColor;
	}

	public void setImageColor(ImageColor imageColor) {
		this.imageColor = imageColor;
	}

	public Path getPdfPathToFile() {
		return pdfPathToFile;
	}

	public void setPdfPathToFile(Path pdfPathToFile) {
		this.pdfPathToFile = pdfPathToFile;
	}

	public Path getPdfFileName() {
		return pdfFileName;
	}

	public void setPdfFileName(Path pdfFileName) {
		this.pdfFileName = pdfFileName;
	}

	public String getImageFileName() {
		return imageFileName;
	}

	public void setImageFileName(String imageFileName) {
		this.imageFileName = imageFileName;
	}

	public boolean isPdfIsEmpty() {
		return pdfIsEmpty;
	}

	public void setPdfIsEmpty(boolean pdfIsEmpty) {
		this.pdfIsEmpty = pdfIsEmpty;
	}

	public int getRoiWidth() {
		return roiWidth;
	}

	public void setRoiWidth(int roiWidth) {
		this.roiWidth = roiWidth;
	}


	public int getRoiHeight() {
		return roiHeight;
	}

	public void setRoiHeight(int roiHeight) {
		this.roiHeight = roiHeight;
	}

//	public BufferedImage getRoiBufferedImage() {
//		return roiBufferedImage;
//	}
//
//	public void setRoiBufferedImage(BufferedImage roiBufferedImage) {
//		this.roiBufferedImage = roiBufferedImage;
//	}

	public boolean isPdfHasCircle() {
		return pdfHasCircle;
	}

	public void setPdfHasCircle(boolean pdfHasCircle) {
		this.pdfHasCircle = pdfHasCircle;
	}

	public int getImageCircleCount() {
		return imageCircleCount;
	}

	public void setImageCircleCount(int imageCircleCount) {
		this.imageCircleCount = imageCircleCount;
	}

	public Path getPdfFileNameWithoutExtension() {
		String fileName = FilenameUtils.removeExtension(pdfFileName.getFileName().toString());
		return Paths.get(fileName);
	}
	
	public Path getImageFileNameWithoutExtension() {
		String fileName = FilenameUtils.removeExtension(imageFileName);
		return Paths.get(fileName);
	}

	public void setPdfFileNameWithoutExtension(Path pdfFileNameWithoutExtension) {
		this.pdfFileNameWithoutExtension = pdfFileNameWithoutExtension;
	}

	public Path getImageRoiPath() {
		return imageRoiPath;
	}

	public void setImageRoiPath(Path imageRoiPath) {
		this.imageRoiPath = imageRoiPath;
	}

	public Path getImageRoiBWPath() {
		return imageRoiBWPath;
	}

	public void setImageRoiBWPath(Path imageRoiBWPath) {
		this.imageRoiBWPath = imageRoiBWPath;
	}

	

}
