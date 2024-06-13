package com.mycompany.matdongsan.dto;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class Agent {
   private String aemail;
   private String abrand;
   private String aaddress;
   private String aprofileoname;
   private byte[] aprofiledata;
   private String aprofiletype;
   private String aphone;
   private String apassword;
   private boolean aremoved;
   private String alongitude;
   private String alatitude;
   
   private MultipartFile aprofile;
}
