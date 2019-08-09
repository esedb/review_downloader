package com.comments.service;

public class CustomKeyStore {	
	private static String developerkeys;

	public static String getDeveloperkeys() {
		return developerkeys;
	}

	public static void setDeveloperkeys(String developerkeys) {
		CustomKeyStore.developerkeys = developerkeys;
	}

}
