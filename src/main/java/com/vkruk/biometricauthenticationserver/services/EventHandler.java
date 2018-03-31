package com.vkruk.biometricauthenticationserver.services;

import com.vkruk.biometricauthenticationserver.models.Employee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger LOGGER = LoggerFactory.getLogger(EventHandler.class);

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
        logEvent("Created", employee);
    }

    @HandleBeforeSave
    public void updateEmployeBefore(Employee employee) {

    }

    @HandleAfterSave
    public void updateEmployeAfter(Employee employee) {
        matchingService.prepareTemplates();
        logEvent("Updated", employee);
    }

    @HandleAfterDelete
    public void deleteEmployee(Employee employee) {
        matchingService.prepareTemplates();
        logEvent("Deleted", employee);
    }

    public void logEvent(String event, Employee employee){
        LOGGER.info(event + " -> employee: "+employee.getEmployeeId()+"; finger: "+employee.getFinger());
    }
}
