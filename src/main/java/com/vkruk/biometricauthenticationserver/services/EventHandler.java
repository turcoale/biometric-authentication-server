package com.vkruk.biometricauthenticationserver.services;

import com.vkruk.biometricauthenticationserver.models.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.*;
import org.springframework.stereotype.Component;

/**
 * Created by Vova on 20.03.2018.
 */
@Component
@RepositoryEventHandler(Employee.class)
public class EventHandler {

    private final MatchingService matchingService;

    @Autowired
    public EventHandler(MatchingService matchingService) {
        this.matchingService = matchingService;
    }

    @HandleBeforeCreate
    public void newEmployeeBefore(Employee employee) {
        int f=0;
        //matchingService.setExtractedTemplates(employee);
    }

    @HandleAfterCreate
    public void newEmployeeAfter(Employee employee) {
        int f=0;
    }

    @HandleBeforeSave
    public void updateEmployeBefore(Employee employee) {
        int f=0;
        //matchingService.setExtractedTemplates(employee);
    }

    @HandleAfterSave
    public void updateEmployeAfter(Employee employee) {
        int f=0;
    }

    @HandleAfterDelete
    public void deleteEmployee(Employee employee) {
        int f=0;
    }
}
