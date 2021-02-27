package org.unibl.eurobasket.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.unibl.eurobasket.model.Game;

@Repository
public interface GameRepository extends JpaRepository<Game, Integer> {
}
