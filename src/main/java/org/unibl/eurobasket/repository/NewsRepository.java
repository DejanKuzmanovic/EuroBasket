package org.unibl.eurobasket.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.unibl.eurobasket.model.News;

public interface NewsRepository extends JpaRepository<News, Integer> {
}
