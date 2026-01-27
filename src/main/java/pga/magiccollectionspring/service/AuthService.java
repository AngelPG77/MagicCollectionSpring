package pga.magiccollectionspring.service;

import pga.magiccollectionspring.models.Users;
import pga.magiccollectionspring.models.dto.LoginRequest;
import pga.magiccollectionspring.repository.UsersRepository;
import pga.magiccollectionspring.utilities.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;


    public String register(Users user) {
        if (usersRepository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("El nombre de usuario ya existe");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        usersRepository.save(user);

        return "Usuario registrado con éxito. Ahora puedes hacer login.";
    }

    public String login(LoginRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        return jwtService.generateToken(request.getUsername());
    }
}
