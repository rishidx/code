package in.rajarshi.file.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * The Class FileDownloadApp.
 *
 * @author Rajarshi Chakrabarty
 * @since 16-05-2020
 */
@SpringBootApplication
@ComponentScan("in.rajarshi.file")
@EnableJpaRepositories("in.rajarshi.file.repository")
@EntityScan("in.rajarshi.file.model")
public class FileApp {
	
	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		SpringApplication.run(FileApp.class, args);
	}
	
}
