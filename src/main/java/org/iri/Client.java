package org.iri;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;
import java.io.*;
import java.security.KeyStore;
import java.util.Scanner;

public class Client {
    private static final String HOST = "localhost";
    private static final int PORT = 12346;

    public static void main(String[] args) {
        try {
            // Charger le keystore du client
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            try (InputStream keyStoreStream = Client.class.getResourceAsStream("/icoclient-keystore.p12")) {
                keyStore.load(keyStoreStream, "azerty".toCharArray());
            }

            // Charger le truststore du client
            KeyStore trustStore = KeyStore.getInstance("PKCS12");
            try (InputStream trustStoreStream = Client.class.getResourceAsStream("/icoclient-truststore.p12")) {
                trustStore.load(trustStoreStream, "azerty".toCharArray());
            }

            // Initialiser le KeyManager et le TrustManager
            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
            keyManagerFactory.init(keyStore, "azerty".toCharArray());

            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance("SunX509");
            trustManagerFactory.init(trustStore);

            // Initialiser le contexte SSL
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), null);

            SSLSocketFactory socketFactory = sslContext.getSocketFactory();
            try (SSLSocket socket = (SSLSocket) socketFactory.createSocket(HOST, PORT);
                 BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                 PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                 Scanner scanner = new Scanner(System.in)) {

                System.out.println("SSL connection established with server.");
                System.out.println("Connected to Server. Type your messages:");
                String message;
                while (true) {
                    System.out.print("Enter message (or 'exit' to quit): ");
                    message = scanner.nextLine();
                    if ("exit".equalsIgnoreCase(message)) {
                        break;
                    }
                    out.println(message);
                    System.out.println("Server response: " + in.readLine());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
