package account.service;

import account.entity.User;
import account.exception_handling.NewPasswordException;
import account.exception_handling.UserExistException;
import account.exception_handling.UserNotExistException;
import account.repository.UserRepository;
import account.security.BCryptEncoderConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = BCryptEncoderConfig.passwordEncoder();
    }

    public User findUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(UserNotExistException::new);
    }


    public User save(User user) {
        user.setEmail(user.getEmail().toLowerCase());
        User optionalUser = userRepository.findUserByEmail(user.getEmail());
        if (optionalUser != null) {
            throw new UserExistException();
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("ROLE_USER");
        userRepository.save(user);
        return user;
    }


    public void updatePassword(UserDetails userDetails, String newPassword) {
        User existingUser = findByEmail(userDetails.getUsername());
        if (passwordEncoder.matches(newPassword, existingUser.getPassword()))
            throw new NewPasswordException();
        existingUser.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(existingUser);
    }
}
