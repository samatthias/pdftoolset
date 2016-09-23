package com.mosimann.pdftoolset;

import java.util.HashMap;

public class ThreadWhitePage implements Runnable{
	
	private HashMap asdf = new HashMap();
	private int name = 0;
	
	public ThreadWhitePage(int i) {
		this.name = i;
	}



	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		for(int a=0; a<=3;a++) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Container.setKeyValue("Thread:"+name+a, "foobar"+a);
		}
		
		
	}

}
