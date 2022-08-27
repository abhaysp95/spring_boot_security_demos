package com.rlj.passutil;

import java.util.logging.Logger;

import org.bouncycastle.crypto.generators.Argon2BytesGenerator;
import org.bouncycastle.crypto.params.Argon2Parameters;
import org.springframework.security.crypto.keygen.BytesKeyGenerator;
import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.security.crypto.password.PasswordEncoder;

public class Argon2PassEncoder implements PasswordEncoder
{
	private static final int DEFAULT_SALT_LENGTH = 16;
	private static final int DEFAULT_HASH_LENGTH = 32;
	private static final int DEFAULT_PARALLELISM = 1;
	private static final int DEFAULT_MEMORY = 1 << 12;  // ~4Mb
	private static final int DEFAULT_ITERATIONS = 3;

	private final Logger logger = Logger.getLogger(Argon2PassEncoder.class.getName());

	private int saltLen;
	private int hashLen;
	private int parallelism;
	private int memory;
	private int iterations;

	private BytesKeyGenerator saltGenerator;


    public Argon2PassEncoder(int saltLen, int hashLen, int parallelism, int memory, int iterations)
	{
        this.saltLen = saltLen;
        this.hashLen = hashLen;
        this.parallelism = parallelism;
        this.memory = memory;
        this.iterations = iterations;

		// uses SecureRandom to generate keys of 8bytes in len (or provided custom len)
		this.saltGenerator = KeyGenerators.secureRandom(this.saltLen);
    }

	public Argon2PassEncoder()
	{
		this(DEFAULT_SALT_LENGTH, DEFAULT_HASH_LENGTH,
				DEFAULT_PARALLELISM, DEFAULT_MEMORY, DEFAULT_ITERATIONS);
	}

    @Override
    public String encode(CharSequence rawPassword) {
        // TODO Auto-generated method stub
		byte[] salt = this.saltGenerator.generateKey();
		byte[] hash = new byte[this.hashLen];

		Argon2Parameters params = new Argon2Parameters.Builder(Argon2Parameters.ARGON2_i)
			.withSalt(salt)
			.withParallelism(this.parallelism)
			.withMemoryAsKB(this.memory)
			.withIterations(this.iterations)
			.build();

		Argon2BytesGenerator generator = new Argon2BytesGenerator();
		generator.init(params);
		generator.generateBytes(rawPassword.toString().toCharArray(), hash);

        return EncodingUtil.encode(hash, params);
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        // TODO Auto-generated method stub
		if (null == rawPassword || null == encodedPassword) {
			this.logger.warning("Either rawPassword or encodedPassword is null");
			return false;
		}

		EncodingUtil.Hash hash;
		try {
			hash = EncodingUtil.decode(encodedPassword);
		} catch (IllegalArgumentException ex) {
			this.logger.warning(String.format("Error in encoded password: %s", ex.getMessage()));
			return false;
		}

		byte[] hashedPass = new byte[hash.getHash().length];
		Argon2BytesGenerator generator = new Argon2BytesGenerator();
		generator.init(hash.getParams());
		generator.generateBytes(rawPassword.toString().toCharArray(), hashedPass);

        return arrayEquals(hashedPass, hash.getHash());
    }

	private static boolean arrayEquals(byte[] arr1, byte[] arr2) {
		if (arr1.length != arr2.length)
			return false;

		int res = 0;
		for (int i = 0; i < arr1.length; i++) {
			res |= (arr1[i] ^ arr2[i]);  // if both elements in same index are same, xor will always be 0
		}

		return res == 0;
	}

    @Override
    public boolean upgradeEncoding(String encodedPassword) {
        // TODO Auto-generated method stub

		if (null == encodedPassword || 0 == encodedPassword.length()) {
			this.logger.warning("Incorrect encoded password");
			return false;
		}

		Argon2Parameters params = EncodingUtil.decode(encodedPassword).getParams();
		return (params.getMemory() > this.memory || params.getIterations() > this.iterations);
    }

}
