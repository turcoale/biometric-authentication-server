package com.vkruk.biometricauthenticationserver.services;

import com.vkruk.biometricauthenticationserver.models.Employee;
//import org.springframework.lang.Nullable; @Nullable
import com.vkruk.biometricauthenticationserver.models.Template;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class EmployeeValidator implements Validator {

    @Override
    public boolean supports(Class<?> aClass) {
        return  Employee.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {

        final Employee employee = (Employee) o;

        if (StringUtils.isEmpty(employee.getTemplate0())) {
            errors.rejectValue("template0", "template1.empty");
        }

        if (StringUtils.isEmpty(employee.getTemplate1())) {
            errors.rejectValue("template1", "template2.empty");
        }
    }
}
