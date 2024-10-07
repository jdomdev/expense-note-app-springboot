package com.johnsunday.app.security.service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.johnsunday.app.entity.Employee;
import com.johnsunday.app.security.dao.IRoleDao;
import com.johnsunday.app.security.dao.IUserDao;
import com.johnsunday.app.security.entity.Role;
import com.johnsunday.app.security.entity.ExpenseUser;
import com.johnsunday.app.service.EmployeeServiceImpl;

@Service
public class UserServiceImpl implements IUserService, UserDetailsService {

	private final String ROLE_USER = "ROLE_USER";
	@Autowired
	private IUserDao userDao;
	@Autowired
	private IRoleDao roleDao;
	@Autowired
	@Lazy
	private BCryptPasswordEncoder passwordEncoder;
	@Autowired
	private EmployeeServiceImpl employeeService;
	@Autowired
	private EntityManager entityManager;

	@Override
	@Transactional
	public ExpenseUser save(ExpenseUser user) throws Exception {
		ExpenseUser savedUser = new ExpenseUser();
		ExpenseUser settedUser = new ExpenseUser();
		try {
			Employee employee = employeeService.findByEmail(user.getEmail());
			if (employee != null) {
				if ((user.getRoles() == null) || (user.getRoles().size() == 0)) {
					user.getRoles().add(roleDao.findByName(ROLE_USER).get());
				}
				settedUser = setUser(user);
				savedUser = userDao.save(settedUser);
			}
		} catch (Exception e) {
			// System.out.println("e.getCause(): " + e.getCause());
			throw new Exception(e.getCause());
		}
		return savedUser;
	}

	private ExpenseUser setUser(ExpenseUser user) throws Exception {
		ExpenseUser settedUser = new ExpenseUser();
		if (user.getId() != null)
			settedUser.setId(user.getId());
		settedUser.setEmail(user.getEmail());
		settedUser.setName(user.getName());
		settedUser.setSurname(user.getSurname());
		settedUser.setPassword(passwordEncoder.encode(user.getPassword()));

		for (Role role : user.getRoles()) {
			if ((roleDao.findById(role.getId()) != null)) {
				settedUser.getRoles().add(entityManager.merge(role));
			} else {
				throw new Exception(
						"The Role name: " + role.getName() + " with id: " + role.getId() + " IS NOT in Data Base");
			}
		}
		return settedUser;
	}

	@Override
	@Transactional
	public ExpenseUser update(Integer id, ExpenseUser user) throws Exception {
		ExpenseUser updatedUser = new ExpenseUser();
		try {
			Optional<ExpenseUser> optionalUser = userDao.findById(id);
			if (!optionalUser.isEmpty()) {
				// User settedUser = setUser(optionalUser.get());
				// updatedUser = userDao.save(settedUser);
				user.setPassword(passwordEncoder.encode(user.getPassword()));
				updatedUser = userDao.save(user);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}
		return updatedUser;
	}

	// Load User by 'email', NOT by name.
	@Override
	public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {
		Optional<ExpenseUser> optionalUser = userDao.findByEmail(userEmail);
		if (optionalUser == null)
			throw new UsernameNotFoundException("User or Password INVALIDS");
		/*
		 * Warning !!!
		 * We have to implement User constructor:
		 * 'Construct the User with the details required by
		 * org.springframework.security.authentication.dao.DaoAuthentication'
		 */
		return new org.springframework.security.core.userdetails.User(
				optionalUser.get().getEmail(),
				optionalUser.get().getPassword(),
				mappAuthorityRole(optionalUser.get().getRoles()));
	}

	public Collection<? extends GrantedAuthority> mappAuthorityRole(Collection<Role> roles) {
		return roles
				.stream()
				.map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
	}

	@Override
	public Optional<ExpenseUser> findByEmail(String email) throws Exception {
		ExpenseUser searchedUser = null;
		Optional<ExpenseUser> optionalUser = userDao.findByEmail(email);
		if (!optionalUser.isEmpty()) {
			searchedUser = optionalUser.get();
		}
		return Optional.of(searchedUser);
	}

	@Override
	public List<ExpenseUser> findAll() throws Exception {
		return userDao.findAll();
	}

	@Override
	public Optional<ExpenseUser> findById(Integer id) throws Exception {
		Optional<ExpenseUser> optUser = userDao.findById(id);
		return optUser;
	}

	@Override
	@Transactional
	public Boolean delete(Integer id) throws Exception {
		boolean isDeleted = false;
		Optional<ExpenseUser> optUser = userDao.findById(id);
		if (optUser != null) {
			optUser.get().getRoles().clear();
			userDao.delete(optUser.get());
			isDeleted = true;
		}
		return isDeleted;
	}
}
