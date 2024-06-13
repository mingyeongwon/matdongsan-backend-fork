package com.mycompany.matdongsan.dto;

import java.sql.Date;

import lombok.Data;

@Data
public class Agent_Review {
   private int arnumber;
   private String arcontent;
   private int arrate;
   private Date ardate;
   private String ar_aemail;
   private String ar_memail;
}

