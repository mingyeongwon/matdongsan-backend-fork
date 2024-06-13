package com.mycompany.matdongsan.dto;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class Member {
   private String memail;
   private String mname;
   private String mprofileoname;
   private byte[] mprofiledata;
   private String mprofiletype;
   private String mphone;
   private String mpassword;
   private boolean mremoved;
   
   private MultipartFile mprofile;

}
