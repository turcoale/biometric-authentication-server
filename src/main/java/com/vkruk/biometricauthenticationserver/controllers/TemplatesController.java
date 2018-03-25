package com.vkruk.biometricauthenticationserver.controllers;

import com.sun.xml.internal.org.jvnet.staxex.Base64Data;
import com.vkruk.biometricauthenticationserver.models.Employee;
import com.vkruk.biometricauthenticationserver.repository.EmployeeRepository;
import com.vkruk.biometricauthenticationserver.services.MatchingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Base64;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * Created by Vova on 18.03.2018.
 */
@Api(value="Templates", description="Operations for working with finger templates")
@BasePathAwareController
public class TemplatesController {

    private final MatchingService matchingService;

    @Autowired
    public TemplatesController(MatchingService matchingService) {
        this.matchingService = matchingService;
    }

    @ApiOperation(value = "${templates-controller.identify}", response = String.class)
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Successfully identified. Returns employee id.", response = Integer.class)
                            ,@ApiResponse(code = 400, message = "Identification failed. Returns error message.", response = String.class)})
    @RequestMapping(method = POST, value = "/findEmployeeByTemplate")
    public @ResponseBody ResponseEntity<Object> identify(@RequestBody final String template){

        ResponseEntity<Object> response;

        try {
            int result = matchingService.identify(template);
            response = new ResponseEntity<Object>(result, HttpStatus.OK);
        } catch (Exception e) {
            response = new ResponseEntity<Object>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return response;

    }

    @ApiOperation(value = "${templates-controller.getTemplates}", response = String.class)
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Successfully extracted template", response = String.class, reference = "Base64String")})
    @RequestMapping(method = POST, value = "/getTemplateByBMP")
    public @ResponseBody String getTemplateByBMP(@RequestBody final String imageBase64){
        byte[] image = Base64.getDecoder().decode(imageBase64);
        return matchingService.extractBase64Template(image);
    }
}
