package account.dto;

import account.annotations.BreachedValidation;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Size;

@Getter
@Setter
@RequiredArgsConstructor
public class PasswordChangeDTO {
    @Size(min = 12, message = "Password length must be 12 chars minimum!")
    @BreachedValidation(message = "The password is in the hacker's database!")
    private String new_password;
}
