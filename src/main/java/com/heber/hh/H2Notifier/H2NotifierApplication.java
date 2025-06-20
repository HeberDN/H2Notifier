package com.heber.hh.H2Notifier;

import com.heber.hh.H2Notifier.configurations.TwilioProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableConfigurationProperties(TwilioProperties.class)
@EnableScheduling
@SpringBootApplication
public class H2NotifierApplication {

	public static void main(String[] args) {
		SpringApplication.run(H2NotifierApplication.class, args);
	}

}
