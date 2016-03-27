package tixer.data.pojo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by slawek@t01.pl on 2016-03-17.
 */
@Entity
@Table(name = "api_logs")
public class LogRecord {

    @Id
    @GeneratedValue
    public Integer id;

    public LogRecord(String uid, String client, String body) {
        this.client = client;
        this.body = body;
        this.fingerprint = uid;
    }

    public String fingerprint;

    public String client;

    public String body;
}
