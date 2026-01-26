package edu.pga.psp.magiccollectionspring.controllers;

import edu.pga.psp.magiccollectionspring.models.dto.CardRequest;
import edu.pga.psp.magiccollectionspring.service.CardYouOwnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/your-cards")
public class CardYouOwnController {

    @Autowired
    private CardYouOwnService inventoryService;

    @PostMapping("/add")
    public ResponseEntity<?> addCard(@RequestBody CardRequest request) {
        try {
            return ResponseEntity.ok(inventoryService.addCardToCollection(request));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateCard(@PathVariable Long id, @RequestBody CardRequest request) {
        try {
            return ResponseEntity.ok(inventoryService.updateCardOwned(id, request));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/collection/{collectionId}")
    public ResponseEntity<?> listByCollection(@PathVariable Long collectionId) {
        try {
            return ResponseEntity.ok(inventoryService.getCardsByCollection(collectionId));
        } catch (Exception e) {
            return ResponseEntity.status(403).body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteCard(@PathVariable Long id) {
        try {
            inventoryService.deleteCard(id);
            return ResponseEntity.ok(Map.of("message", "Carta eliminada correctamente"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
