import java.sql.Date;

public class Contact {

    public String name;
    public String prename;
    public Date birthday;

    public Contact(String name, String prename, Date birthday) {
        this.name = name;
        this.prename = prename;
        this.birthday = birthday;
    }
}
