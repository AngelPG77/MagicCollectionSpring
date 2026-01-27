package pga.magiccollectionspring.repository;

import pga.magiccollectionspring.models.Collections;
import pga.magiccollectionspring.models.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository
public interface CollectionsRepository extends JpaRepository<Collections, Long> {

    List<Collections> findByOwner_Username(String username);

    boolean existsByNameAndOwner(String name, Users owner);

}
