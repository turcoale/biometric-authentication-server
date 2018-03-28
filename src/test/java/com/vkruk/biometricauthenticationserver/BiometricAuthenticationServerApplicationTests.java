package com.vkruk.biometricauthenticationserver;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest({"db.port=3312",
		"db.username=root",
		"server.port=5050",
		"db.password=!QAZ1qaz",
		"db.schema=templates",
		"db.address=localhost"})
public class BiometricAuthenticationServerApplicationTests {

	@Autowired
	ApplicationContext ctx;

	@Test
	public void contextLoads() {
	}

}
