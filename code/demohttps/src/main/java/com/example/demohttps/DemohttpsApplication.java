package com.example.demohttps;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class DemohttpsApplication  {

	public static void main(String[] args) {
		SpringApplication.run(DemohttpsApplication.class, args);
	}
	//keytool -genkeypair -alias tomcat -keyalg RSA -keysize 2048 -storetype PKCS12 -keystore keystore.p12 -validity 3650
	//Enter keystore password: 123456
	//Re-enter new password:123456
	//What is your first and last name?
	//  [Unknown]:  java master
	//What is the name of your organizational unit?
	//  [Unknown]:  javamaster
	//What is the name of your organization?
	//  [Unknown]:  kiev
	//What is the name of your City or Locality?
	//  [Unknown]:  kiev
	//What is the name of your State or Province?
	//  [Unknown]:  ua
	//What is the two-letter country code for this unit?
	//  [Unknown]:  ua
	//Is CN=java master, OU=javamaster, O=kiev, L=kiev, ST=ua, C=ua correct?
	//  [no]:  yes
}
