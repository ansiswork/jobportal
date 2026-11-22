package com.example.jobportal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.jobportal.entity.Application;

public interface ApplicationRepository
        extends JpaRepository<Application, Long> {
	
	boolean existsByUser_IdAndJob_Id(Long userId, Long jobId);

	List<Application> findByUser_Id(Long userId);
}