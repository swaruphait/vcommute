package com.tecsoft.vcommute;

import java.util.TimeZone;

import javax.annotation.PostConstruct;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.tecsoft.vcommute.audit.SpringSecurityAuditorAware;

@SpringBootApplication
@EnableAsync
@EnableScheduling
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class VcommuteApplication {

	public static void main(String[] args) {
		SpringApplication.run(VcommuteApplication.class, args);
	}

	@Bean
	public AuditorAware<String> auditorAware() {
		return new SpringSecurityAuditorAware();
	}

	@PostConstruct
	void init() {
		String systemTimeZone = TimeZone.getDefault().getID();
		TimeZone.setDefault(TimeZone.getTimeZone(systemTimeZone));
	}

}
