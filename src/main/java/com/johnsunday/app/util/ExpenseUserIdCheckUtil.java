package com.johnsunday.app.util;

import org.springframework.beans.factory.annotation.Autowired;

import com.johnsunday.app.entity.Expense;
import com.johnsunday.app.security.jwt.JwtAuthenticationUtil;

public class ExpenseUserIdCheckUtil {
	
	@Autowired private static JwtAuthenticationUtil jwtAuthUtil;
	
	public static Boolean compareExpenseUserId(String token,Expense expense,Integer requestUserId) throws Exception {
		boolean isSameUser = false;
		// 1º - We check if the EXPENSE exists in DB.
		if (!ExpenseUtil.existsExpenseInDb(expense)) {
			// 2º - We check if the EMPLOYEE exists in DB.
			if (EmployeeUtil.existsEmployeeInDb(expense.getEmployee())) {
				// Form B to extract the Id user from token(JwtAuthenticationUtil.getSubject(token))
				// 4º -  We extract the ID USER from token.
				String subject = jwtAuthUtil.getSubject(token);
				String[]arrSubject = subject.split(",");
				int tokenUserId = Integer.parseInt(arrSubject[0]);
				// Test.
				System.out.println("TOKEN User Id: " + tokenUserId);
				// 5º - We compare the ID USER from token with the HTTP param 'requestUserId' from params method.
				if (tokenUserId==requestUserId) isSameUser = true;					
			}else throw new Exception("The EMPLOYEE you're you're looking for DOESN'T EXISTS in DB");
		} else throw new Exception("The EXPENSE you're trying to save IS ALREADY in DB");
		return isSameUser;
	}
}
