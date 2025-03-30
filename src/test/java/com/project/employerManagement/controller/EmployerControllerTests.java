package com.project.employerManagement.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.employerManagement.config.CustomMessages;
import com.project.employerManagement.exception.EntityNotFoundException;
import com.project.employerManagement.model.dto.EmployerDTO;
import com.project.employerManagement.service.EmployerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = EmployerController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class EmployerControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private EmployerService employerService;

    @Autowired
    private ObjectMapper objectMapper;

    private EmployerDTO createEmployerDTO() {
        return EmployerDTO.builder()
                .name("John Doe")
                .email("john@example.com")
                .position("Software Engineer")
                .build();
    }

    @Test
    void addEmployer_ShouldReturnCreatedEmployerDTO() throws Exception {

        EmployerDTO employerDTO = createEmployerDTO();

        when(employerService.addEmployer(any(EmployerDTO.class))).thenReturn(employerDTO);

        ResultActions response = mockMvc.perform(post("/api/employers/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employerDTO)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(employerDTO.getId()))
                .andExpect(jsonPath("$.name").value(employerDTO.getName()))
                .andExpect(jsonPath("$.email").value(employerDTO.getEmail()))
                .andExpect(jsonPath("$.position").value(employerDTO.getPosition()));

        verify(employerService, times(1)).addEmployer(any(EmployerDTO.class));

    }

    @Test
    void updateEmployer_WhenEmployerExists_ShouldReturnUpdatedEmployerDTO() throws Exception {
        EmployerDTO employerDTO = createEmployerDTO();

        when(employerService.updateEmployer(any(EmployerDTO.class), eq(1L))).thenReturn(employerDTO);

        ResultActions response = mockMvc.perform(put("/api/employers/update/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employerDTO)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(employerDTO.getId()))
                .andExpect(jsonPath("$.name").value(employerDTO.getName()))
                .andExpect(jsonPath("$.email").value(employerDTO.getEmail()))
                .andExpect(jsonPath("$.position").value(employerDTO.getPosition()));

        verify(employerService, times(1)).updateEmployer(any(EmployerDTO.class), eq(1L));
    }

    @Test
    void updateEmployer_WhenEmployerDoesNotExist_ShouldReturnNotFound() throws Exception {
        EmployerDTO employerDTO = createEmployerDTO();

        when(employerService.updateEmployer(any(EmployerDTO.class), eq(1L))).thenThrow(new EntityNotFoundException(CustomMessages.EMPLOYER_NOT_FOUND));

        ResultActions response = mockMvc.perform(put("/api/employers/update/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employerDTO)))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(CustomMessages.EMPLOYER_NOT_FOUND));

        verify(employerService, times(1)).updateEmployer(any(EmployerDTO.class), eq(1L));
    }

    @Test
    void deleteEmployer_WhenEmployerExists_ShouldReturnNoContent() throws Exception {

        ResultActions response = mockMvc.perform(delete("/api/employers/delete/1"))
                .andDo(print())
                .andExpect(status().isNoContent());

        verify(employerService, times(1)).deleteEmployer(eq(1L));

    }

    @Test
    void deleteEmployer_WhenEmployerDoesNotExist_ShouldReturnNotFound() throws Exception {

        doThrow(new EntityNotFoundException(CustomMessages.EMPLOYER_NOT_FOUND)).when(employerService).deleteEmployer(eq(1L));

        ResultActions response = mockMvc.perform(delete("/api/employers/delete/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());

        verify(employerService, times(1)).deleteEmployer(eq(1L));

    }

    @Test
    void getEmployerById_WhenEmployerExists_ShouldReturnEmployerDTO() throws Exception {

        EmployerDTO employerDTO = createEmployerDTO();

        when(employerService.getEmployerById(eq(1L))).thenReturn(employerDTO);

        ResultActions response = mockMvc.perform(get("/api/employers/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(employerDTO.getId()))
                .andExpect(jsonPath("$.name").value(employerDTO.getName()))
                .andExpect(jsonPath("$.email").value(employerDTO.getEmail()))
                .andExpect(jsonPath("$.position").value(employerDTO.getPosition()));

        verify(employerService, times(1)).getEmployerById(eq(1L));

    }

    @Test
    void getEmployerById_WhenEmployerDoesNotExist_ShouldReturnNotFound() throws Exception {
        when(employerService.getEmployerById(1L)).thenThrow(new EntityNotFoundException(CustomMessages.EMPLOYER_NOT_FOUND));

        ResultActions response = mockMvc.perform(get("/api/employers/1"))
                .andDo(print())
                .andExpect(status().isNotFound());

        verify(employerService, times(1)).getEmployerById(eq(1L));

    }

    @Test
    void getAllEmployers_ShouldReturnListOfEmployersDTO() throws Exception {
        List<EmployerDTO> employers = List.of(
                createEmployerDTO(),
                createEmployerDTO()
        );

        when(employerService.getAllEmployers()).thenReturn(employers);

        ResultActions response = mockMvc.perform(get("/api/employers/"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()").value(employers.size()));

        verify(employerService, times(1)).getAllEmployers();
    }

}
