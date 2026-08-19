package com.itsilesia.si_learn.helpers.cursors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.itsilesia.si_learn.exceptions.InvalidBase64CursorException;

import java.util.Base64;

public class CursorUtil {

	private static final ObjectMapper mapper = new ObjectMapper()
		.registerModule(new JavaTimeModule());

	public static <TClass> String encode(TClass cursor) {
		try {
			return Base64.getUrlEncoder()
				.encodeToString(mapper.writeValueAsBytes(cursor));
		} catch (Exception e) {
			throw new InvalidBase64CursorException(cursor);
		}
	}

	public static <TClass> TClass decode(Class<TClass> type, String cursor) {
		try {
			byte[] decoded = Base64.getUrlDecoder().decode(cursor);
			return mapper.readValue(decoded, type);
		} catch (Exception e) {
			throw new InvalidBase64CursorException(cursor);
		}
	}
}
