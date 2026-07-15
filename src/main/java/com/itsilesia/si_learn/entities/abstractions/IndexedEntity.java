package com.itsilesia.si_learn.entities.abstractions;

public interface IndexedEntity extends EntityWithUuId {
	int getIndex();

	void setIndex(int value);
}
