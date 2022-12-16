package com.coway.common.util;

import org.springframework.security.crypto.password.MessageDigestPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class Sha512PasswordEncoder implements PasswordEncoder {
    private MessageDigestPasswordEncoder sha512PasswordEncoder;
    
    public Sha512PasswordEncoder() {
    	sha512PasswordEncoder = new MessageDigestPasswordEncoder("SHA-512");
    }
    
    @Override
    public String encode(CharSequence rawPassword) {
        return sha512PasswordEncoder.encode(rawPassword);
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        if (encodedPassword.length() == 128) {
        	String sha512old = SecUtil.SHA512(rawPassword.toString());
            return (sha512old.equals(encodedPassword)) ? true:false;
        }

        if (encodedPassword.length() > 128) {
            return sha512PasswordEncoder.matches(rawPassword, encodedPassword);
        }

        return false;
    }
}
