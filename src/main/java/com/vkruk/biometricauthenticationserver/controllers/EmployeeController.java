package com.vkruk.biometricauthenticationserver.controllers;

import com.vkruk.biometricauthenticationserver.models.Employee;
import com.vkruk.biometricauthenticationserver.models.Template;
import com.vkruk.biometricauthenticationserver.repository.EmployeeRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@BasePathAwareController
public class EmployeeController {

    private final EmployeeRepository repository;

    @Autowired
    public EmployeeController(EmployeeRepository repository) {
        this.repository = repository;
    }

    @ApiOperation(value = "${employee-controller.addUpdate}")
    @RequestMapping(method = RequestMethod.POST, value = "/employee/{id}/templates")
    public @ResponseBody List<Employee> addUpdate( @ApiParam(value = "${employee-controller.addUpdate.id}", required = true)
                                                      @PathVariable("id") final int id,
                                                      @RequestBody final List<Template> templates) {

        repository.deleteAllByEmployeeId(id);

        templates.forEach(template -> {
            Employee employee = new Employee(id,template.getFinger(),template.getTemplate0(),template.getTemplate0());
            repository.save(employee);
        });

        return repository.findByEmployeeId(id);

    }

}
