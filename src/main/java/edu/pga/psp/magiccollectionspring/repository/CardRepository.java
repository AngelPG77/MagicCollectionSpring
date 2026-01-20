package edu.pga.psp.magiccollectionspring.repository;

import edu.pga.psp.magiccollectionspring.models.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {

    Optional<Card> findByScryfallId(String scryfallId);

    List<Card> findByNameContainingIgnoreCaseOrOracleTextContainingIgnoreCase(String name, String oracleText);

    List<Card> findBySetCodeIgnoreCase(String setCode);

    List<Card> findByTypeLineContainingIgnoreCase(String typeLine);

}
