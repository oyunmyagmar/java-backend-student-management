package com.example.studentManagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class StudentManagementApplication {

	public static void main(String[] args) {
		try {
			SpringApplication.run(StudentManagementApplication.class, args);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
