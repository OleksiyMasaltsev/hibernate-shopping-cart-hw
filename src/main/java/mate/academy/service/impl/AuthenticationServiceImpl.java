package mate.academy.service.impl;

import java.util.Optional;
import mate.academy.exception.AuthenticationException;
import mate.academy.exception.RegistrationException;
import mate.academy.lib.Inject;
import mate.academy.lib.Service;
import mate.academy.model.User;
import mate.academy.service.AuthenticationService;
import mate.academy.service.UserService;
import mate.academy.util.HashUtil;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    private static final String EMAIL_PATTERN = "[a-zA-Z0-9_!#$%&’*+=?`{|}~^.-]+@[a-zA-Z0-9.-]+";
    @Inject
    private UserService userService;

    @Override
    public User login(String email, String password) throws AuthenticationException {
        Optional<User> optionalUser = userService.findByEmail(email);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            String hashedPassword = HashUtil.hashPassword(password, user.getSalt());
            if (user.getPassword().equals(hashedPassword)) {
                return user;
            }
        }
        throw new AuthenticationException("Can't login the user with email: " + email);
    }

    @Override
    public User register(String email, String password) throws RegistrationException {
        checkParameters(email, password);
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        return userService.add(user);
    }

    private void checkParameters(String email, String password) throws RegistrationException {
        if (userService.findByEmail(email).isPresent()) {
            throw new RegistrationException("User with email: " + email + " already exists");
        }
        if (!email.matches(EMAIL_PATTERN)) {
            throw new RegistrationException("Email: " + email + " is invalid");
        }
        if (password.isEmpty()) {
            throw new RegistrationException("Password is invalid");
        }
    }
}
