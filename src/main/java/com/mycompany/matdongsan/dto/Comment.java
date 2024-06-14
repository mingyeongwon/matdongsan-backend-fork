package com.mycompany.matdongsan.dto;

import java.sql.Date;

import lombok.Data;

@Data
public class Comment {
   private int cnumber;
   private String ccomment;
   private Date cdate;
   private int cparentnumber;
   private boolean croletype;
   private String cEmail;
   private int cPnumber;
}

