package com.project.employerManagement.controller;


import com.project.employerManagement.model.dto.EmployerDTO;
import com.project.employerManagement.service.EmployerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(
        name = "CRUD REST APIs for employers",
        description = "CRUD REST APIs - Create / Read / Update / Delete employers"
)
@RestController
@RequestMapping("/api/employers")
@RequiredArgsConstructor
public class EmployerController {

    private final EmployerService employerService;

    @Operation(
            summary = "Add employers",
            description = "Add employers to the database"
    )
    @ApiResponse(
            responseCode = "201",
            description = "Created employer successfully"
    )
    @PostMapping("/add")
    public ResponseEntity<EmployerDTO> addEmployer(@RequestBody @Valid EmployerDTO employer) {
        EmployerDTO newEmployer = employerService.addEmployer(employer);
        return ResponseEntity.status(HttpStatus.CREATED).body(newEmployer);
    }

    @Operation(
            summary = "Update employer",
            description = "Update employer in the database"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Updated employer successfully"
    )
    @PutMapping("/update/{id}")
    public ResponseEntity<EmployerDTO> updateEmployer(@RequestBody @Valid EmployerDTO employer, @PathVariable Long id) {
        EmployerDTO updatedEmployer = employerService.updateEmployer(employer, id);
        return ResponseEntity.ok(updatedEmployer);
    }

    @Operation(
            summary = "Delete employer",
            description = "Delete employer from the database"
    )
    @ApiResponse(
            responseCode = "204",
            description = "Deleted employer successfully"
    )
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteEmployer(@PathVariable Long id) {
        employerService.deleteEmployer(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Get employer by id",
            description = "Get employer by id from the database"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Found employer by id successfully"
    )
    @GetMapping("/{id}")
    public ResponseEntity<EmployerDTO> getEmployerById(@PathVariable Long id) {
        EmployerDTO employer = employerService.getEmployerById(id);
        return ResponseEntity.ok(employer);
    }

    @Operation(
            summary = "Get all employers",
            description = "Get all employers from the database"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Found all employers successfully"
    )
    @GetMapping("/")
    public ResponseEntity<List<EmployerDTO>> getAllEmployers() {
        List<EmployerDTO> employers = employerService.getAllEmployers();
        return ResponseEntity.ok(employers);
    }

}
