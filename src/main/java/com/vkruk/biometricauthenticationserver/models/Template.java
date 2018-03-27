package com.vkruk.biometricauthenticationserver.models;


import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;


public class Template {

    @ApiModelProperty(notes = "Finger number")
    private byte finger;
    @ApiModelProperty(notes = "First biometric template of finger")
    private String template0;
    @ApiModelProperty(notes = "Second biometric template of finger")
    private @NotBlank String template1;

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

}
