package pga.magiccollectionspring.controllers;

import pga.magiccollectionspring.service.CollectionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/collections")
public class CollectionsController {

    @Autowired
    private CollectionsService collectionsService;

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody Map<String, String> body) {
        try {
            String name = body.get("name");
            return ResponseEntity.ok(collectionsService.createCollection(name));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAll() {
        try {
            return ResponseEntity.ok(collectionsService.getCollectionsByUser());
        } catch (Exception e) {
            return ResponseEntity.status(401).body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Map<String, String> body) {
        try {
            String newName = body.get("name");
            return ResponseEntity.ok(collectionsService.updateCollection(id, newName));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            collectionsService.deleteCollection(id);
            return ResponseEntity.ok(Map.of("message", "Colección eliminada"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
