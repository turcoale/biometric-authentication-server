package com.vkruk.biometricauthenticationserver.models;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
public class Employee {

    private @Id Long id;
    private byte finger;
    private String template1;
    private String template2;

    private Employee() {
    }

    public Employee(Long id, byte finger, String template1, String template2) {
        this.id = id;
        this.finger = finger;
        this.template1 = template1;
        this.template2 = template2;
    }
}
