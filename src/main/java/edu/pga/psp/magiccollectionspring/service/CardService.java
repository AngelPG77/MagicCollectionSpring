package edu.pga.psp.magiccollectionspring.service;

import edu.pga.psp.magiccollectionspring.mapper.CardMapper;
import edu.pga.psp.magiccollectionspring.models.Card;
import edu.pga.psp.magiccollectionspring.models.dto.CardScryfallDTO;
import edu.pga.psp.magiccollectionspring.models.dto.ScryfallSearchResponse;
import edu.pga.psp.magiccollectionspring.repository.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Collections;
import java.util.List;


@Service
public class CardService {
    @Autowired
    private CardRepository cardRepository;
    @Autowired
    private CardMapper cardMapper;
    @Autowired
    private RestClient scryfallClient;

    public Card getOrFetchCard(String cardName) {
        return cardRepository.findByNameIgnoreCase(cardName)
                .orElseGet(() -> {
                    Card fetched = fetchFromScryfall(cardName);
                    if (fetched == null) {
                        throw new RuntimeException("La carta '" + cardName + "' no existe en la base de datos de Scryfall.");
                    }
                    return fetched;
                });
    }

    private Card fetchFromScryfall(String cardName) {
        try {
            CardScryfallDTO dto = scryfallClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/cards/named")
                            .queryParam("exact", cardName)
                            .build())
                    .retrieve()
                    .onStatus(status -> status.value() == 404, (request, response) -> {
                        throw new RuntimeException("Carta no encontrada en Scryfall");
                    })
                    .body(CardScryfallDTO.class);

            if (dto != null) {
                return cardRepository.save(cardMapper.toEntity(dto));
            }
        } catch (Exception e) {
            System.err.println("Error llamando a Scryfall: " + e.getMessage());
        }
        return null;
    }

    public List<CardScryfallDTO> searchCardsInScryfall(String query) {
        try {
            ScryfallSearchResponse response = scryfallClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/cards/search")
                            .queryParam("q", query)
                            .build())
                    .retrieve()
                    .body(ScryfallSearchResponse.class);

            if (response != null && response.getData() != null) {
                return response.getData();
            }
        } catch (Exception e) {
            System.err.println("Error buscando en Scryfall: " + e.getMessage());
        }
        return Collections.emptyList();
    }
}
