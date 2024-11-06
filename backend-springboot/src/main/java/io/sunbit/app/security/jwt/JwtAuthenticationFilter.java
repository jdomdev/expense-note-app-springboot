package io.sunbit.app.security.jwt;

import java.io.IOException;
import java.util.Collection;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import io.sunbit.app.security.entity.ExpenseUser;
import io.sunbit.app.security.entity.Role;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	@Autowired
	private JwtAuthenticationUtil jwtAuthUtil;
	// @Autowired private IUserDao userDao;
	private ExpenseUser tokenUserDetails = new ExpenseUser();

	@Override
	protected void doFilterInternal(HttpServletRequest request,
			HttpServletResponse response,
			FilterChain filterChain)
			throws ServletException, IOException {
		if (!hasAuthorizationHeader(request)) {
			System.out.println("It hasn't header authorization");
			filterChain.doFilter(request, response);
			return;
		}
		String accessToken = getAccessToken(request);
		if (!jwtAuthUtil.validateAccessToken(accessToken)) {
			System.out.println("Token NOT valid");
			filterChain.doFilter(request, response);
			return;
		}
		// TODO ---> To finish the method which compare the id's(id Token and id URL
		// parameter)
		setAuthenticationContext(accessToken, request);
		// System.out.println("JWT Query String: " + request.getQueryString());// print
		// param
		// System.out.println("JWT Header Names Test: " + request.getHeaderNames());
		// Enumeration<String>headerNames = request.getHeaderNames();
		// //Iterator<String>itNames = headerNames.asIterator();
		// headerNames.asIterator().forEachRemaining(n->System.out.println(n));

		// for (Enumeration<String>e = .elements(); e.hasMoreElements();)
		// System.out.println(e.nextElement());

		// System.out.println("JWT Body Request: ");
		// BufferedReader buffReader = request.getReader();
		// String line;
		// while((line = buffReader.readLine()) != null) {
		// System.out.println(line); // Display the file's contents on the screen, one
		// line at a time
		// }

		// System.out.println("Remote User Request: " +
		// request.getRemoteUser());//->null
		// System.out.println("Principal User Request: " +
		// request.getUserPrincipal());//->null

		// Integer requestUserId =
		// Integer.parseInt(request.getParameter("requestUserId"));

		boolean isAdmin = false;
		Collection<? extends GrantedAuthority> grantedAuths = tokenUserDetails.getAuthorities();
		for (GrantedAuthority auth : grantedAuths) {
			String strAuth = auth.getAuthority();
			// Test.
			// System.out.println("String Authority: " + strAuth);
			if (strAuth.equalsIgnoreCase("ROLE_ADMIN")) {
				isAdmin = true;
				break;
			}
		}
		/*
		 * If the user hasn't role Admin, and the id user from token doesn't match with
		 * the request param 'requestUserId', we return the flow.
		 */
		// if (!isAdmin) {
		// if
		// (tokenUserDetails.getId()!=Integer.parseInt(request.getParameter("userId"))){
		// return;
		// }
		// }

		System.out.println("Method Request: " + request.getMethod());
		System.out.println("Request URI: " + request.getRequestURI());

		// ServletInputStream requestBody = request.getInputStream();
		//// byte[]byteBody = requestBody.readAllBytes();
		// int length = requestBody.available();
		// byte[]byteBody = new byte[8192];
		// int ch;
		// String body = "";
		// while (requestBody.read()!=-1) {
		// ch = requestBody.readLine(byteBody, 0, length);
		// if(ch==-1)break;
		// body += new String(byteBody,0,ch);
		// }
		// System.out.println("Print Body Test " + body);
		//
		// Expense expense = null;

		// if((request.getMethod()).compareToIgnoreCase("POST")==0) {
		// if(!UserUtil.checkUserAdminRole(accessToken, this)) {
		// if
		// (ExpenseUserIdCheckUtil.compareExpenseUserId(accessToken,expense,requestUserId))
		// {
		//
		// }
		// }
		// }

		// String userEmail = userDetails.getUsername();
		// Optional<User>optionalUser = userDao.findByEmail(userEmail);
		// User user = optionalUser.get();
		// int tokenUserId = user.getId();

		// String reqUserId = request.getParameter("requestUserId");
		// System.out.println("Requesting User Id: " + reqUserId);

		filterChain.doFilter(request, response);
	}

	private boolean hasAuthorizationHeader(HttpServletRequest request) {
		boolean hasAuth = true;
		String header = request.getHeader("Authorization");
		System.out.println("Authorization header: " + header);
		if (ObjectUtils.isEmpty(header) || !header.startsWith("Bearer")) {
			hasAuth = false;
		}
		return hasAuth;
	}

	private String getAccessToken(HttpServletRequest request) {
		String header = request.getHeader("Authorization");
		String token = header.split(" ")[1].trim();
		System.out.println("Access token(JwtAuthFilter.getAccessToken()): " + token);
		return token;
	}

	private void setAuthenticationContext(String accessToken, HttpServletRequest request) {
		UserDetails userDetails = getUserDetails(accessToken);

		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null,
				userDetails.getAuthorities());
		authentication.setDetails(
				new WebAuthenticationDetailsSource().buildDetails((HttpServletRequest) request));
		SecurityContextHolder.getContext().setAuthentication(authentication);
	}

	public UserDetails getUserDetails(String accessToken) {
		Claims claims = jwtAuthUtil.parseClaims(accessToken);
		String claimRoles = (String) claims.get("roles");
		// String claimSub = claims.getSubject();

		// Testing
		// System.out.println("Claim Roles: " + claimRoles);

		claimRoles = claimRoles.replace("[", "").replace("]", "");
		String[] roleNames = claimRoles.split(",");
		for (String roleName : roleNames) {
			tokenUserDetails.addRole(new Role(roleName));
		}
		String subject = (String) claims.get(Claims.SUBJECT);
		String[] subjectArray = subject.split(",");
		tokenUserDetails.setId(Long.parseLong(subjectArray[0]));
		tokenUserDetails.setEmail(subjectArray[1]);

		return tokenUserDetails;
	}
}
