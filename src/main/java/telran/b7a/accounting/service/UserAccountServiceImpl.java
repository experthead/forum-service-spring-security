package telran.b7a.accounting.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import telran.b7a.accounting.dao.UserAccountRepository;
import telran.b7a.accounting.dto.RolesResponseDto;
import telran.b7a.accounting.dto.UserAccountResponseDto;
import telran.b7a.accounting.dto.UserRegisterDto;
import telran.b7a.accounting.dto.UserUpdateDto;
import telran.b7a.accounting.dto.exceptions.UserExistsException;
import telran.b7a.accounting.dto.exceptions.UserNotFoundException;
import telran.b7a.accounting.model.UserAccount;

@Service
public class UserAccountServiceImpl implements UserAccountService {
	
	UserAccountRepository repository;
	ModelMapper modelMapper;
	PasswordEncoder passwordEncoder;

	@Autowired
	public UserAccountServiceImpl(UserAccountRepository repository, ModelMapper modelMapper, PasswordEncoder passwordEncoder) {
		this.repository = repository;
		this.modelMapper = modelMapper;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public UserAccountResponseDto addUser(UserRegisterDto userRegisterDto) {
		if (repository.existsById(userRegisterDto.getLogin())) {
			throw new UserExistsException(userRegisterDto.getLogin());
		}
		UserAccount userAccount = modelMapper.map(userRegisterDto, UserAccount.class);
		userAccount.addRole("USER".toUpperCase());
		userAccount.setPasswordExpDate(LocalDate.now().plusMonths(1));
		
		String password = passwordEncoder.encode(userRegisterDto.getPassword());
		userAccount.setPassword(password );
		repository.save(userAccount);
		return modelMapper.map(userAccount, UserAccountResponseDto.class);
	}

	@Override
	public UserAccountResponseDto getUser(String login) {
		UserAccount userAccount = repository.findById(login)
				.orElseThrow(() -> new UserNotFoundException(login));
		return modelMapper.map(userAccount, UserAccountResponseDto.class);
	}

	@Override
	public UserAccountResponseDto removeUser(String login) {
		UserAccount userAccount = repository.findById(login).orElseThrow(() -> new UserNotFoundException(login));
		repository.deleteById(login);
		return modelMapper.map(userAccount, UserAccountResponseDto.class);
	}

	@Override
	public UserAccountResponseDto editUser(String login, UserUpdateDto userUpdateDto) {
		UserAccount userAccount = repository.findById(login).orElseThrow(() -> new UserNotFoundException(login));
		if (userUpdateDto.getFirstName() != null) {
			userAccount.setFirstName(userUpdateDto.getFirstName());
		}
		if (userUpdateDto.getLastName() != null) {
			userAccount.setLastName(userUpdateDto.getLastName());
		}
		repository.save(userAccount);
		return modelMapper.map(userAccount, UserAccountResponseDto.class);
	}

	@Override
	public RolesResponseDto changeRolesList(String login, String role, boolean isAddRole) {
		UserAccount userAccount = repository.findById(login).orElseThrow(() -> new UserNotFoundException(login));
		boolean res;
		if (isAddRole) {
			res = userAccount.addRole(role.toUpperCase());
		} else {
			res = userAccount.removeRole(role.toUpperCase());
		}
		if (res) {
			repository.save(userAccount);
		}
		
		return modelMapper.map(userAccount, RolesResponseDto.class);
	}

	@Override
	public void changePassword(String login, String password) {
		UserAccount userAccount = repository.findById(login).orElseThrow(() -> new UserNotFoundException(login));
		userAccount.setPassword(passwordEncoder.encode(password));
		userAccount.setPasswordExpDate(LocalDate.now().plusMonths(1));
		repository.save(userAccount);

	}

}
