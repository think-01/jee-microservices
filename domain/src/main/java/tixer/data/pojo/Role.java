package tixer.data.pojo;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by slawek@t01.pl on 2016-03-10.
 */
@Entity
@Table(name = "acl_roles")
public class Role  implements Serializable {

    @Id
    @GeneratedValue
    public Integer id;

    public String name;

    @Transient
    public String label;

    public String getLabel() {

        return name.toUpperCase()
                .replaceAll("Ą", "A")
                .replaceAll("Ę", "E")
                .replaceAll("Ś", "S")
                .replaceAll("Ż", "Z")
                .replaceAll("Ć", "C")
                .replaceAll("Ź", "Z")
                .replaceAll("Ń", "N")
                .replaceAll("Ó", "O")
                .replaceAll("Ł", "L")
                .replaceAll("[^A-Z0-9-]", "_");
    }
}
