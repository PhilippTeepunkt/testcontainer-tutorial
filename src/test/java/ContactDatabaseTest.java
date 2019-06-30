import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;

import java.sql.Date;

import static org.junit.Assert.assertEquals;

public class ContactDatabaseTest{

    private static final Logger logger = LoggerFactory.getLogger(ContactDatabaseTest.class);

    @Rule
    public MySQLContainer mySQLContainer = (MySQLContainer) new MySQLContainer()
            .withUsername("testuser")
            .withPassword("my-secret-pw")
            .withLogConsumer(new Slf4jLogConsumer(logger));

    @Test
    public void DatabaseTest()
    {
        Contact contact = new Contact("Tornow","Philipp", new Date(2019,06,30));
        logger.info(contact.name+contact.prename+contact.birthday);

        assertEquals(contact.name,"Tornow");
    }
}