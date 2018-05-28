package com.spring.springjwt.repo;

import com.spring.springjwt.entities.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
}
