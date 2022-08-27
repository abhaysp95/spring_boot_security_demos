package com.rlj.passutil;

import java.util.Base64;

import org.bouncycastle.crypto.params.Argon2Parameters;
import org.bouncycastle.util.Arrays;

final class EncodingUtil
{
	private static final Base64.Encoder B64ENCODER = Base64.getEncoder();
	private static final Base64.Decoder B64DECODER = Base64.getDecoder();

	static String encode(byte[] hash, Argon2Parameters params) throws IllegalArgumentException
	{
		if (null == hash) {
			throw new IllegalArgumentException("No hash provided to encode");
		}

		StringBuilder res = new StringBuilder();
		switch (params.getType()) {
			case Argon2Parameters.ARGON2_i:
				res.append("$argon2i");
				break;
			case Argon2Parameters.ARGON2_d:
				res.append("$argon2d");
				break;
			case Argon2Parameters.ARGON2_id:
				res.append("$argon2id");
				break;
			default:
				throw new IllegalArgumentException("Invalid algo type: " + params.getType());
		}

		res.append("$v=").append(params.getVersion())
			.append("$m=").append(params.getMemory()).append(",t=").append(params.getIterations())
			.append(",p=").append(params.getLanes());

		if (null != params.getSalt()) {
			res.append("$").append(B64ENCODER.encodeToString(params.getSalt()));
		}
		res.append("$").append(B64ENCODER.encodeToString(hash));

		return res.toString();
	}

	static Hash decode(String encodedHash) throws IllegalArgumentException
	{
		if (null == encodedHash) {
			throw new IllegalArgumentException("Nothing to decode");
		}

		Argon2Parameters.Builder paramsBuilder;
		String[] vals = encodedHash.split("\\$");
		if (4 >= vals.length) {
			throw new IllegalArgumentException("Correctly encoded hash is not passed");
		}

		int idx = 1;
		switch (vals[idx]) {
			case "argon2i":
				paramsBuilder = new Argon2Parameters.Builder(Argon2Parameters.ARGON2_i);
				break;
			case "argon2d":
				paramsBuilder = new Argon2Parameters.Builder(Argon2Parameters.ARGON2_d);
				break;
			case "argon2id":
				paramsBuilder = new Argon2Parameters.Builder(Argon2Parameters.ARGON2_id);
				break;
			default:
				throw new IllegalArgumentException("Invalid algorithm type" + vals[idx]);
		}

		if (vals[++idx].startsWith("v=")) {
			paramsBuilder.withVersion(Integer.parseInt(vals[idx].substring(2)));
		}

		String[] perfVals = vals[++idx].split(",");
		if (3 != perfVals.length) {
			throw new IllegalArgumentException("Invalid number of performance params:" + perfVals.length);
		}
		if (!perfVals[0].startsWith("m=")) {
			throw new IllegalArgumentException("Invalid memory param");
		}
		paramsBuilder.withMemoryAsKB(Integer.parseInt(perfVals[0].substring(2)));
		if (!perfVals[1].startsWith("t=")) {
			throw new IllegalArgumentException("Invalid iteration param");
		}
		paramsBuilder.withIterations(Integer.parseInt(perfVals[1].substring(2)));
		if (!perfVals[2].startsWith("p=")) {
			throw new IllegalArgumentException("Invalid parallelism param");
		}
		paramsBuilder.withParallelism(Integer.parseInt(perfVals[2].substring(2)));

		if (5 == vals.length) {  // no salt in encoded hash
			paramsBuilder.withSalt(null);
			return new Hash(B64DECODER.decode(vals[++idx]), paramsBuilder.build());
		}
		paramsBuilder.withSalt(B64DECODER.decode(vals[++idx]));

		return new Hash(B64DECODER.decode(vals[++idx]), paramsBuilder.build());

	}

	public static class Hash
	{
		private Argon2Parameters params;
		private byte[] hash;

		Hash(byte[] hash, Argon2Parameters params)
		{
			this.hash = Arrays.clone(hash);  // Arrays from bouncycastle
			this.params = params;
		}

        public Argon2Parameters getParams() {
            return params;
        }

        public void setParams(Argon2Parameters params) {
            this.params = params;
        }

        public byte[] getHash() {
            return Arrays.clone(this.hash);
        }

        public void setHash(byte[] hash) {
            this.hash = Arrays.clone(hash);
        }


	}
}
