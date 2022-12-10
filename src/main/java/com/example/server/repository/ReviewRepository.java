package com.example.server.repository;

import com.example.server.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    @Query("SELECT count(r) from Review r where r.user.id = ?1 and r.order.id = ?2")
    long existsByUserAndOrder(Long userId, Long OrderId);
}
