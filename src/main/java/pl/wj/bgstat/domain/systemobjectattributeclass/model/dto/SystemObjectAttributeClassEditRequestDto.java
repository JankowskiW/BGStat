package pl.wj.bgstat.domain.systemobjectattributeclass.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SystemObjectAttributeClassEditRequestDto {
    @NotNull(message = "Required field cannot be null")
    private boolean required;
    @Length(max = 150, message = "Class default value cannot be longer than 150 characters")
    private String classDefaultValue;
}
