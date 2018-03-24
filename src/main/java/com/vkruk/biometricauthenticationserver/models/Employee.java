package com.vkruk.biometricauthenticationserver.models;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import java.util.Base64;

@Data
@Entity
public class Employee {

    private @Id @GeneratedValue Long id;
    private int employeeId;
    private byte finger;
    private @Lob String template1;
    private @Lob String template2;

    private Employee() {
    }

    public Employee(Long id, int employeeId, byte finger, String template1, String template2) {
        this.id = id;
        this.employeeId = employeeId;
        this.finger = finger;
        this.template1 = template1;
        this.template2 = template2;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public byte getFinger() {
        return finger;
    }

    public void setFinger(byte finger) {
        this.finger = finger;
    }

    public String getTemplate1() {
        return template1;
    }

    public void setTemplate1(String template1) {
        this.template1 = template1;
    }

    public String getTemplate2() {
        return template2;
    }

    public void setTemplate2(String template2) {
        this.template2 = template2;
    }


    public byte[] imgTemplate1() {
        return Base64.getDecoder().decode(this.template1);
    }

    public byte[] imgTemplate2() {
        return Base64.getDecoder().decode(this.template2);
    }
}
