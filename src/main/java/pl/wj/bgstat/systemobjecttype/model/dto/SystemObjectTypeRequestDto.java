package pl.wj.bgstat.systemobjecttype.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SystemObjectTypeRequestDto {
    @NotBlank(message = "System object type name cannot be blank")
    @Length(max = 100, message = "System object type name cannot be longer than 100 characters")
    private String name;
    @NotBlank(message = "System object type description cannot be blank")
    @Length(max = 500, message = "System object type description cannot be longer than 500 characters")
    private String description;
    private boolean archived;
}
