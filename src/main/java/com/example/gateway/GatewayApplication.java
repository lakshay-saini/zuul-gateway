package com.example.gateway;

import com.example.gateway.bean.auth.Role;
import com.example.gateway.bean.auth.User;
import com.example.gateway.security.UserService;
import com.google.common.collect.Sets;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.ConfigurableApplicationContext;

@EnableZuulProxy
@EnableEurekaServer
@SpringBootApplication
public class GatewayApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext applicationContext = SpringApplication.run(GatewayApplication.class, args);
		UserService userService = applicationContext.getBean(UserService.class);
		try {
			userService.loadUserByUsername("test@test.com");
		} catch (Exception e) {
			if (e.getMessage().contains("Invalid username or password")) {
				User newUser = new User();
				newUser.setEmail("test@test.com");
				newUser.setName("test@test.com");
				newUser.setPassword("test");
				Role userRole = new Role();
				userRole.setId(1);
				userRole.setRole("USER");
				Role adminRole = new Role();
				adminRole.setId(2);
				adminRole.setRole("ADMIN");
				newUser.setRole(Sets.newHashSet(userRole, adminRole));
				userService.save(newUser);
			}
		}
	}
}
