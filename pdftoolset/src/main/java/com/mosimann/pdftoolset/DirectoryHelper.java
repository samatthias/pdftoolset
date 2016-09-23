package com.mosimann.pdftoolset;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class DirectoryHelper{
	
	private static DirectoryHelper instanceDirectoryHelper;
	
	private DirectoryHelper(){}
	
	public static DirectoryHelper getInstance(){
		 if(instanceDirectoryHelper == null) {
			 instanceDirectoryHelper = new DirectoryHelper();
	      }
	      return instanceDirectoryHelper;
	}
	
	
	public void chkDirExistAndCreate(Path path){
		// Check if paths exists
		if (!Files.exists(path)) {
			try {
				Files.createDirectories(path);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
 
	}
	
	
	
    public List<Path> listDir(Path path) {
    	
    	
//		List <File> fileList = Arrays.asList(listOfFiles);
//		Collections.sort(fileList, new NaturalOrderComparator());
    	
        List<Path> fileList = new ArrayList<>();
        
        int i = 0;
        
        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(path)) {
            for (Path pathIterator : directoryStream) {
                fileList.add(pathIterator);
            }
        } catch (IOException ex) {}
        
        return fileList;

    }
	
	
//	public void chkDirExist(){
//		
//		
//		return 
//	}
	

	

}
