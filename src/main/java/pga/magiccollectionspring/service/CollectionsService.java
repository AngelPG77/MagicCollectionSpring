package pga.magiccollectionspring.service;

import pga.magiccollectionspring.models.Collections;
import pga.magiccollectionspring.models.Users;
import pga.magiccollectionspring.repository.CollectionsRepository;
import pga.magiccollectionspring.repository.UsersRepository;
import jakarta.transaction.Transactional;
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


    private String getAuthenticatedUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    @Transactional
    public Collections createCollection(String name) {
        Users owner = getCurrentUser();

        if (collectionsRepository.existsByNameAndOwner(name, owner)) {
            throw new RuntimeException("Ya tienes una colección llamada '" + name + "'");
        }

        Collections collection = new Collections();
        collection.setName(name);
        collection.setOwner(owner);

        return collectionsRepository.save(collection);
    }


    public List<Collections> getCollectionsByUser() {
        return collectionsRepository.findByOwner_Username(getAuthenticatedUsername());
    }

    public Collections getCollectionById(Long id) {
        Collections coll = collectionsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Colección no encontrada"));
        validateOwnership(coll);
        return coll;
    }

    @Transactional
    public void deleteCollection(Long id) {
        Collections coll = getCollectionById(id);
        collectionsRepository.delete(coll);
    }

    @Transactional
    public Collections updateCollection(Long id, String newName) {
        Collections coll = getCollectionById(id);

        if (!coll.getName().equals(newName)) {
            if (collectionsRepository.existsByNameAndOwner(newName, coll.getOwner())) {
                throw new RuntimeException("Ya tienes otra colección llamada '" + newName + "'");
            }
            coll.setName(newName);
        }

        return collectionsRepository.save(coll);
    }

    private Users getCurrentUser() {
        return usersRepository.findByUsername(getAuthenticatedUsername())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado en sesión"));
    }

    private void validateOwnership(Collections coll) {
        if (!coll.getOwner().getUsername().equals(getAuthenticatedUsername())) {
            throw new RuntimeException("Acceso denegado: Esta colección no es tuya");
        }
    }

}