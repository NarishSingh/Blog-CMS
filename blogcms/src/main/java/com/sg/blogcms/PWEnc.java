package com.sg.blogcms;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Takes in the password and then encodes it to String, and will print to console
 */
public class PWEnc {
    public static void main(String[] args) {
        String clearTxtPw = "password";

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String hashedPw = encoder.encode(clearTxtPw);
        System.out.println(hashedPw);
    }
}