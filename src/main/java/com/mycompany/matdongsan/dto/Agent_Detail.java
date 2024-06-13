package com.mycompany.matdongsan.dto;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class Agent_Detail {
   private int adnumber;
   private String adname;
   private String adattachoname;
   private byte[] adattachdata;
   private String adattachtype;
   private String adbrandnumber;
   private String ad_aemail;
   
   private MultipartFile adattach;
}
