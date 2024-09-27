package voting.dapp.vote;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class VoteApplication {
	public static void main(String[] args) {
		SpringApplication.run(VoteApplication.class, args);
		/*try {
			App.main(args);
		} catch (Exception e) {
			System.out.println("Error Found Voting Gateway" + e);
		}*/

	}

}
