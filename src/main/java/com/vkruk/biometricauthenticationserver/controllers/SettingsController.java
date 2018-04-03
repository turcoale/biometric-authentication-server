package com.vkruk.biometricauthenticationserver.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Vova on 10.03.2018.
 */
@Controller
public class SettingsController {
    @RequestMapping(value = "/")
    public String settings(){
        return "index";
    }
}
