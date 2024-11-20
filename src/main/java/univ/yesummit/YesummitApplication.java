package univ.yesummit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class YesummitApplication {

	public static void main(String[] args) {
		SpringApplication.run(YesummitApplication.class, args);
	}

}
