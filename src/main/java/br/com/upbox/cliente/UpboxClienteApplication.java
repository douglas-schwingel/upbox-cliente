package br.com.upbox.cliente;

import br.com.upbox.controller.UsuarioController;
import br.com.upbox.ftp.FtpConnectionFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackageClasses = {UsuarioController.class, WebConfig.class, FtpConnectionFactory.class})
public class UpboxClienteApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(UpboxClienteApplication.class, args);
	}


	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(UpboxClienteApplication.class);
	}

}
