package com.vkruk.biometricauthenticationserver.controllers;

import com.vkruk.biometricauthenticationserver.models.Employee;
import com.vkruk.biometricauthenticationserver.repository.EmployeeRepository;
import com.vkruk.biometricauthenticationserver.services.MatchingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Base64;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * Created by Vova on 18.03.2018.
 */

@BasePathAwareController
public class TemplatesController {

    private final MatchingService matchingService;

    @Autowired
    public TemplatesController(MatchingService matchingService) {
        this.matchingService = matchingService;
    }

    @RequestMapping(method = POST, value = "/identify")
    public @ResponseBody int identify(HttpEntity<String> httpEntity){
        return matchingService.identify(httpEntity.getBody());
    }

    @RequestMapping(method = POST, value = "/getTemplate")
    public @ResponseBody String getTemplate(HttpEntity<String> httpEntity){
        byte[] image = Base64.getDecoder().decode(httpEntity.getBody());
        return matchingService.extractBase64Template(image);
    }
}
