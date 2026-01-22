package edu.pga.psp.magiccollectionspring.service;

import edu.pga.psp.magiccollectionspring.models.Collections;
import edu.pga.psp.magiccollectionspring.models.Users;
import edu.pga.psp.magiccollectionspring.repository.CollectionsRepository;
import edu.pga.psp.magiccollectionspring.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CollectionsService {

    @Autowired
    private CollectionsRepository collectionsRepository;

    @Autowired
    private UsersRepository usersRepository;

    public Collections createCollection(String name) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Users user = usersRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Collections collection = new Collections();
        collection.setName(name);
        collection.setOwner(user);

        return collectionsRepository.save(collection);
    }

    public List<Collections> getMyCollections() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return collectionsRepository.findByOwner_Username(username);
    }

    public void deleteCollection(Long id) {
        Collections coll = collectionsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Colección no encontrada"));

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!coll.getOwner().getUsername().equals(username)) {
            throw new RuntimeException("No tienes permiso para borrar esta colección");
        }

        collectionsRepository.delete(coll);
    }
}