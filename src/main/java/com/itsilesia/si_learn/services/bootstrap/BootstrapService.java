package com.itsilesia.si_learn.services.bootstrap;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class BootstrapService {

	@EventListener(ApplicationReadyEvent.class)
	public void init() {
	}


}
