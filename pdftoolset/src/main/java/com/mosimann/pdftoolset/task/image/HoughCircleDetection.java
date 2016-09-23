package com.mosimann.pdftoolset.task.image;

/** houghCircles_.java:

This program is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 2 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

@author Hemerson Pistori (pistori@ec.ucdb.br) and Eduardo Rocha Costa
@created 18 de Mar�o de 2004

The Hough Transform implementation was based on 
Mark A. Schulze applet (http://www.markschulze.net/)

*/

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.awt.image.DataBufferByte;
import java.awt.image.IndexColorModel;
import java.awt.image.WritableRaster;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.imageio.ImageIO;

import com.mosimann.pdftoolset.JobContext;
import com.mosimann.pdftoolset.Page;



/**
*   This ImageJ plugin shows the Hough Transform Space and search for
*   circles in a binary image. The image must have been passed through
*   an edge detection module and have edges marked in white (background
*   must be in black).
*/
public class HoughCircleDetection {

   private int radiusMin;  // Find circles with radius grater or equal radiusMin
   private int radiusMax;  // Find circles with radius less or equal radiusMax
   private int radiusInc;  // Increment used to go from radiusMin to radiusMax
   private int maxCircles; // Numbers of circles to be found
   private int threshold = -1; // An alternative to maxCircles. All circles with
   // a value in the hough space greater then threshold are marked. Higher thresholds
   // results in fewer circles.
   private byte imageValues[]; // Raw image (returned by ip.getPixels())
   private double houghValues[][][]; // Hough Space Values
   private int width; // Hough Space width (depends on image width)
   private int height;  // Hough Space heigh (depends on image height)
   private int depth;  // Hough Space depth (depends on radius interval)
   public int offset; // Image Width
   public int offx;   // ROI x offset
   public int offy;   // ROI y offset
   private Point centerPoint[]; // Center Points of the Circles Found.
   private int vectorMaxSize = 500;
   private boolean useThreshold = false;
   private int lut[][][]; // LookUp Table for rsin e rcos values
   private BufferedImage bufferedImage;


//   public int setup(String arg, ImagePlus imp) {
//       if (arg.equals("about")) {
//           showAbout();
//           return DONE;
//       }
//       return DOES_8G+DOES_STACKS+SUPPORTS_MASKING;
//   }
   
   public HoughCircleDetection( 
		   					 int radiusMin, 
		   					 int radiusMax,
		   					 int radiusInc,
		   					 int maxCircles,
		   					 int threshold) {
	   this.bufferedImage = bufferedImage;
	   this.radiusMin = radiusMin;
	   this.radiusMax = radiusMax;
	   this.radiusInc = radiusInc;
	   this.maxCircles = maxCircles;
	   this.threshold = threshold;
	   
	   this.depth = ((radiusMax-radiusMin)/radiusInc)+1;
	   
	   
	   System.out.println("RadiusMin: " + this.radiusMin);
	   System.out.println("RadiusMax: " + this.radiusMax);
	   System.out.println("RadiusInc: " + this.radiusInc);
	   System.out.println("MaxCircles: "+ this.maxCircles);
	   System.out.println("Threshold: " + this.threshold);
	   

	   
   }

   public void run(JobContext jobContext) {

//       imageValues = (byte[])ip.getPixels();
//       Rectangle r = ip.getRoi();

	   int z = 0;
	   for(Page page : jobContext.getArrayListPages()) {
		   
			BufferedImage bufferedImage = null;
			try {
				bufferedImage = ImageIO.read(Files.newInputStream(page.getImageRoiBWPath()));
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		   
			System.out.println("InputImage Type: " + bufferedImage.getType());
			System.out.println("InputImage Color Space: " + bufferedImage.getColorModel().getColorSpace().getType());
			
		   
		   this.height = bufferedImage.getHeight();
		   this.width = bufferedImage.getWidth();
		   
		   this.offset = bufferedImage.getWidth();
		   this.offx = 0;
		   this.offy = 0;
	
		   
		   System.out.println("Depth: " + this.depth);
		   System.out.println("Height: " + this.height);
		   System.out.println("Width: " + this.width);
		   System.out.println("Offset X: " + this.offx);
		   System.out.println("Offset Y: " + this.offy);
		   System.out.println("Offset Allgemein: " + this.offset);

       imageValues = ((DataBufferByte) bufferedImage.getData().getDataBuffer()).getData();
       houghTransform();
       
       int lengthInputImageByteArray = imageValues.length;
       
       byte [] accumlatorByteImage = new byte[lengthInputImageByteArray];
       this.createHoughPixels(accumlatorByteImage);
       
       byte [] circlesByteImage = new byte[lengthInputImageByteArray];
       this.createHoughPixels(circlesByteImage);
       
       
       if (maxCircles > 0) {
    	   useThreshold = false;
    	   threshold = -1;
       } else {
    	   useThreshold = true;   	   
    	   if(threshold < 0) {
    		   System.out.println("Threshold must be greater than 0");
    	   }
       }
       
	    if(useThreshold)
	    	 getCenterPointsByThreshold(threshold);
	    else
	    	 getCenterPoints(maxCircles);
	    
	    drawCircles(circlesByteImage);
	    
	    
	    System.out.println("Length accumlatorByteImage: " + accumlatorByteImage.length);
	    System.out.println("Lenght circlesImage: " + circlesByteImage.length);    
	    System.out.println("Max circles found: " + this.maxCircles);
	    
//	    int z = 0;
//	    for (byte a : accumlatorByteImage){
//	    	int b = a & 0xff;
//	    	if (b>255){
//	    		System.out.println("b ist grösser als 255" + b);
//	    	}
////	    	z++;
////	    	if(z==500){
////	    		System.out.println("A: " + b);
//	    		z = 0;
////	    	}
////	    		System.out.print(" A: " + b);
//	    }
	    
//	    try {
	        // retrieve image
//	    	InputStream in = new ByteArrayInputStream(accumlatorByteImage);
//	    	BufferedImage accumlatorBufferedImageGray = new BufferedImage(width,height, BufferedImage.TYPE_BYTE_GRAY);
//			BufferedImage accumlatorBufferedImage = ImageIO.read(in);
//			ColorConvertOp op = new ColorConvertOp(
//					accumlatorBufferedImage.getColorModel().getColorSpace(),
//					accumlatorBufferedImageGray.getColorModel().getColorSpace(),null);
//			op.filter(accumlatorBufferedImage,accumlatorBufferedImageGray);
//			 
//			if(accumlatorBufferedImageGray == null)
//				System.out.println("BufferedImage Accumolator is null.");
	    	
	    	z++;
	    	
	    	BufferedImage tempAccumlatorImage = this.create(accumlatorByteImage);
	    	System.out.println("Type tempAccumlatorImage: " + tempAccumlatorImage.getType());
	    	BufferedImage tempCircleImage = this.create(circlesByteImage);
	    	System.out.println("Type tempCircleImage: " + tempCircleImage.getType());
	    	
	    	page.setImageCircleCount(maxCircles);
	    	
	    	
		    Path imageAccumlatorPath = Paths.get(jobContext.getWorkingDirectory().toString(), page.getImageFileNameWithoutExtension().toString() + "_roi_bw_hough_accumulator.jpg" );
			System.out.println(imageAccumlatorPath.toString());	 
			
			Path imageCirclePath = Paths.get(jobContext.getWorkingDirectory().toString(), page.getImageFileNameWithoutExtension().toString() + "_roi_bw_hough_circle.jpg" );
			System.out.println(imageAccumlatorPath.toString());
			
	    	
	    	 try {
	    		 ImageIO.write(tempAccumlatorImage, "jpg",  Files.newOutputStream(imageAccumlatorPath));
	    		 ImageIO.write(tempCircleImage, "jpg", Files.newOutputStream(imageCirclePath));
		
	    	 } catch (IOException e) {
	    		//	
	    			    }
			
			this.maxCircles = 0;
			
			//	in.close();
//			
//	    	InputStream inNext = new ByteArrayInputStream(circlesImage);
//			BufferedImage circleBufferedImage = ImageIO.read(inNext);
//			ImageIO.write(circleBufferedImage, "png", new File("C:/Users/mosimannmat/Documents/PdfToolSet/work/circleImage.jpg"));
//			inNext.close();
//	
			
//			
//	    } catch (IOException e) {
//	
//	    }
	    
		}
              
//       offx = r.x;
//       offy = r.y;
//       width = r.width;
//       height = r.height;
//       offset = ip.getWidth();
//
//
//       if( readParameters() ) { // Show a Dialog Window for user input of
//           // radius and maxCircles.
//
//
//           houghTransform();
//
//           // Create image View for Hough Transform.
////           ImageProcessor newip = new ByteProcessor(width, height);
//           byte[] newpixels = (byte[])newip.getPixels();
//           createHoughPixels(newpixels);
//
//           // Create image View for Marked Circles.
//           ImageProcessor circlesip = new ByteProcessor(width, height);
//           byte[] circlespixels = (byte[])circlesip.getPixels();
//
//           // Mark the center of the found circles in a new image
//           if(useThreshold)
//               getCenterPointsByThreshold(threshold);
//           else
//               getCenterPoints(maxCircles);
//           drawCircles(circlespixels);
//
//           new ImagePlus("Hough Space [r="+radiusMin+"]", newip).show(); // Shows only the hough space for the minimun radius
//           new ImagePlus(maxCircles+" Circles Found", circlesip).show();
//       }
   }

//   void showAbout() {
//       IJ.showMessage("About Circles_...",
//                      "This plugin finds n circles\n" +
//                      "using a basic HoughTransform operator\n." +
//                      "For better results apply an Edge Detector\n" +
//                      "filter and a binarizer before using this plugin\n"+
//                      "\nAuthor: Hemerson Pistori (pistori@ec.ucdb.br)"
//                     );
//   }

//   boolean readParameters() {
//
//       GenericDialog gd = new GenericDialog("Hough Parameters", IJ.getInstance());
//       gd.addNumericField("Minimum radius (in pixels) :", 10, 0);
//       gd.addNumericField("Maximum radius (in pixels)", 20, 0);
//       gd.addNumericField("Increment radius (in pixels) :", 2, 0);
//       gd.addNumericField("Number of Circles (NC): (enter 0 if using threshold)", 10, 0);
//       gd.addNumericField("Threshold: (not used if NC > 0)", 60, 0);
//
//       gd.showDialog();
//
//       if (gd.wasCanceled()) {
//           return(false);
//       }
//
//       radiusMin = (int) gd.getNextNumber();
//       radiusMax = (int) gd.getNextNumber();
//       radiusInc = (int) gd.getNextNumber();
//       depth = ((radiusMax-radiusMin)/radiusInc)+1;
//       maxCircles = (int) gd.getNextNumber();
//       threshold = (int) gd.getNextNumber();
//       if (maxCircles > 0) {
//           useThreshold = false;
//           threshold = -1;
//       } else {
//           useThreshold = true;
//           if(threshold < 0) {
//               IJ.showMessage("Threshold must be greater than 0");
//               return(false);
//           }
//       }
//       return(true);
//
//   }

   /** The parametric equation for a circle centered at (a,b) with
       radius r is:

   a = x - r*cos(theta)
   b = y - r*sin(theta)

   In order to speed calculations, we first construct a lookup
   table (lut) containing the rcos(theta) and rsin(theta) values, for
   theta varying from 0 to 2*PI with increments equal to
   1/8*r. As of now, a fixed increment is being used for all
   different radius (1/8*radiusMin). This should be corrected in
   the future.

   Return value = Number of angles for each radius
      
   */
   private int buildLookUpTable() {

       int i = 0;
       int incDen = Math.round (8F * radiusMin);  // increment denominator

       lut = new int[2][incDen][depth];

       for(int radius = radiusMin;radius <= radiusMax;radius = radius+radiusInc) {
           i = 0;
           for(int incNun = 0; incNun < incDen; incNun++) {
               double angle = (2*Math.PI * (double)incNun) / (double)incDen;
               int indexR = (radius-radiusMin)/radiusInc;
               int rcos = (int)Math.round ((double)radius * Math.cos (angle));
               int rsin = (int)Math.round ((double)radius * Math.sin (angle));
               if((i == 0) | (rcos != lut[0][i][indexR]) & (rsin != lut[1][i][indexR])) {
                   lut[0][i][indexR] = rcos;
                   lut[1][i][indexR] = rsin;
                   i++;
               }
           }
       }

       return i;
   }

   private void houghTransform () {

       int lutSize = buildLookUpTable();
       System.out.println("INT lutSize: " + lutSize);
             

       houghValues = new double[width][height][depth];

       int k = width - 1;
       int l = height - 1;

       for(int y = 1; y < l; y++) {
           for(int x = 1; x < k; x++) {
               for(int radius = radiusMin;radius <= radiusMax;radius = radius+radiusInc) {
                   if( imageValues[(x+offx)+(y+offy)*offset] != 0 )  {// Edge pixel found
                       int indexR=(radius-radiusMin)/radiusInc;
                       for(int i = 0; i < lutSize; i++) {

                           int a = x + lut[1][i][indexR]; 
                           int b = y + lut[0][i][indexR]; 
                           if((b >= 0) & (b < height) & (a >= 0) & (a < width)) {
                               houghValues[a][b][indexR] += 1;
                           }
                       }

                   }
               }
           }

       }
       
       if (lut.length==0)
    	   System.out.println("Somthing is wrong with the lookup table.");
       
       if(houghValues.length==0)
    	   System.out.println("Somthing is wrong with the hough value array.");
 

   }


   // Convert Values in Hough Space to an 8-Bit Image Space.
   private void createHoughPixels (byte houghPixels[]) {
       double d = -1D;
       for(int j = 0; j < height; j++) {
           for(int k = 0; k < width; k++)
               if(houghValues[k][j][0] > d) {
                   d = houghValues[k][j][0];
               }

       }

       for(int l = 0; l < height; l++) {
           for(int i = 0; i < width; i++) {
               houghPixels[i + l * width] = (byte) Math.round ((houghValues[i][l][0] * 255D) / d);
           }

       }
   }

	// Draw the circles found in the original image.
	public void drawCircles(byte[] circlespixels) {
		
		// Copy original input pixels into output
		// circle location display image and
		// combine with saturation at 100
		int roiaddr=0;
		for( int y = offy; y < offy+height; y++) {
			for(int x = offx; x < offx+width; x++) {
				// Copy;
				circlespixels[roiaddr] = imageValues[x+offset*y];
				// Saturate
				if(circlespixels[roiaddr] != 0 )
					circlespixels[roiaddr] = 100;
				else
					circlespixels[roiaddr] = 0;
				roiaddr++;
			}
		}
		// Copy original image to the circlespixels image.
		// Changing pixels values to 100, so that the marked
		// circles appears more clear. Must be improved in
		// the future to show the resuls in a colored image.
		//for(int i = 0; i < width*height ;++i ) {
		//if(imageValues[i] != 0 )
		//if(circlespixels[i] != 0 )
		//circlespixels[i] = 100;
		//else
		//circlespixels[i] = 0;
		//}
		if(centerPoint == null) {
			if(useThreshold)
				getCenterPointsByThreshold(threshold);
			else
			getCenterPoints(maxCircles);
		}
		byte cor = -1;
		// Redefine these so refer to ROI coordinates exclusively
		int offset = width;
		int offx=0;
		int offy=0;
		
		for(int l = 0; l < maxCircles; l++) {
			int i = centerPoint[l].x;
			int j = centerPoint[l].y;
			// Draw a gray cross marking the center of each circle.
			for( int k = -10 ; k <= 10 ; ++k ) {
				int p = (j+k+offy)*offset + (i+offx);
				if(!outOfBounds(j+k+offy,i+offx))
					circlespixels[(j+k+offy)*offset + (i+offx)] = cor;
				if(!outOfBounds(j+offy,i+k+offx))
					circlespixels[(j+offy)*offset   + (i+k+offx)] = cor;
			}
			for( int k = -2 ; k <= 2 ; ++k ) {
				if(!outOfBounds(j-2+offy,i+k+offx))
					circlespixels[(j-2+offy)*offset + (i+k+offx)] = cor;
				if(!outOfBounds(j+2+offy,i+k+offx))
					circlespixels[(j+2+offy)*offset + (i+k+offx)] = cor;
				if(!outOfBounds(j+k+offy,i-2+offx))
					circlespixels[(j+k+offy)*offset + (i-2+offx)] = cor;
				if(!outOfBounds(j+k+offy,i+2+offx))
					circlespixels[(j+k+offy)*offset + (i+2+offx)] = cor;
			}
		}
	}


   private boolean outOfBounds(int y,int x) {
       if(x >= width)
           return(true);
       if(x <= 0)
           return(true);
       if(y >= height)
           return(true);
       if(y <= 0)
           return(true);
       return(false);
   }

   public Point nthMaxCenter (int i) {
       return centerPoint[i];
   }


   /** Search for a fixed number of circles.

   @param maxCircles The number of circles that should be found.  
   */
   private void getCenterPoints (int maxCircles) {


       centerPoint = new Point[maxCircles];
       int xMax = 0;
       int yMax = 0;
       int rMax = 0;




       for(int c = 0; c < maxCircles; c++) {
           double counterMax = -1;
           for(int radius = radiusMin;radius <= radiusMax;radius = radius+radiusInc) {


               int indexR = (radius-radiusMin)/radiusInc;
               for(int y = 0; y < height; y++) {
                   for(int x = 0; x < width; x++) {
                       if(houghValues[x][y][indexR] > counterMax) {
                           counterMax = houghValues[x][y][indexR];
                           xMax = x;
                           yMax = y;
                           rMax = radius;
                       }
                   }

               }
           }

           centerPoint[c] = new Point (xMax, yMax);

           clearNeighbours(xMax,yMax,rMax);
       }
   }


   /** Search circles having values in the hough space higher than a threshold

   @param threshold The threshold used to select the higher point of Hough Space
   */
   private void getCenterPointsByThreshold (int threshold) {

       centerPoint = new Point[vectorMaxSize];
       int xMax = 0;
       int yMax = 0;
       int countCircles = 0;

       for(int radius = radiusMin;radius <= radiusMax;radius = radius+radiusInc) {
           int indexR = (radius-radiusMin)/radiusInc;
           for(int y = 0; y < height; y++) {
               for(int x = 0; x < width; x++) {



                   if(houghValues[x][y][indexR] > threshold) {


                       if(countCircles < vectorMaxSize) {


                           centerPoint[countCircles] = new Point (x, y);

                           clearNeighbours(xMax,yMax,radius);

                           ++countCircles;
                       } else
                           break;
                   }
               }
           }
       }

       maxCircles = countCircles;
   }

   /** Clear, from the Hough Space, all the counter that are near (radius/2) a previously found circle C.
       
   @param x The x coordinate of the circle C found.
   @param x The y coordinate of the circle C found.
   @param x The radius of the circle C found.
   */
   private void clearNeighbours(int x,int y, int radius) {


       // The following code just clean the points around the center of the circle found.


       double halfRadius = radius / 2.0F;
	double halfSquared = halfRadius*halfRadius;


       int y1 = (int)Math.floor ((double)y - halfRadius);
       int y2 = (int)Math.ceil ((double)y + halfRadius) + 1;
       int x1 = (int)Math.floor ((double)x - halfRadius);
       int x2 = (int)Math.ceil ((double)x + halfRadius) + 1;



       if(y1 < 0)
           y1 = 0;
       if(y2 > height)
           y2 = height;
       if(x1 < 0)
           x1 = 0;
       if(x2 > width)
           x2 = width;



       for(int r = radiusMin;r <= radiusMax;r = r+radiusInc) {
           int indexR = (r-radiusMin)/radiusInc;
           for(int i = y1; i < y2; i++) {
               for(int j = x1; j < x2; j++) {	      	     
                   if(Math.pow (j - x, 2D) + Math.pow (i - y, 2D) < halfSquared) {
                       houghValues[j][i][indexR] = 0.0D;
                   }
               }
           }
       }

   }




	public BufferedImage create(byte [] src) {
	    byte[] r = new byte[256];
	    byte[] g = new byte[256];
	    byte[] b = new byte[256];
	    for (int i = 0; i < 256; ++i) {
	        r[i] = g[i] = b[i] = (byte) (i & 0xff);
	    }
	    IndexColorModel icm = new IndexColorModel(8, 256, r, g, b);
	
	    return create(src, icm);
	}

	
	
	 public BufferedImage create(byte [] src, IndexColorModel icm) {
		  	int height=500;
		  	int width=500;
	        WritableRaster wr = icm.createCompatibleWritableRaster(width,
	                height);

	        final byte[] bitsOn = {(byte) 0x80, 0x40, 0x20, 0x10, 0x08, 0x04, 0x02, 0x01};
	        byte[] srcPixels = (byte[]) src;
	        DataBufferByte dataBuffer = (DataBufferByte) wr.getDataBuffer();
	        byte[] destPixels = dataBuffer.getData();
	        int mapSize = icm.getMapSize();
	        if (mapSize == 256) {
	            System.arraycopy(srcPixels, 0, destPixels, 0, destPixels.length);
	            return new BufferedImage(icm, wr, false, null);
	        } else if (mapSize == 2) {
	            // Double check that dest data are large enough
	            int srcWidth = width;
	            int destWidth = (width + 7) / 8;
	            int expectedDestSize = destWidth * height;
	            if (destPixels.length != expectedDestSize) {
	                throw new IllegalStateException("Internal error: wrong size of destPixels.");
	            }
	            // Single bit image, pack bits
	            for (int i = 0; i < destPixels.length; ++i) {
	                byte destByte = 0x00;
	                int offset = (i / destWidth) * srcWidth + (i % destWidth) * 8;
	                for (int j = 0; j < 8 && ((j + offset) < srcPixels.length); ++j) {
	                    if (srcPixels[j + offset] != 0) {
	                        destByte += bitsOn[j];
	                    }
	                }
	                destPixels[i] = destByte;
	            }
	            return new BufferedImage(icm, wr, false, null);
	        } else {
	            // FIX: deal with all bit packing schemes
	            throw new UnsupportedOperationException("Unable to properly decode this image (color map).\n" +
	                    "Please report this problem at http://ij-plugins.sf.net\n" +
	                    "or by sending email to 'jsacha at users.sourceforge.net'\n" +
	                    "  Map size    = " + mapSize + "\n" +
	                    "  Src pixels  = " + srcPixels.length + "\n" +
	                    "  Dest pixels = " + destPixels.length);
	        }
	    }

}
