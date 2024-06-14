package com.mycompany.matdongsan.dto;

import java.sql.Date;

import lombok.Data;

@Data
public class Report {
   private int rnumber;
   private String rcontent;
   private Date rdate;
   private boolean rroletype;
   private String rEmail;
   private int rPnumber;
}

