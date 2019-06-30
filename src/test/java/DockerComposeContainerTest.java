import com.mysql.jdbc.log.LogFactory;
import org.junit.ClassRule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DockerComposeContainerTest {

    private static Logger logger = LoggerFactory.getLogger(DockerComposeContainerTest.class);

    @ClassRule
    public static DockerComposeContainer composeContainer = new DockerComposeContainer(
            new File("src/test/resources/compose-test.yml")) //load yml file for compose
            .withExposedService("simpleAlpineServer",80) //set webserver port
            .withExposedService("simpleMySQLContainer,",3306) //set default mysql port
            .withLogConsumer("simpleAlpineServer",new Slf4jLogConsumer(logger));



    @Test
    public void simpleWebserverTest()throws Exception {

        //Create HTTP URL
        String address = "http://"+composeContainer.getServiceHost("simpleAlpineServer",80)
                +":"
                +composeContainer.getServicePort("simpleAlpineServer",80);

        //create connection + request
        URL url = new URL(address);
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        connection.setRequestMethod("GET");

        //read receiving data
        BufferedReader buffIn = new BufferedReader(
                new InputStreamReader(connection.getInputStream()));

        String inputLine;
        StringBuffer content = new StringBuffer();

        while ((inputLine = buffIn.readLine())!=null)
        {
            content.append(inputLine);
        }

        buffIn.close();

        // compare response with expected
        String response = content.toString();
        assertEquals("Hello Testcontainer!",response);
    }

    @Test
    public void DatabaseTest()
    {
        try{

            //creates new jdbc instance for database connection
            JDBC jdbc = new JDBC("jdbc:mysql://"+composeContainer.getServiceHost("simpleMySQLContainer",80),"root","example");

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
