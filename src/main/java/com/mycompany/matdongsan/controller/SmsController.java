package com.mycompany.matdongsan.controller;

import net.nurigo.sdk.NurigoApp;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/sms")
public class SmsController {

    @Value("${coolsms.apiKey}")
    private String apiKey;
    @Value("${coolsms.apisecret}")
    private String apisecret;
    @Value("${coolsms.fromnumber}")
    private String fromnumber;

}
