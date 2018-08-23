package com.map;

import com.map.service.MapService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
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

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= WebEnvironment.RANDOM_PORT)
public class MapServiceApplicationTests {
	@Autowired
	ApplicationContext ctx;

	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	private MapService service;

	@Value("classpath:city-test.txt")
	private Resource file;

	private List<String> testCases;

	@LocalServerPort
	private int port;

	private static final String serviceUrl = "http://localhost:%d/connected?origin=%s&destination=%s";

	@Before
	public void setUp() throws IOException {
		testCases = Files.readAllLines(Paths.get(file.getURI()), StandardCharsets.UTF_8);
	}

	@Test
	public void test() {
		String getUrl = null;
		System.out.println("serviceUrl: " + serviceUrl);
		for (String line : testCases) {
			if(line.trim().isEmpty()) continue;

			String[] items = line.trim().split(",");
			String expected = items[2].trim();

//			String message = (service.hasBFSPath(items[0], items[1])) ? "yes" : "no";
//			assertEquals(message, expected);
//			System.out.println(items[0] + " ==== " + items[1] + " -> " + message);

			getUrl = String.format(serviceUrl, port, items[0], items[1]);
			ResponseEntity<String> response = restTemplate.getForEntity(getUrl, String.class);
			String message = response.getBody();
//			assertEquals(message, expected);
			System.out.println(items[0] + " ==== " + items[1] + " -> " + message);
		}
	}


}
