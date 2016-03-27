package tixer.data.dao;

import com.sun.mail.smtp.SMTPMessage;
import tixer.data.pojo.LogRecord;

import javax.annotation.Resource;
import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.*;
import java.util.stream.Stream;

/**
 * Created by slawek@t01.pl on 2016-03-17.
 */
@Stateless
@Asynchronous
public class LogsManager{

    @PersistenceContext
    private EntityManager em;

    @Resource(name = "java:/TixerGmail")
    private Session session;

    public void create(
            String uuid,
            String log,
            String client
    ) {


        em.persist(
                new LogRecord(
                        uuid,
                        client,
                        log
                )
        );

        try {
            String addy1 = System.getProperty("tixer.email.reporting");
            String addy2 = System.getProperty("tixer.email.reporting." + client);

            Iterator<String> recipients = Stream.concat(
                    Arrays.asList(addy1 == null ? new String[0] : addy1.split(",")).stream(),
                    Arrays.asList(addy2 == null ? new String[0] : addy2.split(",")).stream()
            ).distinct().map(n -> n.trim()).iterator();

            SMTPMessage message = new SMTPMessage(session);
            message.setSendPartial( true );

            while( recipients.hasNext() )
                message.addRecipients(
                        Message.RecipientType.BCC,
                        InternetAddress.parse(recipients.next())
                );

            message.setSubject( "API Error: " + uuid );
            message.setText( log );

            Transport.send(message);

        } catch ( Exception e) {
            System.err.println( e.getMessage() );
        }
    }
}
