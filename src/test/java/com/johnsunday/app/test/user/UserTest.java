package com.johnsunday.app.test.user;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Rollback;

import com.johnsunday.app.security.dao.IRoleDao;
import com.johnsunday.app.security.dao.IUserDao;
import com.johnsunday.app.security.entity.ExpenseUser;
import com.johnsunday.app.security.entity.Role;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class UserTest {

	@Autowired
	IUserDao userDao;
	@Autowired
	IRoleDao roleDao;

	@Test
	public void testUserSaving() {
		PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

		String rawPassword = "george1234";
		String encodedPassword = passwordEncoder.encode(rawPassword);

		ExpenseUser newUser = new ExpenseUser(
				"George",
				"Harrison",
				"georgeharrison@mail.com",
				encodedPassword,
				// Arrays.asList(new UserRole("ROLE_ADMIN"))
				Arrays.asList(roleDao.findByName("ROLE_USER").get()));
		// User newAdminUser = new User(
		// "Otis",
		// "Ray Redding",
		// "otisredding@gmail.com",
		// encodedPassword,
		// //Arrays.asList(new UserRole("ROLE_ADMIN"))
		// Arrays.asList(roleDao.findByName("ROLE_ADMIN").get())
		// );
		ExpenseUser savedUser = userDao.save(newUser);

		assertThat(savedUser).isNotNull();
		assertThat(savedUser.getId()).isGreaterThan(0);
	}

	@Test
	public void testAssignRoleToUser() {
		Optional<ExpenseUser> optUser = userDao.findByEmail("rihannafenty@mail.com");
		Optional<Role> optRole = roleDao.findByName("ADMIN_ROLE");
		ExpenseUser user = optUser.get();
		user.addRole(optRole.get());
		ExpenseUser updatedUser = userDao.save(user);

		assertThat(updatedUser.getRoles().size() == 2);
	}
}
