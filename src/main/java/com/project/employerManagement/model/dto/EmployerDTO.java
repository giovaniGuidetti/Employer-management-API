package com.project.employerManagement.model.dto;

import com.project.employerManagement.config.CustomMessages;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmployerDTO {

    private long id;

    @Schema(description = "Name of the employer", example = "John Doe")
    @NotEmpty(message = CustomMessages.NAME_NOT_EMPTY)
    private String name;

    @Schema(description = "Email of the employer", example = "iJp6H@example.com")
    @NotEmpty(message = CustomMessages.EMAIL_NOT_EMPTY)
    private String email;

    @Schema(description = "Position of the employer", example = "Software Engineer")
    @NotEmpty(message = CustomMessages.POSITION_NOT_EMPTY)
    private String position;

}
