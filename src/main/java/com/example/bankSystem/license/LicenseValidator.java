package com.example.bankSystem.license;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

@Component
public class LicenseValidator implements CommandLineRunner {

    @Value("${app.license.key}")
    private String licenseKey;

    private static final String EXPECTED_HASH = "cb9bc9db969c30cfff4e59db844dfef124cc7795fa3a766e21bcc6c41886263e";

    @Override
    public void run(String... args) throws Exception {
        String hashedInput = hash(licenseKey);

        if (!EXPECTED_HASH.equals(hashedInput)) {
            System.err.println("❌ Invalid license. Contact developer to activate.");
        }
        System.out.println("✅ License validated successfully.");
    }

    private String hash(String input) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hashBytes = digest.digest(input.getBytes(StandardCharsets.UTF_8));
        StringBuilder hexString = new StringBuilder();
        for (byte b : hashBytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
