package com.mycompany.matdongsan.dto;

import java.sql.Date;

import lombok.Data;

@Data
public class Report {
   private int rnumber;
   private String rcontent;
   private Date rdate;
   private boolean rroletype;
   private String r_email;
   private int r_pnumber;
}

