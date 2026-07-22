package com.itsilesia.si_learn.entities.abstractions;

import com.itsilesia.si_learn.entities.enums.ProgressStatus;

import java.time.LocalDateTime;
import java.util.Collection;

public interface UserProgressNode {
	// Current node status check
	ProgressStatus progressStatus();

	// Traversal hooks
	UserProgressNode getParentProgressNode();

	// Traversal hooks
	Collection<? extends UserProgressNode> getSiblingProgressNodes();

	// Traversal hooks
	Collection<?> getBlueprintNodes();

	// Mutator to complete the node
	void setEndDate(LocalDateTime endDate);
}
