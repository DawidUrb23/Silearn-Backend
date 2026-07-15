package com.itsilesia.si_learn.repositories.user_progress;

import com.itsilesia.si_learn.entities.compositeIds.UserModuleId;
import com.itsilesia.si_learn.entities.user_progress.UserModule;
import com.itsilesia.si_learn.repositories.SpecificationJpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserModuleRepository extends SpecificationJpaRepository<UserModule, UserModuleId> {
}
