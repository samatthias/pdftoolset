package com.mosimann.pdftoolset;

import java.util.HashMap;

public class Container {

	private static HashMap<String,String> hashMap = new HashMap();
	
	public static void setKeyValue(String key, String value){
		hashMap.put(key, value);
	}
	
	public static HashMap<String,String>  getContainter(){
		return hashMap;
	}
}
