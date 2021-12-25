package telran.b7a.accounting.model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(of = { "login" })
@Document(collection = "users")
@NoArgsConstructor
public class UserAccount {
	@Id
	String login;
	String password;
	String firstName;
	String lastName;
	LocalDate passwordExpDate;
	Set<String> roles = new HashSet<>();
	
	public UserAccount(String login, String password, String firstName, String lastName) {
		this.login = login;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		
	}

	public boolean addRole(String role) {
		return roles.add(role);
	}

	public boolean removeRole(String role) {
		return roles.remove(role);
	}

}
