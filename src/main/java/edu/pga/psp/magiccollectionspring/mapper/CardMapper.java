package edu.pga.psp.magiccollectionspring.mapper;

import edu.pga.psp.magiccollectionspring.models.Card;
import edu.pga.psp.magiccollectionspring.models.dto.CardScryfallDTO;
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

}
