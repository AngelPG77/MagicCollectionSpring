package edu.pga.psp.magiccollectionspring.service;


import edu.pga.psp.magiccollectionspring.mapper.CardMapper;
import edu.pga.psp.magiccollectionspring.models.Card;
import edu.pga.psp.magiccollectionspring.models.CardYouOwn;
import edu.pga.psp.magiccollectionspring.models.Collections;
import edu.pga.psp.magiccollectionspring.models.Users;
import edu.pga.psp.magiccollectionspring.models.dto.CardRequest;
import edu.pga.psp.magiccollectionspring.models.enums.CardCondition;
import edu.pga.psp.magiccollectionspring.models.enums.Language;
import edu.pga.psp.magiccollectionspring.repository.CardYouOwnRepository;
import edu.pga.psp.magiccollectionspring.repository.CollectionsRepository;
import edu.pga.psp.magiccollectionspring.repository.UsersRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CardYouOwnService {

    @Autowired private CardYouOwnRepository inventoryRepo;

    @Autowired private CardService cardService;

    @Autowired private CollectionsRepository collRepo;
    
    @Autowired private UsersRepository usersRepo;

    @Autowired private CardMapper mapper;

    @Transactional
    public CardYouOwn addCardToCollection(CardRequest request) {
        Collections coll = collRepo.findById(request.getCollectionId())
                .orElseThrow(() -> new RuntimeException("Colección no encontrada"));

        validateOwnership(coll.getOwner().getUsername());

        Card masterCard = cardService.getOrFetchCard(request.getCardName());


        CardCondition cond = request.getCondition() != null ? CardCondition.fromString(request.getCondition()) : CardCondition.NEAR_MINT;
        Language lang = request.getLanguage() != null ? Language.fromCode(request.getLanguage()) : Language.ENGLISH;
        boolean isFoil = request.getIsFoil() != null ? request.getIsFoil() : false;
        int quantityToAdd = request.getQuantity() != null ? request.getQuantity() : 1;

        return inventoryRepo.findExactCardInCollection(coll.getId(), masterCard.getId(), cond, isFoil, lang)
                .map(existing -> {
                    existing.setQuantity(existing.getQuantity() + quantityToAdd);
                    return inventoryRepo.save(existing);
                })
                .orElseGet(() -> {
                    request.setCondition(cond.name());
                    request.setLanguage(lang.getCode());
                    request.setIsFoil(isFoil);
                    request.setQuantity(quantityToAdd);
                    
                    CardYouOwn newEntry = mapper.toInventoryEntity(request, coll, masterCard);
                    return inventoryRepo.save(newEntry);
                });
    }

    @Transactional
    public CardYouOwn updateCardOwned(Long id, CardRequest request) {
        CardYouOwn original = inventoryRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Registro no encontrado"));

        validateOwnership(original.getCollection().getOwner().getUsername());

        CardCondition targetCond = request.getCondition() != null
                ? CardCondition.fromString(request.getCondition()) 
                : original.getCardCondition();
        
        Language targetLang = request.getLanguage() != null 
                ? Language.fromCode(request.getLanguage()) 
                : original.getLanguage();
        
        boolean targetFoil = request.getIsFoil() != null
                ? request.getIsFoil()
                : original.isFoil();


        return inventoryRepo.findExactCardInCollection(original.getCollection().getId(), original.getCardMasterData().getId(), targetCond, targetFoil, targetLang)
                .map(conflict -> {
                    if (!conflict.getId().equals(id)) {
                        int numToAdd = request.getQuantity() != null ? request.getQuantity() : original.getQuantity();
                        conflict.setQuantity(conflict.getQuantity() + numToAdd);
                        inventoryRepo.delete(original);
                        return inventoryRepo.save(conflict);
                    }
                    mapper.updateInventoryEntityFromRequest(request, original);
                    return inventoryRepo.save(original);
                })
                .orElseGet(() -> {
                    mapper.updateInventoryEntityFromRequest(request, original);
                    return inventoryRepo.save(original);
                });
    }

    public List<CardYouOwn> getCardsByCollection(Long collectionId) {
        Collections coll = collRepo.findById(collectionId)
                .orElseThrow(() -> new RuntimeException("Colección no encontrada"));
        validateOwnership(coll.getOwner().getUsername());
        return inventoryRepo.findByCollection_Id(collectionId);
    }

    public void deleteCard(Long id) {
        CardYouOwn record = inventoryRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Registro no encontrado"));
        validateOwnership(record.getCollection().getOwner().getUsername());
        inventoryRepo.delete(record);
    }
    
    public List<CardYouOwn> searchGlobal(String term) {
        Users user = getCurrentUser();
        return inventoryRepo.searchInMyGlobalInventory(user.getId(), term);
    }

    public List<CardYouOwn> searchInCollection(Long collectionId, String term) {
        Collections coll = collRepo.findById(collectionId)
                .orElseThrow(() -> new RuntimeException("Colección no encontrada"));
        validateOwnership(coll.getOwner().getUsername());
        
        return inventoryRepo.searchInSpecificCollection(collectionId, term);
    }

    public List<CardYouOwn> searchByType(String type) {
        Users user = getCurrentUser();
        return inventoryRepo.searchMyCardsByType(user.getId(), type);
    }

    private void validateOwnership(String ownerUsername) {
        String current = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!ownerUsername.equals(current)) {
            throw new RuntimeException("Acceso denegado: No eres el propietario");
        }
    }
    
    private Users getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return usersRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

}
