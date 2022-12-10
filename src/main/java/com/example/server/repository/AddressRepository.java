package com.example.server.repository;

import com.example.server.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
    @Query("select a from Address a where a.user.id = ?1 and  a.active = ?2")
    List<Address> findAddressByUser(Long userId, boolean active);
    @Query("select a from Address  a where a.user.id = ?1")
    List<Address> findAllAddressesByUser(Long userId);
}
