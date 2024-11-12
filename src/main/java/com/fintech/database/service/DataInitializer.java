package com.fintech.database.service;

import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;

public interface DataInitializer {

    @EventListener(ContextRefreshedEvent.class)
    void init();
}