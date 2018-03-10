package com.vkruk.biometricauthenticationserver.repository;


import com.vkruk.biometricauthenticationserver.models.Employee;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by Vova on 10.03.2018.
 */
public interface EmplyeeRepository extends CrudRepository<Employee, Long> {
}
