package com.rlj;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.logging.Logger;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import com.rlj.worker.AesGcmExample;

public class App
{
	public static final int AES_KEY_SIZE = 256;
	public static final int GCM_IV_LENGTH = 12;

	public static final Logger logger = Logger.getLogger(App.class.getName());

    public static void main( String[] args ) throws Exception
    {
		// this class provides the functionality of a secret (symmetric) key
		// generator
		// KeyGenerator objects are reusable, i.e., after a key has been
		// generated, the same KeyGenerator object can be re-used to generate
		// further keys
		KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
		keyGenerator.init(AES_KEY_SIZE);  // algorithm specific generation

		// generate key
		// A secret (symmetric) key. The purpose of this interface is to group
		// (and provide type safety for) all secret key interfaces
		SecretKey key = keyGenerator.generateKey();
		byte[] IV = new byte[GCM_IV_LENGTH];

		// this class provides a cryptographically strong random number generator (RNG)
		SecureRandom random = new SecureRandom();
		random.nextBytes(IV);

		AesGcmExample aesGcmExample = new AesGcmExample();  // object which actually performs
															// encryption and decryption


		Charset charset = StandardCharsets.UTF_8;

		try (BufferedInputStream bufferedInputStream = new BufferedInputStream(System.in);
				BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(bufferedInputStream, charset))) {

			// below commented blocks are seperated blocks which you can tryout
			// for testing (the whole 'while' loop is seperate block)

			/* String str = bufferedReader.readLine();
			logger.info("you entered: " + str);
			byte[] strByte = Base64.getDecoder().decode(str);
			logger.info("you entered (byte): " + strByte.toString());
			str = Base64.getEncoder().encodeToString(strByte);
			logger.info("you entered: " + str); */

			// to understand below commented block, visit: https://www.baeldung.com/java-string-to-byte-array
			String str = bufferedReader.readLine();
			logger.info("you entered: " + str);
			logger.info("you entered: " + str.getBytes(charset));
			// either use String constructor or,
			logger.info("you entered: " + charset.decode(ByteBuffer.wrap(str.getBytes(charset))).toString());
			String encoded = Base64.getEncoder().encodeToString(str.getBytes(charset));
			logger.info("you entered (encoded): " + encoded);
			byte[] decoded = Base64.getDecoder().decode(encoded);
			logger.info("you entered (decoded): " + decoded);
			logger.info("you entered (decoded): " + charset.decode(ByteBuffer.wrap(decoded)).toString());

			byte[] encrypted = aesGcmExample.encrypt(str.getBytes(charset), key, IV);
			logger.info("encrypted: " + encrypted);
			String encodeEncrypted = Base64.getEncoder().encodeToString(encrypted);
			logger.info("encodeEncrypted: " + encodeEncrypted);
			byte[] decodeEncrypted = Base64.getDecoder().decode(encodeEncrypted);
			logger.info("decodeEncrypted: " + encodeEncrypted);
			byte[] decrypted = aesGcmExample.decrypt(decodeEncrypted, key, IV);
			logger.info("decrypted: " + decrypted);
			logger.info("decrypted: " + charset.decode(ByteBuffer.wrap(decrypted)).toString());

			/* String str = bufferedReader.readLine();
			logger.info("you entered (str): " + str);
			byte[] cipherText = aesGcmExample.encrypt(str.getBytes(), key, IV);
			logger.info("cipher text (byte): " + cipherText.toString());
			String encodedCipherText = Base64.getEncoder().encodeToString(cipherText);
			logger.info("cipher text (base64 encoded): " + encodedCipherText);
			byte[] decodedCipherText = Base64.getDecoder().decode(encodedCipherText);
			logger.info("decoded cipher text (base64 decoded): " + decodedCipherText.toString());
			logger.info("passing | " + cipherText.toString() + " | to decrypt");
			str = aesGcmExample.decrypt(cipherText, key, IV);
			logger.info("decrypted text (cipherText): " + str);
			logger.info("decrypted text (cipherText): " + Base64.getDecoder().decode(str).toString());
			str = aesGcmExample.decrypt(decodedCipherText, key, IV);
			logger.info("decrypted text (decodedCipherText): " + str); */

			// https://stackoverflow.com/questions/26419538/how-to-read-a-line-in-bufferedinputstream,
			// checkout for reason
			// the above objects in try-with-resource can be combined in single object (for usage)

			int choice = 0;
			while (choice != -1) {
				logger.info("Press 1 to encrypt, 2 to decrypt and -1 to exit");
				choice = bufferedReader.read() - 48;
				bufferedReader.readLine();
				switch(choice) {
					case 1:
						{
							logger.info("Enter plain text");
							String plainText = bufferedReader.readLine();
							// logger.info("You entered: " + plainText);
							byte[] cipherText = aesGcmExample.encrypt(plainText.getBytes(charset), key, IV);
							// logger.info("encrypt text(byte): " + cipherText.toString());
							logger.info("encrypted text: " + Base64.getEncoder().encodeToString(cipherText));  // to store in db
						}
						break;
					case 2:
						{
							logger.info("Enter encrypted text");
							String entry = bufferedReader.readLine();
							// logger.info("You entered: " + entry);
							byte[] cipherText = Base64.getDecoder().decode(entry);
							// logger.info("ciperText (byte): " + cipherText);
							byte[] plainText = aesGcmExample.decrypt(cipherText, key, IV);
							// logger.info("decrypted text: " + plainText);
							logger.info("decrypted text (str): " + charset.decode(ByteBuffer.wrap(plainText)).toString());
						}
						break;
					default:
						choice = -1;
						logger.warning("Exiting!!!");
						break;
				}
			}

		} catch (IOException ex) {
			logger.warning(ex.getMessage());
		}
    }
}
