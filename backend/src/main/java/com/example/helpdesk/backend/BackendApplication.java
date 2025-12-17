package com.example.helpdesk.backend;

import com.example.helpdesk.backend.constant.Department;
import com.example.helpdesk.backend.constant.Role;
import com.example.helpdesk.backend.model.User;
import com.example.helpdesk.backend.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class BackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}

	@Bean
	public CommandLineRunner seedDefaultAdmin(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		return args -> {
			final String adminEmail = "admin@helpdesk.co.id";
			final String adminRawPassword = "Admin@12345";

			if (!userRepository.existsByEmail(adminEmail)) {
				User admin = User.builder()
						.email(adminEmail)
						.name("Administrator")
						.password(passwordEncoder.encode(adminRawPassword))
						.role(Role.ADMIN)
						.department(Department.SUPERADMIN)
						.phoneNumber("0000000000")
						.isApproved(true)
						.isDeleted(false)
						.build();
				userRepository.save(admin);
				System.out.println("Default admin created: " + adminEmail + " (password: " + adminRawPassword + ")");
			} else {
				System.out.println("Default admin already exists: " + adminEmail);
			}
		};
	}

}
