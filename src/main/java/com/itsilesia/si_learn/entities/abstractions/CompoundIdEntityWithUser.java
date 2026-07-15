package com.itsilesia.si_learn.entities.abstractions;

import com.itsilesia.si_learn.entities.users.User;

public interface CompoundIdEntityWithUser extends EntityWithUuId {
	User getUser();
}
