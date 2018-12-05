
package ec.edu.ute.dordonez.testrabbit02;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.Scanner;
import javax.net.ServerSocketFactory;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;

/**
 *
 * @author dordonez@ute.edu.ec
 */
public class SslServerSocket {
    
    public static void main(String[] args) throws Exception {
        System.setProperty("javax.net.ssl.trustStore", "myTrustStore");
        //System.setProperty("javax.net.ssl.trustStorePassword", "");
        System.setProperty("javax.net.ssl.keyStore", "localhostServer.pkcs12");
        System.setProperty("javax.net.ssl.keyStorePassword", "localhostServer");
        System.setProperty("javax.net.ssl.keyStoreType", "PKCS12");

        ServerSocketFactory ssf = SSLServerSocketFactory.getDefault();
        //ServerSocketFactory ssf = getServerSocketFactory();
        ServerSocket ss = ssf.createServerSocket(8888);
        System.out.println("Esperando cliente");
        Socket socket = ss.accept();
        System.out.println("Cliente conectado");
        Scanner scan = new Scanner(socket.getInputStream());
        PrintWriter pw = new PrintWriter(socket.getOutputStream(), true);
        
        while(true) {
            String line = scan.nextLine();
            pw.println("ECHO: " + line);
        }
        
    }
    
    private static ServerSocketFactory getServerSocketFactory() throws NoSuchAlgorithmException, KeyStoreException, FileNotFoundException, IOException, CertificateException, UnrecoverableKeyException, KeyManagementException {
        SSLContext ctx;
        KeyManagerFactory kmf;
        KeyStore ks;
        //password con que se creo la keystore
        char[] passphrase = "localhostServer".toCharArray();

        ctx = SSLContext.getInstance("TLS");
        kmf = KeyManagerFactory.getInstance("SunX509");
        ks = KeyStore.getInstance("PKCS12");

        //archivo con la keystore
        ks.load(new FileInputStream("localhostServer.pkcs12"), passphrase);
        kmf.init(ks, passphrase);
        ctx.init(kmf.getKeyManagers(), null, null);

        SSLServerSocketFactory ssf = ctx.getServerSocketFactory();
        return ssf;
    }  
    
}
