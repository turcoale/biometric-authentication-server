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

    public int getEmployeeId() {
        return employeeId;
    }

    public byte[] getImgTemplate1() {
        return Base64.getDecoder().decode(this.template1);
    }

    public byte[] getImgTemplate2() {
        return Base64.getDecoder().decode(this.template2);
    }

    public void setTemplate1(String template1) {
        this.template1 = template1;
    }

    public void setTemplate2(String template2) {
        this.template2 = template2;
    }


    public Long getId() {
        return id;
    }

    public byte getFinger() {
        return finger;
    }

    public String getTemplate1() {
        return template1;
    }

    public String getTemplate2() {
        return template2;
    }
}
