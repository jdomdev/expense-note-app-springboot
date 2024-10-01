package com.johnsunday.app.security.jwt;

import java.util.Date;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.johnsunday.app.dao.IEmployeeDao;
import com.johnsunday.app.entity.Employee;
import com.johnsunday.app.security.dao.IUserDao;
import com.johnsunday.app.security.entity.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

@Component
public class JwtAuthenticationUtil {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(JwtAuthenticationUtil.class);
	//private static final Logger LOGGER = LoggerFactory.getLogger(JwtTokenUtil.class);
	private static final long EXPIRE_DURATION = 24 * 60 * 60 * 1000;// 24h
	@Autowired private IUserDao userDao;
	
	@Value("${app.jwt.secret}")
	private String secretKey;
	
	public String generateAccessToken(User user) {		
		return Jwts.builder()
				.setSubject(user.getId() + "," + user.getEmail())
				.claim("roles", user.getRoles().toString())
				.setIssuer("JohnSunday")
				.setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + EXPIRE_DURATION))
				.signWith(SignatureAlgorithm.HS512, secretKey)
				.compact();
	}
	public boolean validateAccessToken(String token) {
		boolean isValidated = false;
		try {
			Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
			isValidated = true;
		}catch(ExpiredJwtException ex) {
			ex.printStackTrace();
			LOGGER.error("jwt expired",ex);
		}catch(IllegalArgumentException ex) {
			ex.printStackTrace();
			LOGGER.error("Token is null, empty or has only whitespace",ex);
		}catch(MalformedJwtException ex) {
			LOGGER.error("JWT is invalid",ex);
		}catch(UnsupportedJwtException ex) {
			ex.printStackTrace();
			LOGGER.error("JWT is not supported",ex);
		}catch(SignatureException ex) {
			ex.printStackTrace();
			LOGGER.error("Signature validation failed",ex);
		}
		return isValidated;
	}
	public String getSubject(String token) {
		return parseClaims(token)
				.getSubject();
	}
	public Claims parseClaims(String token) {
		return Jwts.parser()
				.setSigningKey(secretKey)
				.parseClaimsJws(token)
				.getBody();				
	}
	public Boolean isAdminTokenUser(String token) {
		boolean isAdmin = false;
		Claims claims = null;
		claims = parseClaims(token);
		String claimRoles = (String)claims.get("roles");		
		claimRoles = claimRoles.replace("[", "").replace("]", "");
		String[]roleNames =  claimRoles.split(",");
		for (String role:roleNames) {
			if (role.equalsIgnoreCase("ROLE_ADMIN")) {
				isAdmin = true;
				break;
			}
		}
		return isAdmin;
	}
//	public Boolean matchEmail(Employee expenseEmployee) throws Exception {
//		boolean isMatch = false;
//		int tokenUserId = extractTokenUserId();		
//		Optional<User> optTokenUser = userDao.findById(tokenUserId);		
//		if(optTokenUser.get().getEmail().equalsIgnoreCase(expenseEmployee.getEmail())) isMatch = true;
//		else throw new Exception("ERROR -> The token user email DOESN'T MATCH with the expense employee email.");
//		return isMatch;
//	}
	public Integer extractTokenUserId(String token) {
		Claims claims = null;
		claims = parseClaims(token);
		String subject = (String)claims.get(Claims.SUBJECT);		
		String[]subjectArray = subject.split(",");
		return Integer.parseInt(subjectArray[0]);
	}
}
