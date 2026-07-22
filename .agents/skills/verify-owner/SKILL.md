---
name: verify-owner
description: Helps to verify the owner of a resource.
---

# verify owner

inject `CourseHelperService` and use the method `.isAuthorOrThrow(#item, userId);`

## When to use this skill

- Use this when you need to limit access to the Author/owner of a resource
- valid items are: Course, Module, Lesson, Task, TaskContent, TestScenario

## How to use it

inject `CourseHelperService` and use its method `.isAuthorOrThrow(#item, userId);`