package com.mosimann.pdftoolset;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class JobContext {
	
	private Path inputDirectory;
	private Path outputDirectory;
	private Path workingDirectory;
	private ArrayList <Page> arrayListPages = new ArrayList<Page>();
	
	
	public JobContext(Path inputDirectory, Path outputDirectory, Path workingDirectory){
		
		this.inputDirectory = inputDirectory;
		this.outputDirectory = outputDirectory;
		this.workingDirectory = workingDirectory;

	}
	
	public JobContext(){
	
	}
	
	public ArrayList <Page> getArrayListPages(){
		return this.arrayListPages;
	}
	
	public void addPage(Page page){
		this.arrayListPages.add(page);
	}

	
	public Path getWorkingDirectory() {
		return workingDirectory;
	}

	public void setWorkingDirectory(Path workingDirectory) {
		this.workingDirectory = workingDirectory;
	}

	public Path getInputDirectory() {
		return inputDirectory;
	}
	
	public void setInputDirectory(Path inputDirectory) {
		this.inputDirectory = inputDirectory;
	}
	
	public Path getOutputDirectory() {
		return outputDirectory;
	}
	
	public void setOutputDirectory(Path outputDirectory) {
		this.outputDirectory = outputDirectory;
	}
	
	
	


	
//	public void readFiles() {
//	
//		File folder = new File(inputDirectory);
//		File[] listOfFiles = folder.listFiles();
//		
//		List <File> fileList = Arrays.asList(listOfFiles);
//		Collections.sort(fileList, new NaturalOrderComparator());
//	
//		int i = 0;
//		
//		 for(File file: fileList){
//			 System.out.println(file.getName().substring(0, file.getName().length()-4));
//			 System.out.println(file.getAbsolutePath());
//			 arrayListPages.add(i, new Page(file.getAbsolutePath(),file.getName().substring(0, file.getName().length()-4)));
//			 i++;
//		  }
//	
//	}

}
