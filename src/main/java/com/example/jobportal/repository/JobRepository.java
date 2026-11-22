package com.example.jobportal.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.example.jobportal.entity.Job;

public interface JobRepository extends JpaRepository<Job, Long>, JpaSpecificationExecutor<Job>  {

    Page<Job> findByTitleContainingIgnoreCase(
            String title,
            Pageable pageable
    );

    Page<Job> findByLocationContainingIgnoreCase(
            String location,
            Pageable pageable
    );
}
