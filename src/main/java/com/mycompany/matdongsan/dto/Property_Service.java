package com.mycompany.matdongsan.dto;

import java.sql.Date;

import lombok.Data;

@Data
public class Property_Service {
   private int psnumber;
   private int psprice;
   private Date psdate;
   private int psquantity;
   private boolean psroletype;
   private String ps_email;
}

