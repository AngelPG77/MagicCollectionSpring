package pga.magiccollectionspring.controllers;

import pga.magiccollectionspring.models.Card;
import pga.magiccollectionspring.repository.CardRepository;
import pga.magiccollectionspring.service.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/cards")
public class CardController {

    @Autowired
    private CardService cardService;

    @Autowired
    private CardRepository cardRepository;

    @GetMapping("/search")
    public ResponseEntity<?> getCardByName(@RequestParam String name) {
        try {
            Card card = cardService.getOrFetchCard(name);
            return ResponseEntity.ok(card);
        } catch (Exception e) {
            return ResponseEntity.status(404).body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/library")
    public ResponseEntity<List<Card>> getAllKnownCards() {
        return ResponseEntity.ok(cardRepository.findAll());
    }

    @GetMapping("/discover")
    public ResponseEntity<?> discoverCards(@RequestParam String query) {
        try {
            return ResponseEntity.ok(cardService.searchCardsInScryfall(query));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCardById(@PathVariable Long id) {
        return cardRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

}
