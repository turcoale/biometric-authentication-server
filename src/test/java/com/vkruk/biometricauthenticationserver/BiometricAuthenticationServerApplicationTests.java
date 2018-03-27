package com.vkruk.biometricauthenticationserver;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
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
		/*CommandLineRunner runner = ctx.getBean(CommandLineRunner.class);
		try {
			runner.run ("--server.port=5050",
                    "--db.username=root",
                    "--db.password=!QAZ1qaz",
                    "--db.port=3312",
                    "--db.schema=templates",
                    "--db.address=localhost");
		} catch (Exception e) {
			e.printStackTrace();
		}*/
	}

}
