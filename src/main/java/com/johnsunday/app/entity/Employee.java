package com.johnsunday.app.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name="employee",uniqueConstraints=@UniqueConstraint(columnNames="email"))
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
//@Audited
@ToString
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Employee implements Serializable {
	
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	@Column(name="name",nullable=false,length=128)
	@Length(min=3,max=128)
	@NonNull
	private String name;
	@Column(name="surname",nullable=false,length=255)
	@Length(min=5,max=255)
	@NonNull
	private String surname;
	@Column(name="birth_date")
	//@Temporal(TemporalType.TIMESTAMP)
	@NonNull
	private LocalDateTime birthDate;
	@OneToOne
	@JoinColumn(name="employee_type_id_fk")
	@NonNull
	private EmployeeType employeeType;
	@Column(name="email",nullable=false)
	@Length(min=3,max=50)
	@NonNull
	private String email;
	
	@OneToMany(mappedBy="employee",targetEntity=Expense.class,
			   cascade= {CascadeType.MERGE, CascadeType.REMOVE,            
					   CascadeType.REFRESH, CascadeType.DETACH},
			   orphanRemoval=false)
	//@JsonIgnore
	@JsonBackReference
	private List<Expense>expenses = new ArrayList<>();
	
	@OneToMany(mappedBy="employee",targetEntity=Payroll.class,
			   cascade={CascadeType.MERGE,CascadeType.REFRESH,
					    CascadeType.DETACH},
			   orphanRemoval=false)
	@JsonIgnore
	//@JsonBackReference	
	private List<Payroll>payrolls = new ArrayList<>();
	
	// Constructor without id.
	public Employee(String name,String surname,LocalDateTime birthDate,
					EmployeeType employeeType,String email,
					List<Expense>expenses,List<Payroll>payrolls) {
		this.name = name;
		this.surname = surname;
		this.birthDate = birthDate;
		this.employeeType = employeeType;
		this.email = email;
		this.expenses = expenses;
		this.payrolls = payrolls;
	}
	
	public void addExpense(Expense expense) {
		expenses.add(expense);
	}
	public void addPayroll(Payroll payroll) {
		payrolls.add(payroll);
	}
	public void removeExpense(Expense expense) {
		expenses.remove(expense);
	}
	public void removePayroll(Payroll payroll) {
		payrolls.remove(payroll);
	}
}
