package com.project.employerManagement.service;

import com.project.employerManagement.config.CustomMessages;
import com.project.employerManagement.exception.EntityAlreadyExistsException;
import com.project.employerManagement.exception.EntityNotFoundException;
import com.project.employerManagement.model.dto.EmployerDTO;
import com.project.employerManagement.model.entity.Employer;
import com.project.employerManagement.repository.EmployerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmployerServiceTests {

    @Mock
    private EmployerRepository employerRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private EmployerService employerService;

    private EmployerDTO createEmployerDTO() {
        return EmployerDTO.builder()
                .name("John Doe")
                .email("john@example.com")
                .position("Software Engineer")
                .build();
    }

    private Employer createEmployer() {
        return Employer.builder()
                .name("John Doe")
                .email("john@example.com")
                .position("Software Engineer")
                .build();
    }

    //-------------------------Testing addEmployer from EmployerService-------------------------

    @Test
    void addEmployer_WhenEmailDoesNotExist_ShouldSaveAndReturnEmployerDTO() {

        EmployerDTO employerDTO = createEmployerDTO();

        Employer employer = createEmployer();

        when(modelMapper.map(employerDTO, Employer.class)).thenReturn(employer);

        when(employerRepository.findByEmail(employer.getEmail())).thenReturn(Optional.empty());

        when(employerRepository.save(employer)).thenReturn(employer);

        when(modelMapper.map(employer, EmployerDTO.class)).thenReturn(employerDTO);

        EmployerDTO result = employerService.addEmployer(employerDTO);

        assertNotNull(result);
        assertEquals(employerDTO.getEmail(), result.getEmail());
        verify(employerRepository, times(1)).save(employer);

    }

    @Test
    void addEmployer_WhenEmailAlreadyExists_ShouldThrowException() {

        EmployerDTO employerDTO = createEmployerDTO();

        Employer existingEmployer = createEmployer();

        when(modelMapper.map(employerDTO, Employer.class)).thenReturn(existingEmployer);

        when(employerRepository.findByEmail(existingEmployer.getEmail())).thenReturn(Optional.of(existingEmployer));

        EntityAlreadyExistsException exception = assertThrows(
                EntityAlreadyExistsException.class,
                () -> employerService.addEmployer(employerDTO)
        );

        assertEquals(CustomMessages.employerAlreadyExists(employerDTO.getEmail()), exception.getMessage());
        verify(employerRepository, never()).save(any());

    }

    //-------------------------Testing updateEmployer from EmployerService-------------------------

    @Test
    void updateEmployer_WhenEmployerExists_ShouldUpdateAndReturnEmployerDTO() {

        EmployerDTO employerDTO = createEmployerDTO();

        Employer existingEmployer = createEmployer();

        when(employerRepository.findById(1L)).thenReturn(Optional.of(existingEmployer));

        when(employerRepository.save(existingEmployer)).thenReturn(existingEmployer);

        when(modelMapper.map(existingEmployer, EmployerDTO.class)).thenReturn(employerDTO);

        EmployerDTO result = employerService.updateEmployer(employerDTO, 1L);

        assertNotNull(result);
        assertEquals(employerDTO.getEmail(), result.getEmail());
        verify(employerRepository, times(1)).findById(1L);
        verify(employerRepository, times(1)).save(existingEmployer);

    }

    @Test
    void updateEmployer_WhenEmployerDoesNotExist_ShouldThrowException() {

        EmployerDTO employerDTO = createEmployerDTO();

        when(employerRepository.findById(1L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> employerService.updateEmployer(employerDTO, 1L)
        );

        assertEquals(CustomMessages.EMPLOYER_NOT_FOUND, exception.getMessage());
        verify(employerRepository, never()).save(any());

    }

    @Test
    void updateEmployer_WhenEmailAlreadyExists_ShouldThrowException() {

        Employer employerBeingUpdated = createEmployer();

        EmployerDTO employerDTO = createEmployerDTO();
        employerDTO.setEmail("danny@example.com");

        Employer sameEmailEmployer = createEmployer();
        sameEmailEmployer.setEmail("danny@example.com");

        when(employerRepository.findById(1L)).thenReturn(Optional.of(employerBeingUpdated));

        when(employerRepository.findByEmail(employerDTO.getEmail())).thenReturn(Optional.of(sameEmailEmployer));

        EntityAlreadyExistsException exception = assertThrows(
                EntityAlreadyExistsException.class,
                () -> employerService.updateEmployer(employerDTO, 1L)
        );

        assertEquals(CustomMessages.employerAlreadyExists(employerDTO.getEmail()), exception.getMessage());
        verify(employerRepository, never()).save(any());

    }

    //-------------------------Testing deleteEmployer from EmployerService-------------------------

    @Test
    void deleteEmployer_WhenEmployerExists_ShouldDeleteEmployer() {

        Long employerId = 1L;

        when(employerRepository.existsById(employerId)).thenReturn(true);

        employerService.deleteEmployer(employerId);

        verify(employerRepository, times(1)).deleteById(employerId);

    }

    @Test
    void deleteEmployer_WhenEmployerDoesNotExist_ShouldThrowException() {

        Long employerId = 1L;

        when(employerRepository.existsById(employerId)).thenReturn(false);

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> employerService.deleteEmployer(employerId)
        );

        assertEquals(CustomMessages.EMPLOYER_NOT_FOUND, exception.getMessage());
        verify(employerRepository, never()).deleteById(employerId);

    }

    //-------------------------Testing getByIdEmployer from EmployerService-------------------------

    @Test
    void getEmployerById_WhenEmployerExists_ShouldReturnEmployerDTO() {

        Long employerId = 1L;

        Employer employer = createEmployer();

        EmployerDTO employerDTO = createEmployerDTO();

        when(employerRepository.findById(employerId)).thenReturn(Optional.of(employer));

        when(modelMapper.map(employer, EmployerDTO.class)).thenReturn(employerDTO);

        EmployerDTO result = employerService.getEmployerById(employerId);

        assertNotNull(result);
        assertEquals(employer.getEmail(), result.getEmail());
        verify(employerRepository, times(1)).findById(employerId);

    }

    @Test
    void getEmployerById_WhenEmployerDoesNotExist_ShouldThrowException() {

        Long employerId = 1L;

        when(employerRepository.findById(1L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> employerService.getEmployerById(1L)
        );

        assertEquals(CustomMessages.EMPLOYER_NOT_FOUND, exception.getMessage());
        verify(employerRepository, times(1)).findById(employerId);

    }

    //-------------------------Testing getAllEmployers from EmployerService-------------------------

    @Test
    void getAllEmployers_WhenEmployersExist_ShouldReturnListOfEmployerDTOs() {

        List<Employer> employers = List.of(createEmployer(), createEmployer());

        List<EmployerDTO> employerDTOs = List.of(createEmployerDTO(), createEmployerDTO());

        when(employerRepository.findAll()).thenReturn(employers);

        when(modelMapper.map(employers.get(0), EmployerDTO.class)).thenReturn(employerDTOs.get(0));
        when(modelMapper.map(employers.get(1), EmployerDTO.class)).thenReturn(employerDTOs.get(1));

        List<EmployerDTO> result = employerService.getAllEmployers();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(employerRepository, times(1)).findAll();

    }
}