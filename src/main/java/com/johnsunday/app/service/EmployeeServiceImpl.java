package com.johnsunday.app.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.johnsunday.app.dao.IEmployeeDao;
import com.johnsunday.app.dao.IEmployeeTypeDao;
import com.johnsunday.app.entity.Employee;
import com.johnsunday.app.util.DateUtil;

@Service
public class EmployeeServiceImpl implements IEmployeeService{
	
	@Autowired IEmployeeDao employeeDao;
	@Autowired IEmployeeTypeDao employeeTypeDao;
	
	@Override 
	public Employee findByNameAndSurnameAllIgnoreCase(String name,String surname) throws Exception {
		Optional<Employee>optionalEmployee  = employeeDao.findByNameAndSurnameAllIgnoreCase(name,surname);
		Employee searchedEmployee = null;
		if (!optionalEmployee.isEmpty()) {
			searchedEmployee = optionalEmployee.get();
		}
		return searchedEmployee;		
	}
	@Override
	public List<Employee> findAll() throws Exception {
		try {
			return  employeeDao.findAll();										
		}catch(Exception e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}
	}
	@Override
	public Employee findById(Integer id) throws Exception {
		try {
			Optional<Employee> optionalEmployee = employeeDao.findById(id);		
			return optionalEmployee.get();
		}catch(Exception e) {			
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}
	}
	@Override
	@Transactional
	public Employee save(Employee employee) throws Exception {
		try {
			LocalDateTime parsedDate = DateUtil.formattingDate(employee.getBirthDate());
			employee.setBirthDate(parsedDate);			
			return employeeDao.save(employee);			
		}catch(Exception e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}
	}
	@Override
	@Transactional
	public Employee update(Integer id,Employee employee) throws Exception {
		Employee updatedEmployee = null;
		try {
			Optional<Employee> optionalEmployee = employeeDao.findById(id);					
			if(optionalEmployee!=null) {
				LocalDateTime parsedDate = DateUtil.formattingDate(employee.getBirthDate());
				employee.setBirthDate(parsedDate);
				updatedEmployee = employeeDao.save(employee);	
			}					
		}catch(Exception e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}
		return updatedEmployee;	
	}
	@Override
	@Transactional
	public Boolean delete(Integer id) throws Exception {
		boolean isDeleted = false;
		try {
			if(employeeDao.existsById(id)) {
				employeeDao.deleteById(id);
				isDeleted = true;
			} else {
				throw new Exception();
			}
		}catch(Exception e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}
		return isDeleted;
	}
	@Override
	public Employee findByEmail(String email) throws Exception {
		try {
			Optional<Employee>optEmployee = employeeDao.findByEmail(email);
			return optEmployee.get();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}				
	}
}
