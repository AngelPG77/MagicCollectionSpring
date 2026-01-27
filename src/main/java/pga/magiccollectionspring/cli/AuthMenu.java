package pga.magiccollectionspring.cli;

import pga.magiccollectionspring.models.dto.LoginRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Map;
import java.util.Scanner;

@Component
public class AuthMenu {

    private final Scanner scanner;
    private final RestClient client;
    private final UserSession session;

    @Autowired
    public AuthMenu(Scanner scanner, UserSession session) {
        this.scanner = scanner;
        this.session = session;
        this.client = RestClient.builder().baseUrl("http://localhost:8080").build();
    }

    public void show() {
        System.out.println("\n--- BIENVENIDO ---");
        System.out.println("1. Iniciar Sesión");
        System.out.println("2. Registrarse");
        System.out.println("3. Salir");
        System.out.print("Opción: ");

        String op = scanner.nextLine();
        switch (op) {
            case "1" -> login();
            case "2" -> register();
            case "3" -> System.exit(0);
            default -> System.out.println("Opción no válida");
        }
    }

    private void login() {
        System.out.print("Usuario: ");
        String user = scanner.nextLine();
        System.out.print("Contraseña: ");
        String pass = scanner.nextLine();

        try {
            Map response = client.post()
                    .uri("/auth/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new LoginRequest(user, pass))
                    .retrieve()
                    .body(Map.class);

            String token = (String) response.get("token");
            session.login(user, token);
            System.out.println(">> Login correcto.");
        } catch (Exception e) {
            System.out.println(">> Error de login: " + e.getMessage());
        }
    }

    private void register() {
        System.out.print("Nuevo Usuario: ");
        String user = scanner.nextLine();
        System.out.print("Nueva Contraseña: ");
        String pass = scanner.nextLine();

        try {
            client.post()
                    .uri("/auth/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new LoginRequest(user, pass))
                    .retrieve()
                    .toBodilessEntity();
            System.out.println(">> Registro exitoso. Ahora inicia sesión.");
        } catch (Exception e) {
            System.out.println(">> Error al registrar: " + e.getMessage());
        }
    }
}
