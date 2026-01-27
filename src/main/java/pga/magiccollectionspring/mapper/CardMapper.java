package pga.magiccollectionspring.mapper;

import pga.magiccollectionspring.models.Card;
import pga.magiccollectionspring.models.CardYouOwn;
import pga.magiccollectionspring.models.Collections;
import pga.magiccollectionspring.models.dto.CardRequest;
import pga.magiccollectionspring.models.dto.CardScryfallDTO;
import pga.magiccollectionspring.models.enums.CardCondition;
import pga.magiccollectionspring.models.enums.Language;
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

        entity.setQuantity(request.getQuantity() != null ? request.getQuantity() : 1);
        entity.setFoil(request.getIsFoil() != null ? request.getIsFoil() : false);

        return entity;
    }

    public void updateInventoryEntityFromRequest(CardRequest request, CardYouOwn existing) {
        if (request == null || existing == null) return;

        if (request.getQuantity() != null) {
            existing.setQuantity(request.getQuantity());
        }
        
        if (request.getIsFoil() != null) {
            existing.setFoil(request.getIsFoil());
        }
        
        if (request.getCondition() != null) {
            existing.setCardCondition(CardCondition.fromString(request.getCondition()));
        }
        
        if (request.getLanguage() != null) {
            existing.setLanguage(Language.fromCode(request.getLanguage()));
        }
    }

}
