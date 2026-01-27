package pga.magiccollectionspring.cli;

import pga.magiccollectionspring.models.dto.CardRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

@Component
public class CollectionMenu {

    private final Scanner scanner;
    private final RestClient client;
    private final UserSession session;
    private String currCollName;

    @Autowired
    public CollectionMenu(Scanner scanner, UserSession session) {
        this.scanner = scanner;
        this.session = session;
        this.client = RestClient.builder().baseUrl("http://localhost:8080").build();
    }

    public void show(Long collId, String collName) {
        boolean back = false;
        currCollName = collName;

        while (!back) {
            System.out.println("\n--- GESTIONANDO: " + currCollName + " ---");
            System.out.println("1. Ver cartas");
            System.out.println("2. Añadir carta");
            System.out.println("3. Actualizar carta");
            System.out.println("4. Buscar en esta colección");
            System.out.println("5. Actualizar nombre colección");
            System.out.println("6. Eliminar colección");
            System.out.println("0. Volver");
            System.out.print("Opción: ");

            String op = scanner.nextLine();
            switch (op) {
                case "1" -> listCardsInCollection(collId);
                case "2" -> addCardToCollection(collId);
                case "3" -> updateCardInCollection(collId);
                case "4" -> searchInCollection(collId);
                case "5" -> updateCollectionName(collId);
                case "6" -> {
                    deleteCollection(collId);
                    back = true;
                }
                case "0" -> back = true;
            }
        }
    }

    private void listCardsInCollection(Long collId) {
        try {
            List<Map> cards = getCards(collId);

            if (cards.isEmpty()) System.out.println("Colección vacía.");
            else {
                System.out.println("CARTAS:");
                for (Map c : cards) {
                    Map master = (Map) c.get("cardMasterData");
                    System.out.printf("[%d] %dx %s (%s) [%s] Foil: %s\n",
                            c.get("id"),
                            c.get("quantity"),
                            master.get("name"),
                            c.get("cardCondition"),
                            c.get("language"),
                            c.get("isFoil"));
                }
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private List<Map> getCards(Long collId) {
        return client.get()
                .uri("/your-cards/collection/" + collId)
                .header("Authorization", "Bearer " + session.getAuthToken())
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
    }

    private void addCardToCollection(Long collId) {
        System.out.println("\n--- AÑADIR CARTA ---");
        System.out.print("Nombre de la carta (Exacto o Scryfall): ");
        String name = scanner.nextLine();

        System.out.print("Cantidad (1): ");
        String qtyStr = scanner.nextLine();
        int quantity = qtyStr.isEmpty() ? 1 : Integer.parseInt(qtyStr);

        System.out.print("Condición (NEAR_MINT, EXCELLENT, GOOD, POOR...): ");
        String condition = scanner.nextLine();
        if (condition.isEmpty()) condition = "NEAR_MINT";

        System.out.print("Idioma (en, es, jp...): ");
        String lang = scanner.nextLine();
        if (lang.isEmpty()) lang = "en";

        System.out.print("¿Es Foil? (s/n): ");
        boolean isFoil = scanner.nextLine().equalsIgnoreCase("s");

        CardRequest req = new CardRequest();
        req.setCollectionId(collId);
        req.setCardName(name);
        req.setQuantity(quantity);
        req.setCondition(condition);
        req.setLanguage(lang);
        req.setIsFoil(isFoil);

        try {
            client.post()
                    .uri("/your-cards/add")
                    .header("Authorization", "Bearer " + session.getAuthToken())
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(req)
                    .retrieve()
                    .toBodilessEntity();
            System.out.println(">> Carta añadida.");
        } catch (Exception e) {
            System.out.println("Error añadiendo carta: " + e.getMessage());
        }
    }

    private void updateCardInCollection(Long collId) {
        System.out.println("\n--- ACTUALIZAR CARTA ---");
        listCardsInCollection(collId);
        System.out.print("Introduce el ID de la carta a actualizar (el número entre corchetes []): ");
        
        try {
            Long cardId = Long.parseLong(scanner.nextLine());
            
            System.out.println("Deja en blanco para no cambiar el valor.");
            
            System.out.print("Nueva Cantidad: ");
            String qtyStr = scanner.nextLine();
            Integer quantity = qtyStr.isEmpty() ? null : Integer.parseInt(qtyStr);

            System.out.print("Nueva Condición: ");
            String condition = scanner.nextLine();
            if (condition.isEmpty()) condition = null;

            System.out.print("Nuevo Idioma: ");
            String lang = scanner.nextLine();
            if (lang.isEmpty()) lang = null;

            System.out.print("¿Es Foil? (s/n/vacio): ");
            String foilStr = scanner.nextLine();
            Boolean isFoil = null;
            if (!foilStr.isEmpty()) {
                isFoil = foilStr.equalsIgnoreCase("s");
            }

            CardRequest req = new CardRequest();
            req.setCollectionId(collId);
            req.setQuantity(quantity);
            req.setCondition(condition);
            req.setLanguage(lang);
            req.setIsFoil(isFoil);

            client.put()
                    .uri("/your-cards/update/" + cardId)
                    .header("Authorization", "Bearer " + session.getAuthToken())
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(req)
                    .retrieve()
                    .toBodilessEntity();
            System.out.println(">> Carta actualizada.");

        } catch (Exception e) {
            System.out.println("Error actualizando carta: " + e.getMessage());
        }
    }

    private void searchInCollection(Long collId) {
        System.out.print("Buscar en colección: ");
        String term = scanner.nextLine();
        try {
            List<Map> cards = client.get()
                    .uri("/your-cards/search/collection/" + collId + "?term=" + term)
                    .header("Authorization", "Bearer " + session.getAuthToken())
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {});

            System.out.println("Resultados: " + cards.size());
            cards.forEach(c -> {
                Map master = (Map) c.get("cardMasterData");
                System.out.println("- " + master.get("name"));
            });
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void updateCollectionName(Long collId) {
        System.out.print("Nuevo nombre: ");
        String name = scanner.nextLine();
        try {
            client.put()
                    .uri("/collections/update/" + collId)
                    .header("Authorization", "Bearer " + session.getAuthToken())
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Map.of("name", name))
                    .retrieve()
                    .toBodilessEntity();
            currCollName = name;
            System.out.println(">> Nombre actualizado.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void deleteCollection(Long collId) {
        System.out.print("¿Seguro? (s/n): ");
        if (scanner.nextLine().equalsIgnoreCase("s")) {
            try {
                client.delete()
                        .uri("/collections/delete/" + collId)
                        .header("Authorization", "Bearer " + session.getAuthToken())
                        .retrieve()
                        .toBodilessEntity();
                System.out.println(">> Colección eliminada.");
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
}
