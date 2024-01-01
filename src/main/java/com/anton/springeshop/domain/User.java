package com.anton.springeshop.domain;

import lombok.*;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class User {

	private static final String SEQ_NAME = "user_seq"; //наименование последовательности

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQ_NAME)
	@SequenceGenerator(name = SEQ_NAME, sequenceName = SEQ_NAME, allocationSize = 1)
	private Long id;
	private String name;
	private String password;
	private String email;
	private boolean archive;
	@Enumerated(EnumType.STRING) //роли в виде строк
	private Role role;
	@OneToOne(mappedBy = "user", cascade = CascadeType.REMOVE) //у одного пользователя одна корзина
	private Bucket bucket;

}
