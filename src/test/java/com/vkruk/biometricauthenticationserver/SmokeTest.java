package com.vkruk.biometricauthenticationserver;

import static org.assertj.core.api.Assertions.assertThat;

import com.vkruk.biometricauthenticationserver.controllers.EmployeeController;
import com.vkruk.biometricauthenticationserver.controllers.SettingsController;
import com.vkruk.biometricauthenticationserver.controllers.TemplatesController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest({"db.port=3312",
        "db.username=root",
        "server.port=5050",
        "db.password=!QAZ1qaz",
        "db.schema=templates",
        "db.address=localhost"})
public class SmokeTest {

    @Autowired
    private EmployeeController employeeController;
    @Autowired
    private TemplatesController templatesController;
    @Autowired
    private SettingsController settingsController;


    @Test
    public void contexLoads() throws Exception {
        assertThat(employeeController).isNotNull();
        assertThat(templatesController).isNotNull();
        assertThat(settingsController).isNotNull();
    }
}
