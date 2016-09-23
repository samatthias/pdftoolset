package com.mosimann.pdftoolset;

public enum ImageColor {
	
	BlackAndWhite(1), Gray(2),  Color(3);
	
	private int numVal;
	
	ImageColor(int numVal) {
	        this.numVal = numVal;
	    }

	    public int getNumVal() {
	        return numVal;
	    }
}
