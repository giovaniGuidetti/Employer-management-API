package com.project.employerManagement.service;

import com.project.employerManagement.config.CustomMessages;
import com.project.employerManagement.exception.EntityAlreadyExistsException;
import com.project.employerManagement.exception.EntityNotFoundException;
import com.project.employerManagement.model.dto.EmployerDTO;
import com.project.employerManagement.model.entity.Employer;
import com.project.employerManagement.repository.EmployerRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployerService implements IEmployerService {

    private final EmployerRepository employerRepository;

    private final ModelMapper modelMapper;

    @Override
    public EmployerDTO addEmployer(EmployerDTO employer) {
        Employer employerToSave = modelMapper.map(employer, Employer.class);
        if(emailAlreadyExists(employerToSave.getEmail())) {
            throw new EntityAlreadyExistsException(CustomMessages.employerAlreadyExists(employer.getEmail()));
        }
        return modelMapper.map(employerRepository.save(employerToSave), EmployerDTO.class);
    }

    @Override
    public EmployerDTO updateEmployer(EmployerDTO employer, Long id) {
        return employerRepository.findById(id).map( e -> {
            if(!e.getEmail().equals(employer.getEmail()) && emailAlreadyExists(employer.getEmail())) {
                throw new EntityAlreadyExistsException(CustomMessages.employerAlreadyExists(employer.getEmail()));
            }
            e.setName(employer.getName());
            e.setEmail(employer.getEmail());
            e.setPosition(employer.getPosition());
            return modelMapper.map(employerRepository.save(e), EmployerDTO.class);
        }).orElseThrow(() -> new EntityNotFoundException(CustomMessages.EMPLOYER_NOT_FOUND));
    }

    @Override
    public void deleteEmployer(Long id) {
        if(!employerRepository.existsById(id)) {
            throw new EntityNotFoundException(CustomMessages.EMPLOYER_NOT_FOUND);
        }
        employerRepository.deleteById(id);
    }

    @Override
    public EmployerDTO getEmployerById(Long id) {
        return employerRepository.findById(id).map(e -> modelMapper.map(e, EmployerDTO.class))
                .orElseThrow(() -> new EntityNotFoundException(CustomMessages.EMPLOYER_NOT_FOUND));
    }

    @Override
    public List<EmployerDTO> getAllEmployers() {
        return employerRepository.findAll().stream()
                .map(e -> modelMapper.map(e, EmployerDTO.class)).toList();
    }

    public Boolean emailAlreadyExists(String email) {
        return employerRepository.findByEmail(email).isPresent();
    }

}
