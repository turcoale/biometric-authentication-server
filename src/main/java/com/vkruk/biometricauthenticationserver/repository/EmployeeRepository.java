package com.vkruk.biometricauthenticationserver.repository;


import com.vkruk.biometricauthenticationserver.models.Employee;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Vova on 10.03.2018.
 */
@Repository
public interface EmployeeRepository extends CrudRepository<Employee, Long>{

    List<Employee> findByEmployeeId(@Param("id") int id);

    @Transactional
    long deleteAllByEmployeeId(@Param("id") int id);

    List<Employee> findByEmployeeIdAndFinger(int id, byte finger);

}
