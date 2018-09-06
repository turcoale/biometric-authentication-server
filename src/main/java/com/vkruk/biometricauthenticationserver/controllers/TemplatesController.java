package com.vkruk.biometricauthenticationserver.controllers;

import com.vkruk.biometricauthenticationserver.services.MatchingService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.xml.ws.Response;
import java.util.Base64;

import static org.springframework.web.bind.annotation.RequestMethod.POST;


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
            String result = matchingService.identify(template);
            response = new ResponseEntity<Object>(result, HttpStatus.OK);
        } catch (Exception e) {
            response = new ResponseEntity<Object>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return response;

    }

    @ApiOperation(value = "${templates-controller.getTemplates}", response = String.class)
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Successfully extracted template", response = String.class, reference = "Base64String"),
                                @ApiResponse(code = 400, message = "Extracting failed. Returns error message.", response = String.class)})
    @RequestMapping(method = POST, value = "/getTemplateByBMP")
    public @ResponseBody ResponseEntity<Object> getTemplateByBMP(@RequestBody final String imageBase64){

        ResponseEntity<Object> response;

        try {
            byte[] image = Base64.getDecoder().decode(imageBase64);
            String template = matchingService.extractBase64Template(image);
            response = new ResponseEntity<Object>(template, HttpStatus.OK);
        } catch (Exception e) {
            response = new ResponseEntity<Object>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return response;

    }

    @ApiResponses(value = {@ApiResponse(code = 200, message = "Templates are similar", response = Integer.class)
            ,@ApiResponse(code = 400, message = "Templates are different", response = String.class)})
    @RequestMapping(method = POST, value = "/compare2Templates")
    public @ResponseBody ResponseEntity<Object> compareTemplates(@RequestBody @ApiParam(value = "Array of two templates.", required = true) final String[] templates){
        if(templates.length!=2 || !matchingService.compareTemplates(templates[0],templates[1])){
            return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<Object>(HttpStatus.OK);
    }
}
