package com.johnsunday.app.security.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.johnsunday.app.security.dto.DtoRole;
import com.johnsunday.app.security.dto.DtoUser;
import com.johnsunday.app.security.service.RoleServiceImpl;

@CrossOrigin(origins="*")
@RequestMapping("api/v1/role")
@RestController
public class RoleControllerImpl 
			 /*extends BaseControllerImpl<Role, RoleServiceImpl>*/ {	
	
	@Lazy
	@Autowired
	private RoleServiceImpl roleService;
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/all")	
	public ResponseEntity<?> getAllRole(@RequestParam("requestUserId")Integer requestUserId){
		try {
			return ResponseEntity.status(HttpStatus.OK).body(roleService.findAll());
		}catch(Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"error\":\"Error. Please, Try it later. It is NOT possible to SHOW all the roles\"}");
		}
	}
	@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
	@GetMapping("/one/{roleId}")
	public ResponseEntity<?> getOneRole(@PathVariable("roleId")Integer roleId,
										@RequestParam("requestUserId")Integer requestUserId){
		try {
			return ResponseEntity.status(HttpStatus.OK).body(roleService.findById(roleId));
		}catch(Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"error\":\"Error. Please, Try it later. It is NOT possible to SHOW the role who you find.\"}");
		}
	}
	@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
	@PostMapping("/save")	
	public ResponseEntity<?> saveRole(@Valid @RequestBody DtoRole dtoRole,
									  @RequestParam("requestUserId")Integer requestUserId){
		try {
			return ResponseEntity.status(HttpStatus.OK).body(roleService.save(dtoRole));
		}catch(Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\":\"Error. Please, Try it later. NOT possible to SAVE the user.\"}");
		}
	}
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PutMapping("/update/{roleId}")	
	public ResponseEntity<?> updateEntity(@PathVariable("roleId")Integer roleId,
										  @RequestBody DtoRole dtoRole,
										  @RequestParam("requestUserId")Integer requestUserId){
		try {
			return ResponseEntity.status(HttpStatus.OK).body(roleService.update(roleId, dtoRole));
		}catch(Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\":\"Error. Please, Try it later. it is NOT possible UPDATE the user who you looking for.\"}");
		}
	}
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@DeleteMapping("/delete/{userId}")	
	public ResponseEntity<?> deleteEntity(@PathVariable("userId")Integer userId,
										  @RequestParam("requestUserId")Integer requestUserId){
		try {
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body(roleService.delete(userId));
		}catch(Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\":\"Error. Please, Try it later. It is NOT possible DELETE the user who you looking for.\"}");
		}
	}
}