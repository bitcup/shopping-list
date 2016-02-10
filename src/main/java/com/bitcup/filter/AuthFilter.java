package com.bitcup.filter;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author bitcup
 */
@Component
public class AuthFilter extends OncePerRequestFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthFilter.class);

    @Value("${auth.filter.disabled}")
    private boolean disabled = false;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return request.getMethod().equalsIgnoreCase("OPTIONS") ||
                request.getRequestURI().equals("/api/v1.0/greeting") ||
                request.getRequestURI().equals("/login") ||
                request.getRequestURI().equals("/favicon.ico");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        if (disabled) {
            filterChain.doFilter(request, response);
        } else {
            String auth = request.getHeader("Authorization");
            LOGGER.info("'Authorization' header: " + auth);
            if (auth != null && auth.length() > 0 && auth.startsWith("FOO ")) {
                String idAndSig = auth.substring("FOO ".length());
                LOGGER.info("idAndSig: " + idAndSig);
                String[] pieces = StringUtils.split(idAndSig, ":");
                if (pieces.length == 2) {
                    String userId = pieces[0];
                    String signature = pieces[1];
                    if (userId != null && userId.length() > 0 && signature != null && signature.length() > 0) {
                        LOGGER.info("userId: " + userId);
                        LOGGER.info("signature: " + signature);
                        // lookup user's private key by
                        String privateKey = getPrivateKey(userId);
                        if (privateKey != null) {
                            LOGGER.info("private key: " + privateKey);
                            // calculate signature
                            String uri = request.getRequestURI();
                            LOGGER.info("uri: " + uri);
                            String calculatedSig = hmacSha1(uri, privateKey);
                            LOGGER.info("calculatedSig: " + calculatedSig);
                            if (calculatedSig.equals(signature)) {
                                filterChain.doFilter(request, response);
                            } else {
                                LOGGER.info("calculatedSig does not match passed signature -  request: " + request.toString());
                            }
                        }
                    }
                }
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            } else {
                LOGGER.info("unauthorized request: " + request.toString());
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            }
        }
    }

    private String getPrivateKey(String userId) {
        if (userId.equals("abc")) {
            return "1234567890";
        }
        return null;
    }

    public static String hmacSha1(String value, String key) {
        try {
            // Get an hmac_sha1 key from the raw key bytes
            byte[] keyBytes = key.getBytes();
            SecretKeySpec signingKey = new SecretKeySpec(keyBytes, "HmacSHA1");

            // Get an hmac_sha1 Mac instance and initialize with the signing key
            Mac mac = Mac.getInstance("HmacSHA1");
            mac.init(signingKey);

            // Compute the hmac on input data bytes
            byte[] rawHmac = mac.doFinal(value.getBytes());

            // Convert raw bytes to Hex
            byte[] hexBytes = new Hex().encode(rawHmac);

            //  Covert array of Hex bytes to a String
            return new String(hexBytes, "UTF-8");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
