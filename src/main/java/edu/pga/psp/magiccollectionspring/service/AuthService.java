package edu.pga.psp.magiccollectionspring.service;

import edu.pga.psp.magiccollectionspring.models.Users;
import edu.pga.psp.magiccollectionspring.repository.UsersRepository;
import edu.pga.psp.magiccollectionspring.utilities.JwtUtils;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;


    public Users register(String username, String password) {
        if (usersRepository.existsByUsername(username)) {
            throw new RuntimeException("El usuario ya existe");
        }
        Users user = new Users();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        return usersRepository.save(user);
    }

    public String login(String username, String password) {
        Users user = usersRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Credenciales inválidas"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Credenciales inválidas");
        }

        return jwtUtils.generateToken(user);
    }
}
