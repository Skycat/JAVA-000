package io.github.skycat.geekstudy.httpserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * NettyGatewayApplication
 * @author Skycat
 * @email skycat2004@gmail.com
 * @version 1.0 2020-09-03 00:34:46
 */
@SpringBootApplication
public class NettyHttpServerApplication {
    /**
     * main
     * @param args
     */
	public static void main(String[] args) {
		SpringApplication.run(NettyHttpServerApplication.class, args);
	}
}
