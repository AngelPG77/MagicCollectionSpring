package edu.pga.psp.magiccollectionspring.service;


import edu.pga.psp.magiccollectionspring.models.Card;
import edu.pga.psp.magiccollectionspring.models.CardYouOwn;
import edu.pga.psp.magiccollectionspring.models.Collections;
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

    @Autowired
    private CardYouOwnRepository cardYouOwnRepository;

    @Autowired
    private CardService cardService;

    @Autowired
    private CollectionsRepository collectionsRepository;


    @Transactional
    public CardYouOwn addCardToCollection(Long collectionId, String cardName, int quantity, String conditionStr, boolean foil, String langStr) {

        CardCondition condition = CardCondition.fromString(conditionStr);
        Language language = Language.fromCode(langStr);

        Collections coll = collectionsRepository.findById(collectionId)
                .orElseThrow(() -> new RuntimeException("Colección no encontrada"));

        validateOwnership(coll.getOwner().getUsername());
        Card masterCard = cardService.getOrFetchCard(cardName);

        return cardYouOwnRepository.findExactCardInCollection(collectionId, masterCard.getId(), condition, foil, language)
                .map(existing -> {
            existing.setQuantity(existing.getQuantity() + quantity);
            return cardYouOwnRepository.save(existing);
        })
                .orElseGet(() -> {
            CardYouOwn newCard = new CardYouOwn();
            newCard.setCollection(coll);
            newCard.setCardMasterData(masterCard);
            newCard.setQuantity(quantity);
            newCard.setCardCondition(condition.toString());
            newCard.setFoil(foil);
            newCard.setLanguage(language.toString());
            return cardYouOwnRepository.save(newCard);
        });
    }


    @Transactional
    public CardYouOwn updateCardOwned(Long cardOwnedId, int newQuantity, String conditionStr, boolean newFoil, String langStr) {

        CardCondition newCondition = CardCondition.fromString(conditionStr);
        Language newLanguage = Language.fromCode(langStr);

        CardYouOwn original = cardYouOwnRepository.findById(cardOwnedId)
                .orElseThrow(() -> new RuntimeException("Registro de inventario no encontrado"));

        validateOwnership(original.getCollection().getOwner().getUsername());


        return cardYouOwnRepository.findExactCardInCollection(original.getCollection().getId(), original.getCardMasterData().getId(), newCondition, newFoil, newLanguage)
                .map(conflict -> {
            if (!conflict.getId().equals(cardOwnedId)) {
                conflict.setQuantity(conflict.getQuantity() + newQuantity);
                cardYouOwnRepository.delete(original);
                return cardYouOwnRepository.save(conflict);
            }
            return applyChanges(original, newQuantity, newCondition, newFoil, newLanguage);
        })
                .orElseGet(() -> applyChanges(original, newQuantity, newCondition, newFoil, newLanguage));
    }

    public List<CardYouOwn> getCardsByCollection(Long collectionId) {
        Collections coll = collectionsRepository.findById(collectionId)
                .orElseThrow(() -> new RuntimeException("Colección no encontrada"));

        validateOwnership(coll.getOwner().getUsername());
        return cardYouOwnRepository.findByCollection_Id(collectionId);
    }

    public void deleteCardFromCollection(Long id) {
        CardYouOwn cardOwned = cardYouOwnRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Carta no encontrada"));

        validateOwnership(cardOwned.getCollection().getOwner().getUsername());
        cardYouOwnRepository.delete(cardOwned);
    }

    private void validateOwnership(String ownerUsername) {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!ownerUsername.equals(currentUsername)) {
            throw new RuntimeException("Acceso denegado: No eres el dueño de este recurso");
        }
    }

    private CardYouOwn applyChanges(CardYouOwn record, int qty, CardCondition cond, boolean foil, Language lang) {
        record.setQuantity(qty);
        record.setCardCondition(cond.toString());
        record.setFoil(foil);
        record.setLanguage(lang.toString());
        return cardYouOwnRepository.save(record);
    }

}

