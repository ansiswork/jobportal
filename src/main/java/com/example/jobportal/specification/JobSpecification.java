package com.example.jobportal.specification;

import org.springframework.data.jpa.domain.Specification;

import com.example.jobportal.entity.Job;

public class JobSpecification {

    public static Specification<Job> hasKeyword(String keyword) {

        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("title")),
                        "%" + keyword.toLowerCase() + "%"
                );
    }

    public static Specification<Job> hasLocation(String location) {

        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("location")),
                        "%" + location.toLowerCase() + "%"
                );
    }
}
