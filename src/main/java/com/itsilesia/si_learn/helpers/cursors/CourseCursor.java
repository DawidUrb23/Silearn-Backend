package com.itsilesia.si_learn.helpers.cursors;

import java.time.LocalDateTime;
import java.util.UUID;

public record CourseCursor(LocalDateTime createdAt, UUID id) {}
