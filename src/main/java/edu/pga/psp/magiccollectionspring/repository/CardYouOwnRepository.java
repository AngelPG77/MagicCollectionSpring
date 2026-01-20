package edu.pga.psp.magiccollectionspring.repository;


import edu.pga.psp.magiccollectionspring.models.CardYouOwn;
import edu.pga.psp.magiccollectionspring.models.Collections;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CardYouOwnRepository extends JpaRepository<CardYouOwn, Long> {

    List<CardYouOwn> findByCollection(Collections collection);


    @Query("SELECT c FROM CardYouOwn c WHERE c.collection.owner.id = :userId " +
            "AND (LOWER(c.cardMasterData.name) LIKE LOWER(CONCAT('%', :term, '%')) " +
            "OR LOWER(c.cardMasterData.oracleText) LIKE LOWER(CONCAT('%', :term, '%')))")
    List<CardYouOwn> searchInMyGlobalInventory(@Param("userId") Long userId, @Param("term") String term);


    @Query("SELECT c FROM CardYouOwn c WHERE c.collection.id = :collId " +
            "AND (LOWER(c.cardMasterData.name) LIKE LOWER(CONCAT('%', :term, '%')))")
    List<CardYouOwn> searchInSpecificCollection(@Param("collId") Long collId, @Param("term") String term);

    @Query("SELECT c FROM CardYouOwn c WHERE c.collection.owner.id = :userId " +
            "AND LOWER(c.cardMasterData.typeLine) LIKE LOWER(CONCAT('%', :type, '%'))")
    List<CardYouOwn> searchMyCardsByType(@Param("userId") Long userId, @Param("type") String type);

}
