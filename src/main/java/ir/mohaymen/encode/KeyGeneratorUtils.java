package ir.mohaymen.encode;


import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.security.PublicKey;

public final class KeyGeneratorUtils {

    public static PublicKey readPublicKeyFromPem(File file) throws IOException {
        try (FileReader keyReader = new FileReader(file)) {
            return readPublicKeyFromPem(keyReader);
        }
    }

    public static PublicKey readPublicKeyFromPem(Reader reader) throws IOException {
        try (PEMParser pemParser = new PEMParser(reader)) {
            JcaPEMKeyConverter converter = new JcaPEMKeyConverter();
            Object keyPair = pemParser.readObject();
            if (keyPair instanceof PEMKeyPair pemKeyPair) {
                return converter.getKeyPair(pemKeyPair).getPublic();
            } else {
                SubjectPublicKeyInfo publicKeyInfo = SubjectPublicKeyInfo.getInstance(keyPair);
                return converter.getPublicKey(publicKeyInfo);
            }
        }
    }
}