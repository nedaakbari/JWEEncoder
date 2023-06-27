package ir.mohaymen.encode;

import com.nimbusds.jose.JOSEException;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
@RequiredArgsConstructor
public class Runner implements CommandLineRunner {
    private final EncryptCrypto encryptCrypto;

    @Override
    public void run(String... args) throws JOSEException {
        var scanner = new Scanner(System.in);
        while (true) {
            System.out.println("input data:");
            var input = scanner.nextLine();
            var jwe = encryptCrypto.encryptData(input);
            System.out.println("encrypted data:\n" + jwe);
        }
    }
}
