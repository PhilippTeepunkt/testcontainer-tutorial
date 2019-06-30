import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DatabaseConnectorTest{

    private static final Logger logger = LoggerFactory.getLogger(DatabaseConnectorTest.class);

    @Rule
    public MySQLContainer mySQLContainer = (MySQLContainer) new MySQLContainer()
            .withUsername("testuser")
            .withPassword("my-secret-pw")
            .withInitScript("init.sql")
            .withLogConsumer(new Slf4jLogConsumer(logger));

    @Test
    public void DatabaseTest()
    {
        try{

            //creates new jdbc instance for database connection
            JDBC jdbc = new JDBC(mySQLContainer.getJdbcUrl(),mySQLContainer.getUsername(),mySQLContainer.getPassword());

            //insert something
            jdbc.update("INSERT INTO contacts(ID,NAME,VORNAME,AGE) VALUES (0,'Tornow','Philipp',20),(0,'Simon','Jürgen',45);");

            //test if insertion works
            ResultSet result = jdbc.submitQuery("SELECT * FROM contacts WHERE NAME = 'Tornow'");

            assertTrue(result.next());
            assertEquals(20,result.getInt("AGE"));


        }catch (SQLException exception){

            logger.debug(exception.getMessage());
        }
    }
}