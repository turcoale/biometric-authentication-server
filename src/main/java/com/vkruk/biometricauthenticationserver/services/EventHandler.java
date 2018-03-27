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

    }

    @HandleAfterCreate
    public void newEmployeeAfter(Employee employee) {
        matchingService.prepareTemplates();
    }

    @HandleBeforeSave
    public void updateEmployeBefore(Employee employee) {

    }

    @HandleAfterSave
    public void updateEmployeAfter(Employee employee) {
        matchingService.prepareTemplates();
    }

    @HandleAfterDelete
    public void deleteEmployee(Employee employee) {
        matchingService.prepareTemplates();
    }
}
