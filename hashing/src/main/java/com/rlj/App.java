package com.rlj;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.logging.Logger;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import de.mkammerer.argon2.Argon2Helper;

public class App
{

	private static final Logger logger = Logger.getLogger(App.class.getName());

    public static void main( String[] args )
    {
		// returns argon2i, default salt len: 16 bytes and hash len: 32 bytes
		// Argon2 argon2 = Argon2Factory.create();

		// or we can provide the lengths ourselves
		Argon2 argon2 = Argon2Factory.create(32, 64);

		char[] password = "pass".toCharArray();  // argon2 takes password to hash in form of char[]

		Instant start = Instant.now();

		int iteration = 20;
		int memory = 32 * 1024;  // ~64Mb
		int parallelism = Runtime.getRuntime().availableProcessors();

		logger.info("available processors (to jvm): " + parallelism);

		try {

			String hash = argon2.hash(iteration, memory, parallelism, password);
			logger.info("hashed pass: " + hash);  // the output is already 64-bit encoded

			logger.info(String.format("hashing took %s ms",
						ChronoUnit.MILLIS.between(start, Instant.now())));

			Instant verificationStart = Instant.now();

			// you don't need to decode (base64) if you are using this method to verify password
			logger.info("password verification: " +
					(argon2.verify(hash, password) ? "true" : "false"));

			logger.info(String.format("verification took %s ms",
						ChronoUnit.MILLIS.between(verificationStart, Instant.now())));

			Instant helperStart = Instant.now();

			// in max, 1 sec (use this to find out iteration (time limit) by
			// specifying how much memory and thread you can provide)
			logger.info("iterations: " + Argon2Helper.findIterations(argon2, 1000, memory, parallelism));

			logger.info(String.format("finding iteration took %s ms",
						ChronoUnit.MILLIS.between(helperStart, Instant.now())));
		} finally {
			Instant wiperStart = Instant.now();
			argon2.wipeArray(password);
			logger.info(String.format("wiping took %s ms",
						ChronoUnit.MILLIS.between(wiperStart, Instant.now())));
		}

		logger.info(String.format("total time taken: %s ms",
					ChronoUnit.MILLIS.between(start, Instant.now())));
    }
}
