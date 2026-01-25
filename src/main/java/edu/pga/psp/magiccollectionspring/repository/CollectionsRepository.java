package edu.pga.psp.magiccollectionspring.repository;

import edu.pga.psp.magiccollectionspring.models.Collections;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;


@Repository
public interface CollectionsRepository extends JpaRepository<Collections, Long> {

    List<Collections> findByOwner_Username(String username);

}
