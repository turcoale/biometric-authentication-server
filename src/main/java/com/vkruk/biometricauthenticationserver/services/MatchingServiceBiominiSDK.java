package com.vkruk.biometricauthenticationserver.services;

import com.suprema.ImageSDK;
import com.vkruk.biometricauthenticationserver.models.Employee;
import com.vkruk.biometricauthenticationserver.repository.EmployeeRepository;
import jdk.nashorn.internal.ir.IfNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;

/**
 * Created by Vova on 10.03.2018.
 */
@Service
public class MatchingServiceBiominiSDK implements MatchingService {

    final int MAX_TEMPLATE_SIZE = 384;
    final int IMG_WIDTH = 300;
    final int IMG_HEIGHT = 400;
    final int IMG_RESOLUTION = 500;

    private ImageSDK sdk;
    private long[] hMatcher = new long[1];
    private long[] hExtractorContainer = new long[1];

    private final EmployeeRepository repository;

    @Autowired
    public MatchingServiceBiominiSDK(EmployeeRepository repository) {

        this.repository = repository;
        this.sdk = new ImageSDK();

        sdk.UFE_Create(hExtractorContainer);
        sdk.UFM_Create(hMatcher);

    }

    @Override
    public int identify(String template) {

        byte[] bTemplate = extractByteTemplate(Base64.getDecoder().decode(template));

        Map<String, Object> templatesData = getTemplates();

        byte[][] templatesArray = (byte[][]) templatesData.get("TemplatesArray");
        int[] templatesSize = (int[]) templatesData.get("TemplatesSize");
        int[] employeeIdsArray = (int[]) templatesData.get("EmployeeIdsArray");

        int[] result = new int[1];
        int nRes = sdk.UFM_IdentifyMT(hMatcher[0], bTemplate, bTemplate.length, templatesArray, templatesSize, templatesArray.length, 60000, result);

        if (nRes == 0 && result[0] != -1) {
            int index = result[0];
            if (index != -1) {
                return employeeIdsArray[index];
            }
        }

        return nRes;
    }

    public byte[] extractByteTemplate(byte[] image) {

        byte[] pImage = loadImageFromBMPBuffer(image);
        byte[] pTemplate = new byte[this.MAX_TEMPLATE_SIZE];
        int[] pnTemplateSize = new int[1];
        int[] pnEnrollQuality = new int[1];

        int result = sdk.UFE_Extract(hExtractorContainer[0], pImage, this.IMG_WIDTH, this.IMG_HEIGHT, this.IMG_RESOLUTION, pTemplate, pnTemplateSize, pnEnrollQuality);

        byte[] template = pImage = new byte[pnTemplateSize[0]];

        if (result == sdk.UFE_OK) {
            template = Arrays.copyOf(pTemplate, pnTemplateSize[0]);
        }

        return template;

    }

    public String extractBase64Template(byte[] image) {
        byte[] template = extractByteTemplate(image);
        return Base64.getEncoder().encodeToString(template);
    }

    public void setExtractedTemplates(Employee employee){
        String extractedTemplate1 = extractBase64Template(employee.getImgTemplate1());
        String extractedTemplate2 = extractBase64Template(employee.getImgTemplate1());
        employee.setTemplate1(extractedTemplate1);
        employee.setTemplate2(extractedTemplate2);
    }

    public Map<String, Object> getTemplates() {

        Iterable<Employee> employees = repository.findAll();

        int count = ((Collection<?>) employees).size() * 2;

        byte[][] templatesArray = new byte[count][this.MAX_TEMPLATE_SIZE];
        int[] templatesSize = new int[count];
        int[] employeeIdsArray = new int[count];

        int index = count - 1;
        for (Employee employee : employees) {

            byte[] template1 = employee.getImgTemplate1();
            templatesArray[index] = template1;
            templatesSize[index] = template1.length;
            employeeIdsArray[index] = employee.getEmployeeId();
            index--;

            byte[] template2 = employee.getImgTemplate2();
            templatesArray[index] = template2;
            templatesSize[index] = template2.length;
            employeeIdsArray[index] = employee.getEmployeeId();
            index--;
        }


        Map templatesData = new HashMap();
        templatesData.put("TemplatesArray", templatesArray);
        templatesData.put("TemplatesSize", templatesSize);
        templatesData.put("EmployeeIdsArray", employeeIdsArray);

        return templatesData;
    }

    public void extractTemplate(byte[] imageBuffer, int index, byte[][] outTemplate, int[] outSize) {

        byte[] pImage = loadImageFromBMPBuffer(imageBuffer);

        byte[] pTemplate = new byte[this.MAX_TEMPLATE_SIZE];
        int[] pnTemplateSize = new int[1];
        int[] pnEnrollQuality = new int[1];

        int result = sdk.UFE_Extract(hExtractorContainer[0], pImage, this.IMG_WIDTH, this.IMG_HEIGHT, this.IMG_RESOLUTION, pTemplate, pnTemplateSize, pnEnrollQuality);

        if (result == sdk.UFE_OK) {
            outTemplate[index] = pTemplate;
            outSize[index] = pnTemplateSize[0];
        }

    }

    public byte[] loadImageFromBMPBuffer(byte[] imageBuffer) {

        byte[] pImage = new byte[this.IMG_WIDTH * this.IMG_HEIGHT];//null;
        int[] nWidth = new int[1];
        int[] nHeight = new int[1];

        int result = sdk.UFE_LoadImageFromBMPBuffer(imageBuffer, imageBuffer.length, pImage, nWidth, nHeight);

        return pImage;
    }
}
