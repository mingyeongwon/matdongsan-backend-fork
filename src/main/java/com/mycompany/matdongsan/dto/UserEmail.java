package com.mycompany.matdongsan.dto;

import lombok.Data;

@Data
public class UserEmail {
   private int unumber;
   private String uemail;
   private String urole;
   private String upassword;
   private boolean uremoved;
}
