package com.callcenter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

import com.callcenter.conf.AsyncConf;

@SpringBootApplication
@Import(value = { 
		AsyncConf.class
})
public class PruebaCallCenterApplication{


	public static void main(String[] args) {
		SpringApplication.run(PruebaCallCenterApplication.class, args);
	}
}
