package com.mycompany.matdongsan.service;

import com.mycompany.matdongsan.dto.SmsRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SmsService {
    @Value("${coolsms.apikey}")
    private String apiKey;

    @Value("${coolsms.apisecret}")
    private String apiSecret;

    @Value("${coolsms.fromNumber}")
    private String fromNumber;

    public void sendSms(SmsRequest requestDto) {

    }
}
