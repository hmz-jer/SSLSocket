package org.iri;

import javax.net.ServerSocketFactory;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.TrustManagerFactory;
import java.io.*;
import java.net.Socket;
import java.security.KeyStore;

public class ServerSocket {
    private static final int PORT = 12346;

    public static void main(String[] args) {
        try {
            // Charger le keystore du serveur
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            try (InputStream keyStoreStream = ServerSocket.class.getResourceAsStream("/iriserver-keystore.p12")) {
                keyStore.load(keyStoreStream, "azerty".toCharArray());
            }

            // Charger le truststore du serveur
            KeyStore trustStore = KeyStore.getInstance("PKCS12");
            try (InputStream trustStoreStream = ServerSocket.class.getResourceAsStream("/iriserver-truststore.p12")) {
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

            ServerSocketFactory serverSocketFactory = sslContext.getServerSocketFactory();
            try (SSLServerSocket serverSocket = (SSLServerSocket) serverSocketFactory.createServerSocket(PORT)) {
                System.out.println("Server is running...");

                while (true) {
                    try (Socket clientSocket = serverSocket.accept()) {
                        System.out.println("SSL connection established with client: " + clientSocket.getInetAddress());
                        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

                            String inputLine;
                            while ((inputLine = in.readLine()) != null) {
                                System.out.println("Received: " + inputLine);
                                out.println("Echo: " + inputLine);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
