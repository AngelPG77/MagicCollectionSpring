package edu.pga.psp.magiccollectionspring.repository;

import edu.pga.psp.magiccollectionspring.models.Collections;
import edu.pga.psp.magiccollectionspring.models.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CollectionsRepository extends JpaRepository<Collections, Long> {

    List<Collections> findByOwner(Users owner);

}
