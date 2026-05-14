package com.mini_wallet_api.demo;

import com.mini_wallet_api.demo.service.walletservice;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		var context = SpringApplication.run(DemoApplication.class, args);

		var service = context.getBean(walletservice.class);

	}
}