package com.rlj.passutil;

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
        return false;
    }


}
