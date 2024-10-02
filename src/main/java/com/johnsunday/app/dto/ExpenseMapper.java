package com.johnsunday.app.dto;

import com.johnsunday.app.entity.Expense;

public class ExpenseMapper {
	// Without ID.
	public static Expense dtoToExpense(ExpenseDto dtoExpense) {
		return new Expense(
				null, dtoExpense.getConcept(),
				dtoExpense.getNote(),
				dtoExpense.getDate(),
				dtoExpense.getAmount(),				
				EmployeeMapper.dtoToEmployeeWithId(dtoExpense.getDtoEmployee())
				);
	}
	public static ExpenseDto expenseToDto(Expense expense) {
		return new ExpenseDto(
				expense.getConcept(),
				expense.getNote(),
				expense.getDate(),
				expense.getAmount(),				
				EmployeeMapper.employeeToDtoWithId(expense.getEmployee())
				);
	}
	// With ID.
	public static Expense dtoToExpenseWithId(ExpenseDto dtoExpense) {
		return new Expense(
				dtoExpense.getId(),
				dtoExpense.getConcept(),
				dtoExpense.getNote(),
				dtoExpense.getDate(),
				dtoExpense.getAmount(),				
				EmployeeMapper.dtoToEmployeeWithId(dtoExpense.getDtoEmployee())
				);
	}	
	public static ExpenseDto expenseToDtoWithId(Expense expense) {
		return new ExpenseDto(
				expense.getId(),
				expense.getConcept(),
				expense.getNote(),
				expense.getDate(),
				expense.getAmount(),				
				EmployeeMapper.employeeToDtoWithId(expense.getEmployee())
				);
	}
}