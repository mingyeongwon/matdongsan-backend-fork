package com.mycompany.matdongsan.dto;

import java.util.Date;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class Question {
   private int qnumber;
   private String qcategory;
   private String qtitle;
   private String qattachoname;
   private byte[] qattachdata;
   private String qattachtype;
   private Date qdate;
   private boolean qroletype;
   private String q_email;
   
   private MultipartFile qattach;
}
