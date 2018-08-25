package com.map;

import com.map.service.MapService;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Logger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MapServiceApplicationTests {

    private static final String serviceUrl = "http://localhost:%d/connected?origin=%s&destination=%s";
    private static final String bfsUrl = "http://localhost:%d/connections?origin=%s";
    private static final String errorUrl = "http://localhost:%d/connected";
    private Logger log = Logger.getLogger(this.getClass().getName());
    @Autowired
    private ApplicationContext ctx;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private MapService service;

    @Value("classpath:city-test.txt")
    private Resource file;

    @LocalServerPort
    private int port;

    private List<String> testCases;

    @Before
    public void setUp() throws IOException {
        testCases = Files.readAllLines(Paths.get(file.getURI()), StandardCharsets.UTF_8);
    }

    @Test
    public void testBfs_ValidRoot() {
        String src = "Boston";
        int unexpected = 0;
        String bfsPath = service.bfs(src);

        assertNotEquals(unexpected, bfsPath.length());
    }

    @Test
    public void testBfs_nullRoot() {
        String src = "NullCity";
        String expected = "";

        String bfsPath = service.bfs(src);
        assertEquals(expected, bfsPath);
    }

    @Test
    public void testBfsRestApi_InvalidCity() {
        String src = "InvalidCity";
        String expected = null;

        String getUrl = String.format(bfsUrl, port, src);
        ResponseEntity<String> response = restTemplate.getForEntity(getUrl, String.class);
        String message = response.getBody();
        log.info(src + " -> " + message);

        assertEquals(expected, message);
    }

    @Test
    public void testBfsRestApi_EmptyCity() {
        String src = "";
        String expected = "Please provide a city for origin";

        String getUrl = String.format(bfsUrl, port, src);
        ResponseEntity<String> response = restTemplate.getForEntity(getUrl, String.class);
        String message = response.getBody();
        log.info(src + " -> " + message);

        assertEquals(expected, message);
    }

    @Test
    public void testBfsRestApi_Error() {
        String expected = "Error-400: Required String parameter 'origin' is not present";

        String getUrl = String.format(errorUrl, port);
        ResponseEntity<String> response = restTemplate.getForEntity(getUrl, String.class);
        String message = response.getBody();
        log.info(" -> " + message);

        assertEquals(expected, message);
    }

    @Test
    public void testBfsRestApi_ValidCity() {
        String src = "Boston";
        int unexpected = 0;

        String getUrl = String.format(bfsUrl, port, src);
        ResponseEntity<String> response = restTemplate.getForEntity(getUrl, String.class);
        String message = response.getBody();
        log.info(src + " -> " + message);

        assertNotEquals(unexpected, message.length());
    }

    @Test
    public void testHasBfsPathService() {
        for (String line : testCases) {
            if (line.trim().isEmpty()) {
                continue;
            }

            String[] items = line.split(",");
            String expected = items[2].trim();
            String src = getCity(items[0]);
            String dest = getCity(items[1]);

            String message = (service.hasBFSPath(src, dest)) ? "yes" : "no";
            log.info(src + " - " + dest + " -> " + message);
            assertEquals(expected, message);
        }
    }

    @Test
    public void testHasBfsPathRestApi() {
        String getUrl = null;
        log.info("serviceUrl: " + serviceUrl);
        for (String line : testCases) {
            if (line.trim().isEmpty()) {
                continue;
            }

            String[] items = line.split(",");
            String expected = items[3].trim();
            String src = items[0];
            String dest = items[1];

            getUrl = String.format(serviceUrl, port, src, dest);
            ResponseEntity<String> response = restTemplate.getForEntity(getUrl, String.class);
            String message = response.getBody();
            log.info(src + " - " + dest + " -> " + message);
            assertEquals(expected, message);
        }
    }

    private String getCity(String city) {
        return city.isEmpty() ? null : city;
    }
}
