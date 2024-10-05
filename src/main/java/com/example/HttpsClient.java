package com.example;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import java.net.URL;
import java.security.KeyStore;
import java.security.cert.X509Certificate;

public class HttpsClient {

    public static void main(String[] args) {
        try {
            // URL del server al quale connettersi
            URL url = new URL("https://esempio.com");

            // Se vuoi fidarti di tutti i certificati (solo per sviluppo)
            SSLContext sslContext = SSLContext.getInstance("TLS");
            TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    public X509Certificate[] getAcceptedIssuers() {
                        return new X509Certificate[]{};
                    }
                    public void checkClientTrusted(X509Certificate[] certs, String authType) {
                    }
                    public void checkServerTrusted(X509Certificate[] certs, String authType) {
                    }
                }
            };
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());

            // Se vuoi usare un TrustManager basato sul keystore di sistema (sicuro per produzione)
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tmf.init((KeyStore) null);
            sslContext.init(null, tmf.getTrustManagers(), new java.security.SecureRandom());

            // Stabilisce la connessione
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setSSLSocketFactory(sslContext.getSocketFactory());
            conn.setRequestMethod("GET");

            // Leggi i dati dalla connessione
            int responseCode = conn.getResponseCode();
            System.out.println("Risposta HTTP: " + responseCode);

            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
