package org.unibl.eurobasket.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.unibl.eurobasket.model.Comment;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
}
