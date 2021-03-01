package org.unibl.eurobasket.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.unibl.eurobasket.model.Comment;
import org.unibl.eurobasket.model.News;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Integer> {

    List<Comment> findAllByNews(News news);

}
