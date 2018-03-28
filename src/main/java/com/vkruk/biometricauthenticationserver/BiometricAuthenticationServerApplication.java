package com.vkruk.biometricauthenticationserver;

import com.vkruk.biometricauthenticationserver.services.EmployeeValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.data.rest.core.event.ValidatingRepositoryEventListener;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurerAdapter;
import springfox.documentation.spring.data.rest.configuration.SpringDataRestConfiguration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;



@SpringBootApplication
@EnableSwagger2
@Import(SpringDataRestConfiguration.class)
public class BiometricAuthenticationServerApplication extends RepositoryRestConfigurerAdapter{

	public static void main(String[] args) {
		SpringApplication.run(BiometricAuthenticationServerApplication.class, args);
	}

	@Override
	public void configureValidatingRepositoryEventListener(ValidatingRepositoryEventListener validatingListener) {
		validatingListener.addValidator("beforeSave", new EmployeeValidator());
	}

}
