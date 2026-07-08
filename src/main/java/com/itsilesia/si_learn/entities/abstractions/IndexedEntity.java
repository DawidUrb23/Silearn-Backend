package com.itsilesia.si_learn.entities.abstractions;

import java.util.UUID;

public interface IndexedEntity {
	int getIndex();

	void setIndex(int value);

	UUID getId();
}
