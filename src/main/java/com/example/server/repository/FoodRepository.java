package com.example.server.repository;

import com.example.server.entity.Food;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FoodRepository extends JpaRepository<Food, Long> {
    @Query("select f from Food f join f.categories cate where cate.id = ?1")
    List<Food> findFoodByCategoryId(Long categoryId);
    @Query("select f from Food f where f.id in (?1)")
    List<Food> findFoodByList(List<Long> ids);
}
