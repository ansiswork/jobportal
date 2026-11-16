package com.example.jobportal.dto;

public class JobResponse {

    private Long id;
    private String title;
    private String description;
    private String company;
    private String location;
    private Double salary;

    public JobResponse() {
    }

    public JobResponse(
            Long id,
            String title,
            String description,
            String company,
            String location,
            Double salary) {

        this.id = id;
        this.title = title;
        this.description = description;
        this.company = company;
        this.location = location;
        this.salary = salary;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getCompany() {
        return company;
    }

    public String getLocation() {
        return location;
    }

    public Double getSalary() {
        return salary;
    }
}