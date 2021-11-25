package engine;


import com.google.gson.Gson;
import engine.user.User;
import engine.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/register")
public class RegistrationController {
    static Gson gson = new Gson();

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public RegistrationController(
            UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

//    @GetMapping
//    public String registerForm() {
//        return "registration";
//    }

    @PostMapping
    public ResponseEntity<String> processRegistration(@RequestBody String body) {
        try {
            System.out.println("in try block");

            RegistrationForm form = gson.fromJson(body, RegistrationForm.class);

            System.out.println("form created");
            System.out.println(form.getEmail());

            if (userRepository.findByUsername(form.getEmail()).isPresent()) {
                System.out.println("user already exists");
                // return "<h2>User already exists</h2>";
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            User user = form.toUser(passwordEncoder);
            if (!user.getUsername().contains("@") || !user.getUsername().contains(".")
                    || form.getPassword().length() < 5 || form.getPassword().trim().length() == 0) {
                System.out.println("Malformed username");
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            userRepository.save(user);
            System.out.println("saved user");
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            System.out.println("in catch block");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
