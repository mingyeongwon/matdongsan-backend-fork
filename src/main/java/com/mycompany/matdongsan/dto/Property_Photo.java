package com.mycompany.matdongsan.dto;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class Property_Photo {
   private int ppnumber;
   private String ppattachoname;
   private byte[] ppattachdata;
   private String ppattachtype;
   private int pp_pnumber;

   private MultipartFile ppattach;
}

