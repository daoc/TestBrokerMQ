
package ec.edu.ute.dordonez.testrabbit02;

/**
 *
 * @author dordonez@ute.edu.ec
 */
public class SslServerSocket {
    
    public static void main(String[] args) {
        System.setProperty("javax.net.ssl.keyStore", "keystoreFile");
        System.setProperty("javax.net.ssl.keyStorePassword", "keystorePassword");

        
    }
}
