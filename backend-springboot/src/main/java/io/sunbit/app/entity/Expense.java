package io.sunbit.app.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.validator.constraints.Length;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "expense")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
// @Audited
@RequiredArgsConstructor
@ToString
public class Expense implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(name = "concept", nullable = false)
	@Length(min = 3, max = 128)
	@NonNull
	private String concept;
	@Column(name = "note", nullable = false)
	@Length(min = 3, max = 255)
	// @NonNull
	private String note;
	@Column(name = "expense_date", nullable = false)
	// @Temporal(TemporalType.TIMESTAMP)
	@NonNull
	private LocalDateTime date;
	@Column(name = "amount", nullable = false)
	@NonNull
	private Double amount;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "employee_id") // "id"->ERROR
	// @JsonIgnore
	@NonNull
	private Employee employee;

	// Constructor without note.
	public Expense(Long id, String concept, LocalDateTime date, Double amount, Employee employee) {
		this.id = id;
		this.concept = concept;
		this.date = date;
		this.amount = amount;
		this.employee = employee;
	}
}
