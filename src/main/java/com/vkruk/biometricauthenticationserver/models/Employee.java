package com.vkruk.biometricauthenticationserver.models;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import java.util.Base64;

@Data
@Entity
public class Employee {

    @ApiModelProperty(notes = "The database generated ID")
    private @Id @GeneratedValue Long id;
    @ApiModelProperty(notes = "Employee ID")
    private int employeeId;
    @ApiModelProperty(notes = "Finger number")
    private byte finger;
    @ApiModelProperty(notes = "First biometric template of finger")
    private @Lob String template0;
    @ApiModelProperty(notes = "Second biometric template of finger")
    private @Lob String template1;

    private Employee() {
    }

    public Employee(Long id, int employeeId, byte finger, String template0, String template1) {
        this.id = id;
        this.employeeId = employeeId;
        this.finger = finger;
        this.template0 = template0;
        this.template1 = template1;
    }

    public Employee(int employeeId, byte finger, String template0, String template1) {
        this.id = id;
        this.employeeId = employeeId;
        this.finger = finger;
        this.template0 = template0;
        this.template1 = template1;
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

    public String getTemplate0() {
        return template0;
    }

    public void setTemplate0(String template0) {
        this.template0 = template0;
    }

    public String getTemplate1() {
        return template1;
    }

    public void setTemplate1(String template1) {
        this.template1 = template1;
    }

    public byte[] imgTemplate0() {
        return Base64.getDecoder().decode(this.template0);
    }

    public byte[] imgTemplate1() {
        return Base64.getDecoder().decode(this.template1);
    }
}
