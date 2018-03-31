package com.vkruk.biometricauthenticationserver.services;

import com.suprema.ImageSDK;
import com.vkruk.biometricauthenticationserver.models.Employee;
import com.vkruk.biometricauthenticationserver.models.Template;
import com.vkruk.biometricauthenticationserver.repository.EmployeeRepository;
import jdk.nashorn.internal.ir.IfNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static sun.security.krb5.KrbException.errorMessage;

/**
 * Created by Vova on 10.03.2018.
 */
@Service
public class MatchingServiceBiominiSDK implements MatchingService {

    final int MAX_TEMPLATE_SIZE = 384;
    final int IMG_RESOLUTION = 500;

    private ImageSDK sdk;
    private long[] hMatcher = new long[1];
    private long[] hExtractorContainer = new long[1];

    private final EmployeeRepository repository;

    private static final Logger LOGGER = LoggerFactory.getLogger(MatchingServiceBiominiSDK.class);

    private byte[][] templatesArray;
    private int[] templatesSize;
    private int[] employeeIdsArray;

    @Autowired
    public MatchingServiceBiominiSDK(EmployeeRepository repository) {

        this.repository = repository;
        this.sdk = new ImageSDK();

        createExtractor();
        createMatcher();

        this.prepareTemplates();

    }

    @Override
    public int identify(String template) throws Exception {

        if(hMatcher[0] == 0){
            String err = getErrorMessage(sdk.UFE_ERR_NO_LICENSE);
            LOGGER.info("Identification -> "+err);
            throw new Exception(err);
        }

        byte[] bTemplate = Base64.getDecoder().decode(template);

        int[] result = new int[1];
        int nRes = sdk.UFM_IdentifyMT(hMatcher[0], bTemplate, bTemplate.length, templatesArray, templatesSize, templatesArray.length, 60000, result);

        int employeeId = 0;
        if (nRes == sdk.UFE_OK) {
            if (result[0] != -1) {
                int index = result[0];
                employeeId = employeeIdsArray[index];
                LOGGER.info("Identification -> Success "+employeeId);
            }else{
                LOGGER.info("Identification -> Fail "+template);
                throw new Exception("Employee not found!");
            }
        }else {
            String err = getErrorMessage(nRes);
            LOGGER.info("Identification -> "+err);
            throw new Exception(err);
        }

        return employeeId;
    }

    @Override
    public String extractBase64Template(byte[] image) {
        byte[] template = extractByteTemplate(image);
        return Base64.getEncoder().encodeToString(template);
    }

    @Override
    public boolean compareTemplates(String template1,String template2) {

        if(hMatcher[0] == 0){
            LOGGER.info("Comparison -> "+getErrorMessage(sdk.UFE_ERR_NO_LICENSE));
            return false;
        }

        byte[] bTemplate1 = Base64.getDecoder().decode(template1);
        byte[] bTemplate2 = Base64.getDecoder().decode(template2);

        int[] result = new int[1];
        int nRes = sdk.UFM_Verify(hMatcher[0],bTemplate1,bTemplate1.length,bTemplate2,bTemplate2.length,result);

        if(nRes != sdk.UFE_OK){
            LOGGER.info("Comparison -> "+getErrorMessage(nRes));
        }

        return  (nRes == 0 && result[0] == 1);

    }

    public void prepareTemplates(){

        Iterable<Employee> employees = repository.findAll();

        int count = ((Collection<?>) employees).size() * 2;

        this.templatesArray = new byte[count][this.MAX_TEMPLATE_SIZE];
        this.templatesSize = new int[count];
        this.employeeIdsArray = new int[count];

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

    }

    public byte[] extractByteTemplate(byte[] image) {

        int IMG_WIDTH, IMG_HEIGHT;

        try {
            InputStream inp = new ByteArrayInputStream(image);
            BufferedImage img = ImageIO.read(inp);
            IMG_WIDTH = img.getWidth();
            IMG_HEIGHT = img.getHeight();
        } catch (IOException e) {
            IMG_WIDTH = 300;
            IMG_HEIGHT = 400;
        }

        byte[] pImage = loadImageFromBMPBuffer(image, IMG_WIDTH, IMG_HEIGHT);
        byte[] pTemplate = new byte[this.MAX_TEMPLATE_SIZE];
        int[] pnTemplateSize = new int[1];
        int[] pnEnrollQuality = new int[1];

        int result = sdk.UFE_Extract(hExtractorContainer[0], pImage, IMG_WIDTH, IMG_HEIGHT, this.IMG_RESOLUTION, pTemplate, pnTemplateSize, pnEnrollQuality);

        byte[] template = pImage = new byte[pnTemplateSize[0]];

        if (result == sdk.UFE_OK) {
            template = Arrays.copyOf(pTemplate, pnTemplateSize[0]);
        }else{
            LOGGER.info("Extracting -> "+getErrorMessage(result));
        }

        return template;

    }

    public byte[] loadImageFromBMPBuffer(byte[] imageBuffer, int IMG_WIDTH, int IMG_HEIGHT) {

        byte[] pImage = new byte[IMG_WIDTH * IMG_HEIGHT];
        int[] nWidth = new int[1];
        int[] nHeight = new int[1];

        int result = sdk.UFE_LoadImageFromBMPBuffer(imageBuffer, imageBuffer.length, pImage, nWidth, nHeight);

        if(result != sdk.UFE_OK){
            LOGGER.info("Loading from BMP -> "+getErrorMessage(result));
        }

        return pImage;
    }

    public void createExtractor(){

        int resExtractor = sdk.UFE_Create(hExtractorContainer);

        String errMsg = getErrorMessage(resExtractor);

        LOGGER.info( errMsg.isEmpty() ? "Extractor created" : errMsg);

    }

    public void createMatcher() {

        int resMatcher = sdk.UFM_Create(hMatcher);

        String errMsg = getErrorMessage(resMatcher);

        LOGGER.info( errMsg.isEmpty() ? "Matcher created" : errMsg);

    }

    public String getErrorMessage(int result){
        String errMsg = "";
        if(result != sdk.UFE_OK){
            byte[] msg = new byte[150];
            sdk.UFE_GetErrorString(result, msg);
            errMsg = new String(msg);
            LOGGER.info(errMsg);
        }
        return errMsg.trim();
    }

}
