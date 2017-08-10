package com.qt.caesar;

import de.codecentric.boot.admin.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableAdminServer
@EnableAsync
@EnableScheduling
@SpringBootApplication
public class CaesarApplication /*extends SpringBootServletInitializer*/ {

	/**
	 * 打war包
	 * @param builder
	 * @return
     */
//	@Override
//	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
//		return builder.sources(CaesarApplication.class);
//	}

	public static void main(String[] args) {
		SpringApplication.run(CaesarApplication.class, args);
	}
}
