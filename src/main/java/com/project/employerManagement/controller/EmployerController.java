package com.project.employerManagement.controller;


import com.project.employerManagement.model.dto.EmployerDTO;
import com.project.employerManagement.service.EmployerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employers")
@RequiredArgsConstructor
public class EmployerController {

    private final EmployerService employerService;

    @PostMapping("/add")
    public ResponseEntity<EmployerDTO> addEmployer(@RequestBody @Valid EmployerDTO employer) {
        EmployerDTO newEmployer = employerService.addEmployer(employer);
        return ResponseEntity.status(HttpStatus.CREATED).body(newEmployer);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<EmployerDTO> updateEmployer(@RequestBody @Valid EmployerDTO employer, @PathVariable Long id) {
        EmployerDTO updatedEmployer = employerService.updateEmployer(employer, id);
        return ResponseEntity.ok(updatedEmployer);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteEmployer(@PathVariable Long id) {
        employerService.deleteEmployer(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployerDTO> getEmployerById(@PathVariable Long id) {
        EmployerDTO employer = employerService.getEmployerById(id);
        return ResponseEntity.ok(employer);
    }

    @GetMapping("/")
    public ResponseEntity<List<EmployerDTO>> getAllEmployers() {
        List<EmployerDTO> employers = employerService.getAllEmployers();
        return ResponseEntity.ok(employers);
    }

}
