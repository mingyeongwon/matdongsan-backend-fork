package com.mycompany.matdongsan.dto;

import java.util.Date;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class Property {
   private int pnumber;
   private int pdeposite;
   private int prentalfee;
   private String pthumbnailoname;
   private byte[] pthumbnaildata;
   private String pthumbnailtype;
   private String pfloortype;
   private String pfloor;
   private int psize;
   private int pmaintenance;
   private int phitcount;
   private Date pdate;
   private String pstatus;
   private String ptitle;
   private String pcategory;
   private boolean proletype;
   private String pEmail;

   private MultipartFile pthumbnail;
}

