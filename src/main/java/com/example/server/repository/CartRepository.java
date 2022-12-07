package com.example.server.repository;

import com.example.server.entity.Cart;
import com.example.server.entity.ids.CartId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface CartRepository extends JpaRepository<Cart, CartId> {
    @Modifying
    @Query("delete from Cart c where c.user.id = ?1")
    void deleteByUserId(Long id);
}
