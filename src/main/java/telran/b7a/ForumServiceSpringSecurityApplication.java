package telran.b7a;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

import telran.b7a.accounting.dao.UserAccountRepository;
import telran.b7a.accounting.model.UserAccount;

@SpringBootApplication
public class ForumServiceSpringSecurityApplication implements CommandLineRunner{
	
	@Autowired
	UserAccountRepository repository;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	public static void main(String[] args) {
		SpringApplication.run(ForumServiceSpringSecurityApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		if(!repository.existsById("admin")) {
			String password = passwordEncoder.encode("admin");
			UserAccount userAccount = new UserAccount("admin", password, "", "");
			userAccount.addRole("USER".toUpperCase());
			userAccount.addRole("MODERATOR".toUpperCase());
			userAccount.addRole("ADMINISTRATOR".toUpperCase());
			userAccount.setPasswordExpDate(LocalDate.of(2099, 12, 31));
			repository.save(userAccount);
		}
		
	}

}
