package com.johnsunday.app.security.dto;

import com.johnsunday.app.security.entity.Role;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoleDto extends Role {

	private static final long serialVersionUID = 1L;
	private Integer id;
	private String name;

	// Constructor without id.
	public RoleDto(String name) {
		this.name = name;
	}
}