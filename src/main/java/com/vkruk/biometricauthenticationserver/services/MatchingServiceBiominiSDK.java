package com.vkruk.biometricauthenticationserver.services;

import com.suprema.ImageSDK;
import com.vkruk.biometricauthenticationserver.models.Employee;
import com.vkruk.biometricauthenticationserver.repository.EmployeeRepository;
import jdk.nashorn.internal.ir.IfNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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

    private static final Logger LOGGER = LoggerFactory.getLogger(MatchingServiceBiominiSDK.class);

    @Autowired
    public MatchingServiceBiominiSDK(EmployeeRepository repository) {

        this.repository = repository;
        this.sdk = new ImageSDK();

        sdk.UFE_Create(hExtractorContainer);
        sdk.UFM_Create(hMatcher);

    }

    @Override
    public int identify(String template) throws Exception {

        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss.SSS");
        Date date = new Date();
        String time = dateFormat.format(date);

        LOGGER.info(time+"- Start...");

        byte[] bTemplate = Base64.getDecoder().decode(template);

        Map<String, Object> templatesData = getTemplates();

        byte[][] templatesArray = (byte[][]) templatesData.get("TemplatesArray");
        int[] templatesSize = (int[]) templatesData.get("TemplatesSize");
        int[] employeeIdsArray = (int[]) templatesData.get("EmployeeIdsArray");

        int[] result = new int[1];
        int nRes = sdk.UFM_IdentifyMT(hMatcher[0], bTemplate, bTemplate.length, templatesArray, templatesSize, templatesArray.length, 60000, result);

        LOGGER.info(time+" - Stop...");

        int employeeId = 0;
        if (nRes == 0) {
            if (result[0] != -1) {
                int index = result[0];
                employeeId = employeeIdsArray[index];
            }else{
                throw new Exception("Employee not found!");
            }
        }else {
            byte[] msg = new byte[30];
            sdk.UFE_GetErrorString(nRes,msg);
            throw new Exception(new String(msg));
        }

        return employeeId;
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
        String extractedTemplate0 = extractBase64Template(employee.imgTemplate0());
        String extractedTemplate1 = extractBase64Template(employee.imgTemplate1());
        employee.setTemplate0(extractedTemplate0);
        employee.setTemplate1(extractedTemplate1);
    }

    public Map<String, Object> getTemplates() {

        Iterable<Employee> employees = repository.findAll();

        int count = ((Collection<?>) employees).size() * 2;

        byte[][] templatesArray = new byte[count][this.MAX_TEMPLATE_SIZE];
        int[] templatesSize = new int[count];
        int[] employeeIdsArray = new int[count];

        int index = count - 1;
        for (Employee employee : employees) {

            byte[] template0 = employee.imgTemplate0();
            templatesArray[index] = template0;
            templatesSize[index] = template0.length;
            employeeIdsArray[index] = employee.getEmployeeId();
            index--;

            byte[] template1 = employee.imgTemplate1();
            templatesArray[index] = template1;
            templatesSize[index] = template1.length;
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
