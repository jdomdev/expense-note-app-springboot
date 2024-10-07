package com.johnsunday.app.service;

import java.time.LocalDateTime;
import java.util.List;

import com.johnsunday.app.entity.Employee;
import com.johnsunday.app.entity.Payroll;

public interface IPayrollService {

	public List<Payroll> findAll() throws Exception;

	public Payroll findById(Long id) throws Exception;

	public Payroll save(Payroll payroll) throws Exception;

	public Payroll update(Long id, Payroll payroll) throws Exception;

	public Boolean delete(Long id) throws Exception;

	public List<Payroll> findAllPayrollByEmployeeId(Long employeeId) throws Exception;

	public Boolean findByDateAndEmployeeAllIgnoreCase(LocalDateTime PayrollDate, Employee employee) throws Exception;
}
