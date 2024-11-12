package com.fintech.database.repository;

import com.fintech.database.entity.Events;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface EventsRepository extends JpaRepository<Events, Long>, JpaSpecificationExecutor<Events> {
}
