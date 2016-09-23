package com.mosimann.pdftoolset.task.pdf;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.transform.TransformerException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentCatalog;
import org.apache.pdfbox.pdmodel.common.PDMetadata;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDTrueTypeFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.graphics.color.PDOutputIntent;
import org.apache.xmpbox.XMPMetadata;
import org.apache.xmpbox.schema.PDFAIdentificationSchema;
import org.apache.xmpbox.xml.XmpSerializer;

import com.mosimann.pdftoolset.JobContext;
import com.mosimann.pdftoolset.Start;

public class PdfToPdfa {
	
	
	public void pdfToPdfa(JobContext jobContext, ArrayList <File> mergedPDFFiles){
		int a = 0;
		for(File file : mergedPDFFiles) {
			try {
				a++;
				
				
				PDDocument document = PDDocument.load(file);
				
				// load the font from pdfbox.jar
	            InputStream fontStream = Start.class.getResourceAsStream("/org/apache/pdfbox/resources/ttf/LiberationSans-Regular.ttf");
//	            PDFont font = PDTrueTypeFont.loadTTF(document, fontStream);
	            PDType0Font.load(document, fontStream);


	            
	            PDDocumentCatalog cat = document.getDocumentCatalog();
	            PDMetadata metadata = new PDMetadata(document);
	            cat.setMetadata(metadata);
	            
	            // jempbox version
	            XMPMetadata xmp = XMPMetadata.createXMPMetadata();
	            PDFAIdentificationSchema pdfaid = xmp.createAndAddPFAIdentificationSchema();
	            pdfaid.setConformance("B");
	            pdfaid.setPart(1);
	            pdfaid.setAboutAsSimple("PDFBox PDFA sample");
	            XmpSerializer serializer = new XmpSerializer();
	            ByteArrayOutputStream baos = new ByteArrayOutputStream();
	            serializer.serialize(xmp, baos, true);
	            metadata.importXMPMetadata( baos.toByteArray() );
	            
	            InputStream colorProfile = Start.class.getResourceAsStream("/org/apache/pdfbox/resources/icc/ISOcoated_v2_300_bas.icc");
	            //InputStream colorProfile = new FileInputStream(new File("E:/dev/eclipse-dev-java/PDFToolSet/icmprofile/sRGB Color Space Profile.icm"));
	            //InputStream colorProfile = Start.class.getResourceAsStream("/org/apache/pdfbox/resources/pdfa/sRGB Color Space Profile.icm");
	            // create output intent
	            PDOutputIntent oi = new PDOutputIntent(document, colorProfile); 
	            oi.setInfo("sRGB IEC61966-2.1"); 
	            oi.setOutputCondition("sRGB IEC61966-2.1"); 
	            oi.setOutputConditionIdentifier("sRGB IEC61966-2.1"); 
	            oi.setRegistryName("http://www.color.org"); 
	            cat.addOutputIntent(oi);
	            
	            
	            document.save(jobContext.getOutputDirectory()+"PDFA_converted" + a + ".pdf");
	            document.close();
	            
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (TransformerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}

}
