package ir.mohaymen.encode;

import com.nimbusds.jose.EncryptionMethod;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWEAlgorithm;
import com.nimbusds.jose.JWEHeader;
import com.nimbusds.jose.crypto.ECDHEncrypter;
import com.nimbusds.jwt.EncryptedJWT;
import com.nimbusds.jwt.JWTClaimsSet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.security.interfaces.ECPublicKey;
import java.util.Date;

@Component
public class EncryptCrypto {

    private static final String DATA = "data";
    private static final JWEAlgorithm ALGORITHM = JWEAlgorithm.ECDH_ES;
    private static final EncryptionMethod ENC = EncryptionMethod.A256GCM;
    private final ECDHEncrypter ecdhEncrypter;


    public EncryptCrypto(@Value("${encryption_data.publicKey}") String filePath) throws IOException, JOSEException {
        ECPublicKey ecPublicKey = getPublicKey(filePath);
        this.ecdhEncrypter = new ECDHEncrypter(ecPublicKey);
    }

    public String encryptData(String plainText) throws JOSEException {
        var header = new JWEHeader(ALGORITHM, ENC);

        var claimsSet = new JWTClaimsSet.Builder()
                .claim(DATA, plainText)
                .issueTime(new Date()).build();

        var jwt = new EncryptedJWT(header, claimsSet);
        jwt.encrypt(ecdhEncrypter);
        return jwt.serialize();
    }

    private ECPublicKey getPublicKey(String filePath) throws IOException {
        String privateKeyPath = Paths.get("").toAbsolutePath() + "/EC/" + filePath;
        return (ECPublicKey) KeyGeneratorUtils.readPublicKeyFromPem(new File(privateKeyPath));
    }
}