package com.mosimann.pdftoolset.task.pdf;

import java.io.File;
import java.util.ArrayList;

import org.apache.pdfbox.io.MemoryUsageSetting;
import org.apache.pdfbox.multipdf.PDFMergerUtility;

import com.mosimann.pdftoolset.JobContext;
import com.mosimann.pdftoolset.Page;

public class PdfMergeTogether {
	
	public ArrayList <File> PdfMergeTogether(JobContext jobContext){
		
		int i = 0;
		boolean isFirstPage = true;
		
		ArrayList <ArrayList <Page>> PDFDocuments = new ArrayList();		
		ArrayList <Page> PDFDocument = new ArrayList();
		ArrayList <File> mergedPDFFiles= new ArrayList();
		
		for(Page page : jobContext.getArrayListPages()) {
			
			
			String destinationFileName = "PDF_Output_Merge_"+i;
			
			
		
			// Wenn die Seite nicht leer ist
			// Wenn es die erste Seit.e ist
			// Wenn es einen Kreis hat
			if(!page.isPdfIsEmpty() && page.isPdfHasCircle()){
				// ... dann ist es die erste Seite
				if(PDFDocument.isEmpty()) {
					System.out.println("Kreis und nicht Leer und erstes Mal.");
					PDFDocument.add(page);
					if(PDFDocument.isEmpty()) {
						System.out.println("Immer noch leer.");
					}
				}
				else {
					System.out.println("Kreis und nicht Leer und n Mal.");
					PDFDocuments.add(PDFDocument);
					PDFDocument = new ArrayList();
					PDFDocument.add(page);
				}
			}
			else {
				System.out.println("Kein Kreis und nicht Leer.");
				if(!page.isPdfIsEmpty()&&!page.isPdfHasCircle()) {
					PDFDocument.add(page);
				}
			}
			
			i++;
					
		}
		
		// Last add PDFDocList
		PDFDocuments.add(PDFDocument);
		
		int z = 0;
		for(ArrayList <Page> PDFDocList : PDFDocuments){
			try {
				System.out.println("Outer for loop");
				z++;
				PDFMergerUtility mergePdf = new PDFMergerUtility();
				for(Page page : PDFDocList){
	
					System.out.println(z + " " + page.getPdfFileName());
					System.out.println("Filenmae: " +page.getPdfPathToFile());
					
					mergePdf.addSource(page.getPdfPathToFile().toFile());
					
				}
				
				String fileName= jobContext.getOutputDirectory()+"Merged"+z + ".pdf";
				mergePdf.setDestinationFileName(fileName);
				mergePdf.mergeDocuments(MemoryUsageSetting.setupMainMemoryOnly());
		
				
				mergedPDFFiles.add(new File(fileName));
			}
	        catch(Exception e) {	
	        }  
			
		}
		
		return mergedPDFFiles;
	
	}

}
