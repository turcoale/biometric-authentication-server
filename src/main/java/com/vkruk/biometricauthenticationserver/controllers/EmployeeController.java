package com.vkruk.biometricauthenticationserver.controllers;

import com.vkruk.biometricauthenticationserver.models.Employee;
import com.vkruk.biometricauthenticationserver.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

//@RepositoryRestController
@BasePathAwareController
public class EmployeeController {

    private final EmployeeRepository repository;

    @Autowired
    public EmployeeController(EmployeeRepository repository) {
        this.repository = repository;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/employees/{id}/templates")
    public @ResponseBody ResponseEntity<?> addUpdate(@PathVariable("id") int id) {
        List<Employee> employees = repository.findByEmployeeId(id);
        return new ResponseEntity<Object>(employees, HttpStatus.OK);

    }

}
