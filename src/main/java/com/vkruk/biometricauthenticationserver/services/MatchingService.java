package com.vkruk.biometricauthenticationserver.services;

import com.vkruk.biometricauthenticationserver.models.Employee;


/**
 * Created by Vova on 10.03.2018.
 */
public interface MatchingService {
    String identify(String template) throws Exception;
    String extractBase64Template(byte[] image) throws Exception;
    void prepareTemplates();
    boolean compareTemplates(String template1, String template2);
}
