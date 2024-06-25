package com.mycompany.matdongsan.security;

import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import com.mycompany.matdongsan.dto.UserEmail;

public class AppUserDetails extends User {
   private UserEmail userEmail;

   public AppUserDetails(UserEmail userEmail, List<GrantedAuthority> authorities) {
      super(userEmail.getUemail(), userEmail.getUpassword(), userEmail.isUremoved(), true, true, true, authorities);
      this.userEmail = userEmail;
   }

   public UserEmail getUser() {
      return userEmail;
   }
}
