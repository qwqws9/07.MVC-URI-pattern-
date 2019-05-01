package com.model2.mvc.common;

import java.io.UnsupportedEncodingException;

public class ConvertEn {
	
public static String convertKo(String value) {
		
		String convert = null;
		
		try {
			byte[] b = value.getBytes("8859_1");
			convert = new String(b,"utf-8");
		}
		catch(UnsupportedEncodingException uee) {
			uee.printStackTrace();
		}
		return convert;
	}


}
