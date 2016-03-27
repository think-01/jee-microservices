package tixer.system.beans;

/**
 * Created by slawek@t01.pl on 2016-03-08.
 */
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;

import javax.annotation.PostConstruct;
import javax.ejb.*;
import java.security.SecureRandom;
import java.util.Arrays;

@Singleton
@Startup
@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
public class KeyFactory {

    private JWSSigner signer;
    private JWSHeader header;

    @PostConstruct
    @Lock(LockType.WRITE)
    public void applicationStartup() {
        byte[] token = new byte[64];
        SecureRandom sr = new SecureRandom();
        sr.nextBytes(token);

        String key = System.getProperty("tixer.development");
        if( key != null ) Arrays.fill(token, (byte) 100 );

        try {
            signer = new MACSigner(token);
            header = new JWSHeader(JWSAlgorithm.HS256);
        }
        catch( Exception e ) {
            System.err.println( e.getMessage() );
        }
    }

    @Lock(LockType.READ)
    public String issue(String kid){
        try {
            return signer.sign(header, kid.getBytes()).toString();
        }
        catch( Exception e ) {
            System.err.println( e.getMessage() );
            return "";
        }
    }
}
