package com.spymaze.levelbuilder.utility;

public class Utility {
	
	public static String encryptString(String text, char key) {
		String enc = "";
		for (int j = 0; j < text.toCharArray().length; j++) {
			char current = text.charAt(j);
			enc += (current ^= key);
		}
		return enc;
	}
	
	public static int nextEven(float f) {
		return (f > (int) f) ? (int) (f + (2 - f%2)) : (int) f;
	}
	
	public static String parse(String s, String e, String mixed) {
		try {
			return mixed.substring(mixed.indexOf(s) + s.length(), mixed.indexOf(e, mixed.indexOf(s) + s.length()));
		} catch(Exception ee) {
			ee.printStackTrace();
			return null;
		}
	}

}
