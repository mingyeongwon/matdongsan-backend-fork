package com.mycompany.matdongsan.dto;

import java.sql.Date;

import lombok.Data;

@Data
public class PropertyService {
   private int psnumber;
   private int psprice;
   private Date psdate;
   private int psquantity;
   private int psUnumber;
}

