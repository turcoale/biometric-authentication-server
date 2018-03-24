package com.vkruk.biometricauthenticationserver.services;

import com.vkruk.biometricauthenticationserver.models.Employee;
//import org.springframework.lang.Nullable; @Nullable
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * Created by Vova on 20.03.2018.
 */
public class EmployeeValidator implements Validator {

    @Override
    public boolean supports(Class<?> aClass) {
        return  Employee.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {

        final Employee employee = (Employee) o;

        if (StringUtils.isEmpty(employee.getTemplate1())) {
            errors.rejectValue("template1", "template1.empty");
        }

        if (StringUtils.isEmpty(employee.getTemplate2())) {
            errors.rejectValue("template2", "template2.empty");
        }
    }
}
