import org.junit.ClassRule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static org.junit.Assert.assertEquals;

public class SimpleGenericContainerTest {

    private static Logger logger = LoggerFactory.getLogger(SimpleGenericContainerTest.class);

    @ClassRule
    public static GenericContainer simpleAlpineServer = new GenericContainer("alpine:3.10.0")
            .withExposedPorts(80) //maps port to 80
            .withCommand("/bin/sh", "-c",
                    "while true; do echo \"HTTP/1.1 200 OK\n\nHello Testcontainer!\" | nc -l -p 80; done") //command to work as webserver
            .withLogConsumer(new Slf4jLogConsumer(logger)); //logs container

    @Test
    public void simpleWebserverTest()throws Exception {

        //Create HTTP URL
        String address = "http://"+simpleAlpineServer.getContainerIpAddress()+":"+simpleAlpineServer.getMappedPort(80);

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

}
