package org.siro.tools;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class SHA256 {

	public static String GetSHAString(String password)
	{
		String salt = getSalt();
		//System.out.println(salt);
		String hash = SaltAndSHA256(salt,password);
		//System.out.println(hash);
		return salt + hash;
	}
	public static Boolean JudgeString(String password, String SHAString)
	{
		String salt = SHAString.substring(0, 16);
		String hash = SaltAndSHA256( salt,password);
		return SHAString.substring(16).equals(hash);
	}

	private static String SaltAndSHA256(String salt,String password){
		return getSHA(salt+password);
	}

	private static  String getSalt(){
		Random rnd = new Random();
		StringBuilder hexTemp = new StringBuilder();
		for(int i = 0; i < 16; i++){
			hexTemp.append(Integer.toHexString(rnd.nextInt(16)));
		}
		return hexTemp.toString();
	}
	private static String getSHA(String input)
	{

		try {

			// Static getInstance method is called with hashing SHA
			MessageDigest md = MessageDigest.getInstance("SHA-256");

			// digest() method called
			// to calculate message digest of an input
			// and return array of byte
			byte[] messageDigest = md.digest(input.getBytes());

			// Convert byte array into signum representation
			BigInteger no = new BigInteger(1, messageDigest);

			// Convert message digest into hex value
			String hashtext = no.toString(16);

			while (hashtext.length() < 32) {
				hashtext = "0" + hashtext;
			}

			return hashtext;
		}

		// For specifying wrong message digest algorithms
		catch (NoSuchAlgorithmException e) {
			System.out.println("Exception thrown"
					+ " for incorrect algorithm: " + e);
			return null;
		}
	}
}
