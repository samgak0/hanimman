package org.devkirby.hanimman;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.TimeZone;

@EnableScheduling
@SpringBootApplication
public class HanimmanApplication {

	public static void main(String[] args) {
		SpringApplication.run(HanimmanApplication.class, args);
	}
}
