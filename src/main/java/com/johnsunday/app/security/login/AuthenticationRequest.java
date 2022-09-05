package com.johnsunday.app.security.login;

import javax.validation.constraints.Email;

import org.hibernate.validator.constraints.Length;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class AuthenticationRequest {
	@Email @Length(min=3,max=50)
	private String email;
	@Length(min=5,max=20)
	private String password;
}
