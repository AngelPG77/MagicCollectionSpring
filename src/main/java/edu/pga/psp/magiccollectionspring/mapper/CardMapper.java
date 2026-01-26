package edu.pga.psp.magiccollectionspring.mapper;

import edu.pga.psp.magiccollectionspring.models.Card;
import edu.pga.psp.magiccollectionspring.models.CardYouOwn;
import edu.pga.psp.magiccollectionspring.models.Collections;
import edu.pga.psp.magiccollectionspring.models.dto.CardRequest;
import edu.pga.psp.magiccollectionspring.models.dto.CardScryfallDTO;
import edu.pga.psp.magiccollectionspring.models.enums.CardCondition;
import edu.pga.psp.magiccollectionspring.models.enums.Language;
import org.springframework.stereotype.Component;

@Component
public class CardMapper {

    public Card toEntity(CardScryfallDTO cardDTO) {

        if (cardDTO == null) return null;

        Card entity = new Card();
        entity.setScryfallId(cardDTO.getScryfallId());
        entity.setName(cardDTO.getName());
        entity.setSetCode(cardDTO.getSetCode());
        entity.setOracleText(cardDTO.getOracleText());
        entity.setTypeLine(cardDTO.getTypeLine());

        return entity;
    }

    public CardScryfallDTO toDTO(Card entity) {

        if (entity == null) return null;

        CardScryfallDTO dto = new CardScryfallDTO();
        dto.setScryfallId(entity.getScryfallId());
        dto.setName(entity.getName());
        dto.setSetCode(entity.getSetCode());
        dto.setOracleText(entity.getOracleText());
        dto.setTypeLine(entity.getTypeLine());

        return dto;
    }

    public CardYouOwn toInventoryEntity(CardRequest request, Collections collection, Card masterCard) {
        if (request == null) return null;

        CardYouOwn entity = new CardYouOwn();
        entity.setCollection(collection);
        entity.setCardMasterData(masterCard);

        entity.setCardCondition(CardCondition.fromString(request.getCondition()));
        entity.setLanguage(Language.fromCode(request.getLanguage()));

        entity.setQuantity(request.getQuantity());
        entity.setFoil(request.isFoil());

        return entity;
    }

    public void updateInventoryEntityFromRequest(CardRequest request, CardYouOwn existing) {
        if (request == null || existing == null) return;

        existing.setQuantity(request.getQuantity());
        existing.setFoil(request.isFoil());
        existing.setCardCondition(CardCondition.fromString(request.getCondition()));
        existing.setLanguage(Language.fromCode(request.getLanguage()));
    }

}
