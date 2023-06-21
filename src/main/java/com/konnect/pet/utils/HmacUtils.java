package com.konnect.pet.utils;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.TreeMap;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.lang3.StringUtils;

import com.konnect.pet.ex.CustomResponseException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HmacUtils {
	public static String createHmac(String query_string, String key) throws Exception {
		String made_hamc = "";
		String secretKey = key;

		try {
			Mac mac = Mac.getInstance("HmacSHA256");
			mac.init(new SecretKeySpec(secretKey.getBytes(), "HmacSHA256"));
			mac.update(query_string.getBytes("UTF-8"));
			made_hamc = org.apache.tomcat.util.codec.binary.Base64.encodeBase64String(mac.doFinal());

			return made_hamc;
		} catch (UnsupportedEncodingException | NoSuchAlgorithmException | InvalidKeyException e) {
			log.error("{}", e.getMessage());
			throw new Exception();
		}
	}

	/**
	 * hmac 검증 : 메시지의 무결성과 인증을 위해 메시지를 해싱하여 hmac값과 다를시 요청 무료화 처리(필수)
	 * 
	 * @param query_string
	 * @param hmac_cafe
	 */
	public static boolean validationCheckHmac(String query_string, String hmac, String key) throws Exception {
		String made_hamc = "";

		made_hamc = createHmac(query_string, key);

		if (!hmac.equals(made_hamc)) {
			return false;
		}
		
		return true;
	}

	public static String mapToQueryString(Map<String, String> paramMap) {
		Map<String, String> treeMap = new TreeMap<>(paramMap);

		StringBuilder sb = new StringBuilder();
		treeMap.forEach((key, value) -> {
			if (!StringUtils.isEmpty(key)) {
				if (sb.length() > 0) {
					sb.append("&");
				}
				sb.append(key + "=" + value);
			}
		});

		return sb.toString();
	}

	public static Map<String, String> queryToMap(String query) {
		Map<String, String> result = new TreeMap<>();
		for (String param : query.split("&")) {
			String pair[] = param.split("=");
			try {
				if (pair.length > 1) {
					result.put(pair[0], pair[1]);
				} else {
					result.put(pair[0], "");
				}
			} catch (Exception e) {
				log.error("parsing error", e.getMessage());
			}

		}
		return result;
	}
}
