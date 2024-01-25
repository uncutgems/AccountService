package account.controller;

import account.dto.PasswordChangeDTO;
import account.entity.User;
import account.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;

@RestController
@RequestMapping("api")
@Validated
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/auth/signup")
    public ResponseEntity<?> signUp(@Valid @RequestBody User user, Errors errors){
        return new ResponseEntity<>(userService.save(user), HttpStatus.OK);
    }

    @PostMapping("/auth/changepass")
    public ResponseEntity<?> changePassWord(@AuthenticationPrincipal UserDetails userDetails,
                                            @RequestBody @Valid PasswordChangeDTO passwordChangeDTO, Errors errors) {
        if (errors.hasFieldErrors()) {
            FieldError fieldError = errors.getFieldError();
            assert fieldError != null;
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(fieldError.getDefaultMessage());
        }
        System.out.println("I wanna know wtf is going on " + userDetails.getPassword());
        userService.updatePassword(userDetails, passwordChangeDTO.getNew_password());
        HashMap<String, String> map = new HashMap<>();
        map.put("email", userDetails.getUsername());
        map.put("status", "The password has been updated successfully");
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @GetMapping("/empl/payment")
    public ResponseEntity<?> employeePayment(@AuthenticationPrincipal UserDetails userDetails) {
        String email = userDetails.getUsername();
        return new ResponseEntity<>(userService.findUserByEmail(email), HttpStatus.OK);
    }
}