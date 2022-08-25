package com.rlj.worker;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AesGcmExample
{
	public static final int GCM_TAG_LENGTH = 16;

	public byte[] encrypt(byte[] plaintext, SecretKey key, byte[] IV)
		throws Exception
	{
		// get cipher instance
		// Cipher class forms the core of Java Cryptographic Extension (JCE)
		Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");

		// SecretKeySpec class specifies a secret key in a provider-independent fashion
		// The constructor used here does not check if the given bytes indeed
		// specify a secret key of the specified algorithm. In order for those
		// checks to be performed, an algorithm-specific *key specification*
		// class, example: DESKeySpec for DES algorithm
		SecretKeySpec keySpec = new SecretKeySpec(key.getEncoded(), "AES");

		GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH * 8, IV);

		// initialize cipher for ENCRYPT_MODE
		// initializes this cipher with a key and a set of algorithm parameters
		cipher.init(Cipher.ENCRYPT_MODE, keySpec, gcmParameterSpec);

		// perform encryption
		// Encrypts or decrypts data in a single-part operation, or finishes a
		// multiple-part operation. The data is encrypted or decrypted,
		// depending on how this cipher was initialized.
		// Upon finishing, this method resets this cipher object to the state
		// it was in when previously initialized via a call to `init`
		byte[] cipherText = cipher.doFinal(plaintext);

		return cipherText;
	}

	public byte[] decrypt(byte[] cipherText, SecretKey key, byte[] IV)
		throws Exception
	{
		// get cipher instance
		Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");

		SecretKeySpec keySpec = new SecretKeySpec(key.getEncoded(), "AES");

		GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH * 8, IV);

		// initialize cipher for decrypt mode
		cipher.init(Cipher.DECRYPT_MODE, keySpec, gcmParameterSpec);

		// perform decryption
		byte[] plainText = cipher.doFinal(cipherText);

		return plainText;
	}
}
