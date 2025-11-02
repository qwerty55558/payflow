package com.fds.payflow;

import com.fds.payflow.utils.AccountFactory;
import com.fds.payflow.utils.SimpleEncoder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class PayflowApplication {
	public static void main(String[] args) {
        SpringApplication.run(PayflowApplication.class, args);
	}
}
