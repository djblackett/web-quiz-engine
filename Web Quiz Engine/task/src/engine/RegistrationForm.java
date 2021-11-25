package engine;

import engine.user.User;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;


@Data
public class RegistrationForm {

    @Email
    private String email;

    @NotBlank
    @Length(min = 5)
    private String password;


    public User toUser(PasswordEncoder passwordEncoder) {
        return new User(this.email, passwordEncoder.encode(this.password));
    }

}
