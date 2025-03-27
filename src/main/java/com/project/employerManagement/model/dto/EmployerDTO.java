package com.project.employerManagement.model.dto;

import com.project.employerManagement.config.CustomMessages;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmployerDTO {

    private long id;

    @NotEmpty(message = CustomMessages.NAME_NOT_EMPTY)
    private String name;

    @NotEmpty(message = CustomMessages.EMAIL_NOT_EMPTY)
    private String email;

    @NotEmpty(message = CustomMessages.POSITION_NOT_EMPTY)
    private String position;

}
