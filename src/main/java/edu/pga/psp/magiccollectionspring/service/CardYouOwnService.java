package edu.pga.psp.magiccollectionspring.service;


import edu.pga.psp.magiccollectionspring.mapper.CardMapper;
import edu.pga.psp.magiccollectionspring.models.Card;
import edu.pga.psp.magiccollectionspring.models.CardYouOwn;
import edu.pga.psp.magiccollectionspring.models.Collections;
import edu.pga.psp.magiccollectionspring.models.dto.CardRequest;
import edu.pga.psp.magiccollectionspring.models.enums.CardCondition;
import edu.pga.psp.magiccollectionspring.models.enums.Language;
import edu.pga.psp.magiccollectionspring.repository.CardYouOwnRepository;
import edu.pga.psp.magiccollectionspring.repository.CollectionsRepository;
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

    @Autowired private CardMapper mapper;

    @Transactional
    public CardYouOwn addCardToCollection(CardRequest request) {
        Collections coll = collRepo.findById(request.getCollectionId())
                .orElseThrow(() -> new RuntimeException("Colección no encontrada"));

        validateOwnership(coll.getOwner().getUsername());

        Card masterCard = cardService.getOrFetchCard(request.getCardName());

        CardCondition cond = CardCondition.fromString(request.getCondition());
        Language lang = Language.fromCode(request.getLanguage());

        return inventoryRepo.findExactCardInCollection(coll.getId(), masterCard.getId(), cond, request.isFoil(), lang)
                .map(existing -> {
                    existing.setQuantity(existing.getQuantity() + request.getQuantity());
                    return inventoryRepo.save(existing);
                })
                .orElseGet(() -> {
                    CardYouOwn newEntry = mapper.toInventoryEntity(request, coll, masterCard);
                    return inventoryRepo.save(newEntry);
                });
    }

    @Transactional
    public CardYouOwn updateCardOwned(Long id, CardRequest request) {
        CardYouOwn original = inventoryRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Registro no encontrado"));

        validateOwnership(original.getCollection().getOwner().getUsername());

        CardCondition newCond = CardCondition.fromString(request.getCondition());
        Language newLang = Language.fromCode(request.getLanguage());

        return inventoryRepo.findExactCardInCollection(original.getCollection().getId(), original.getCardMasterData().getId(), newCond, request.isFoil(), newLang)
                .map(conflict -> {
                    if (!conflict.getId().equals(id)) {
                        conflict.setQuantity(conflict.getQuantity() + request.getQuantity());
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

    private void validateOwnership(String ownerUsername) {
        String current = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!ownerUsername.equals(current)) {
            throw new RuntimeException("Acceso denegado: No eres el propietario");
        }
    }

}

