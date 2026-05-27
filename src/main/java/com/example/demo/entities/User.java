package com.example.demo.entities;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;

	@Column(name = "nick_name")
	private String nickName;

	private String email;

	private String password;

	@CreatedDate
	@Column(name = "register_date")
	private LocalDateTime registerDate;
}
