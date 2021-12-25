package telran.b7a.security.service;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import telran.b7a.accounting.dao.UserAccountRepository;
import telran.b7a.accounting.model.UserAccount;
import telran.b7a.forum.dao.PostRepository;
import telran.b7a.forum.model.Post;

@Service("customSecurity")
public class CustomWebSecurity {

	PostRepository repository;
	UserAccountRepository userRepository;

	@Autowired
	public CustomWebSecurity(PostRepository repository, UserAccountRepository userRepository) {
		this.repository = repository;
		this.userRepository = userRepository;
	}

	public boolean checkPostAuthority(String postId, String userName) {
		Post post = repository.findById(postId).orElse(null);
		return post != null && userName.equals(post.getAuthor());
	}

	public boolean isPasswordExpired(String userName) {
		UserAccount userAccount = userRepository.findById(userName).orElse(null);
		return userAccount != null && LocalDate.now().isBefore(userAccount.getPasswordExpDate());
	}

}
