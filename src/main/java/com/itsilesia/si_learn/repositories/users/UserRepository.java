package com.itsilesia.si_learn.repositories.users;

import com.itsilesia.si_learn.entities.users.User;
import com.itsilesia.si_learn.repositories.SpecificationJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends SpecificationJpaRepository<User, UUID> {
	boolean existsByEmail(String email);

	Optional<User> findByEmail(String email);
}
