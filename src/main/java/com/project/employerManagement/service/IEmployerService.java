package com.project.employerManagement.service;

import com.project.employerManagement.model.dto.EmployerDTO;
import com.project.employerManagement.model.entity.Employer;

import java.util.List;

public interface IEmployerService {

    public EmployerDTO addEmployer(EmployerDTO employer);

    public EmployerDTO updateEmployer(EmployerDTO employer, Long id);

    public void deleteEmployer(Long id);

    public EmployerDTO getEmployerById(Long id);

    public List<EmployerDTO> getAllEmployers();

}
