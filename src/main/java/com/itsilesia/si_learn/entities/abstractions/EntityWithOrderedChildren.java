package com.itsilesia.si_learn.entities.abstractions;

import com.itsilesia.si_learn.dtos.request.courses.ContentsOrderUpdateRequest;
import com.itsilesia.si_learn.exceptions.InvalidParametersException;
import com.itsilesia.si_learn.exceptions.ItemNotFoundException;
import jakarta.validation.constraints.NotNull;
import org.yaml.snakeyaml.util.Tuple;

import java.util.*;
import java.util.stream.Collectors;

public interface EntityWithOrderedChildren {
	static <T extends IndexedEntity> int getNextIndex(@NotNull List<T> orderedChildren) {
		int maxIndex = orderedChildren.stream()
			.mapToInt(IndexedEntity::getIndex)
			.max()
			.orElse(0);
		return maxIndex + 1;
	}

	static <T extends IndexedEntity> void updateIndexesOfChildren(@NotNull List<T> orderedChildren, @NotNull ContentsOrderUpdateRequest request) {
		final int numberOfChildren = orderedChildren.size();
		if (request.getContents().size() != numberOfChildren)
			throw new InvalidParametersException("Contents Size Mismatch");
		Set<UUID> noDuplicates = new HashSet<>(request.getContents());
		if (noDuplicates.size() != numberOfChildren)
			throw new InvalidParametersException("Contents Size Mismatch");
		int index = 1;
		Map<UUID, T> contentsMap = orderedChildren.stream()
			.collect(Collectors.toMap(
				IndexedEntity::getId, item -> item));
		//update the index
		for (var itemId : request.getContents()) {
			var keyValue = contentsMap.get(itemId);
			if (keyValue == null)
				throw new ItemNotFoundException(IndexedEntity.class, itemId);
			keyValue.setIndex(index++);
		}
	}

	/**
	 * Joins (course, module ...) with (userCourse, userModule ...)
	 *
	 * @param items      (course, module ...)
	 * @param userItems  (userCourse, userModule ...)
	 * @param <Item>     (course, module ...)
	 * @param <UserItem> (userCourse, userModule ...)
	 * @return a list of joined items
	 */
	static <Item extends EntityWithUuId, UserItem extends CompoundIdEntityWithUser>
	@NotNull List<@NotNull Tuple<@NotNull Item, UserItem>>
	JoinItemWithUserItem(@NotNull List<@NotNull Item> items, @NotNull List<UserItem> userItems) {
		var userItemsMap = userItems.stream().collect(
			Collectors.toMap(EntityWithUuId::getId, item -> item)
		);
		return items.stream().map(item -> new Tuple<>(item, userItemsMap.get(item.getId()))).toList();
	}
}
