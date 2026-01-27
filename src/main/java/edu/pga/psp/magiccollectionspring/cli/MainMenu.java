package edu.pga.psp.magiccollectionspring.cli;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

@Component
public class MainMenu {

    private final Scanner scanner;
    private final RestClient client;
    private final UserSession session;
    private final CollectionMenu collectionMenu;

    @Autowired
    public MainMenu(Scanner scanner, UserSession session, CollectionMenu collectionMenu) {
        this.scanner = scanner;
        this.session = session;
        this.collectionMenu = collectionMenu;
        this.client = RestClient.builder().baseUrl("http://localhost:8080").build();
    }

    public void show() {
        System.out.println("\n--- MENÚ PRINCIPAL (" + session.getUsername() + ") ---");
        System.out.println("1. Mis Colecciones");
        System.out.println("2. Crear Colección");
        System.out.println("3. Buscar Cartas (Global/Scryfall)");
        System.out.println("4. Cerrar Sesión");
        System.out.print("Opción: ");

        String op = scanner.nextLine();
        switch (op) {
            case "1" -> listCollections();
            case "2" -> createCollection();
            case "3" -> searchGlobal();
            case "4" -> session.logout();
            default -> System.out.println("Opción no válida");
        }
    }

    private void listCollections() {
        try {
            List<Map> collections = client.get()
                    .uri("/collections/all")
                    .header("Authorization", "Bearer " + session.getAuthToken())
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {});

            if (collections.isEmpty()) {
                System.out.println("No tienes colecciones.");
                return;
            }

            System.out.println("\nTUS COLECCIONES:");
            for (int i = 0; i < collections.size(); i++) {
                System.out.println((i + 1) + ". " + collections.get(i).get("name") + " (ID: " + collections.get(i).get("id") + ")");
            }
            System.out.println("0. Volver");
            System.out.print("Selecciona una colección para gestionarla: ");

            try {
                int selection = Integer.parseInt(scanner.nextLine());
                if (selection > 0 && selection <= collections.size()) {
                    Long collId = ((Number) collections.get(selection - 1).get("id")).longValue();
                    String collName = (String) collections.get(selection - 1).get("name");
                    collectionMenu.show(collId, collName);
                }
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida.");
            }

        } catch (Exception e) {
            System.out.println("Error listando colecciones: " + e.getMessage());
        }
    }

    private void createCollection() {
        System.out.print("Nombre de la nueva colección: ");
        String name = scanner.nextLine();
        try {
            client.post()
                    .uri("/collections/create")
                    .header("Authorization", "Bearer " + session.getAuthToken())
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Map.of("name", name))
                    .retrieve()
                    .toBodilessEntity();
            System.out.println(">> Colección creada.");
        } catch (Exception e) {
            System.out.println("Error creando colección: " + e.getMessage());
        }
    }

    private void searchGlobal() {
        System.out.print("Término de búsqueda (Scryfall): ");
        String query = scanner.nextLine();
        try {
            List<Map> cards = client.get()
                    .uri("/cards/discover?query=" + query)
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {});

            System.out.println("Resultados encontrados: " + cards.size());
            cards.forEach(c -> System.out.println("- " + c.get("name") + " (" + c.get("set_name") + ")"));
        } catch (Exception e) {
            System.out.println("Error buscando: " + e.getMessage());
        }
    }
}
