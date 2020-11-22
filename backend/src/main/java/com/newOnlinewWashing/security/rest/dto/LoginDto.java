package com.newOnlinewWashing.security.rest.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * DTO for storing a user's credentials.
 */
public class LoginDto {

   @NotNull
   @Size(min = 1, max = 50)
   private String login;

   @NotNull
   @Size(min = 4, max = 100)
   private String pwd;

   private Boolean rememberMe;

   public String getLogin() {
      return login;
   }

   public void setLogin(String login) {
      this.login = login;
   }

   public String getPwd() {
      return pwd;
   }

   public void setPwd(String pwd) {
      this.pwd = pwd;
   }

   public Boolean isRememberMe() {
      return rememberMe;
   }

   public void setRememberMe(Boolean rememberMe) {
      this.rememberMe = rememberMe;
   }

   public LoginDto(@NotNull @Size(min = 1, max = 50) String username, @NotNull @Size(min = 4, max = 100) String password) {
      this.login = username;
      this.pwd = password;
   }

   @Override
   public String toString() {
      return "LoginVM{" +
         "username='" + login + '\'' +
         ", rememberMe=" + rememberMe +
         '}';
   }
}
