package io.sunbit.app.security.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.hibernate.validator.constraints.Length;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "role")
// @ToString(includeFieldNames = false)
public class Role implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(name = "name", nullable = false, unique = true)
	@Length(min = 6, max = 25)
	private String name;

	// Constructor without id.
	public Role(String name) {
		this.name = name;
	}

	// Constructor with id.
	public Role(Long id, String name) {
		this.id = id;
		this.name = name;
	}

	@ManyToMany(mappedBy = "roles")
	private List<ExpenseUser> users;

	@Override
	public String toString() {
		return this.name;
	}

}
