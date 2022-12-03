package com.example.server.repository;

import com.example.server.entity.Food;
import com.example.server.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
    @Query("select distinct f.restaurant from Food f join f.categories cate where cate.id = ?1")
    List<Restaurant> findRestaurantByCategoryId(Long categoryId);
}
