package com.rlj.passutil;

import java.util.Base64;

import org.bouncycastle.crypto.params.Argon2Parameters;

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

		return null;
	}
}
