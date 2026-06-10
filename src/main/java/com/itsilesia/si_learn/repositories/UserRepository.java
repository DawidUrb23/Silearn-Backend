package com.itsilesia.si_learn.repositories;
import com.itsilesia.si_learn.entities.users.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;
public interface UserRepository extends JpaRepository<User, UUID> {
}
