import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ContactDatabase {

    private JDBC jdbc;

    public ContactDatabase(JDBC jdbc) {
        this.jdbc = jdbc;
    }

    public void addContact(Contact contact) {
        try {
            jdbc.update("INSERT INTO contacts (ID,NAME,VORNAME,GEBURTSTAG) VALUES (0," + contact.name + "," + contact.prename + "," + contact.birthday + ");");
        }
        catch (SQLException exception)
        {
            System.out.println(exception.getMessage());
        }
    }

    public Contact getContact(String name) {
        try {
            ResultSet result = jdbc.submitQuery("SELECT * FROM contacts as c, WHERE name ='" + name + "';");

            if (!result.next()) {
                String contactName = result.getString("NAME");
                String contactPrename = result.getString("VORNAME");
                Date contactBirthday = result.getDate("GEBURTSTAG");

                return new Contact(contactName, contactPrename, contactBirthday);
            } else {
                System.out.println("Contact with name: " + name + " is not in the database!");
                return null;
            }

        } catch (SQLException exception) {
            System.out.println(exception.getMessage());
        }
        return null;
    }
}
