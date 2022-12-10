package com.example.server.controller;

import com.example.server.entity.Address;
import com.example.server.entity.Restaurant;
import com.example.server.entity.User;
import com.example.server.exception.ResourceNotFoundException;
import com.example.server.repository.AddressRepository;
import com.example.server.repository.RestaurantRepository;
import com.example.server.repository.UserRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.channels.ScatteringByteChannel;
import java.util.List;

@RestController
@RequestMapping("/api/address")
public class AddressController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RestaurantRepository restaurantRepository;
    @Autowired
    private AddressRepository addressRepository;
//    @PutMapping("/user/{userId}/address")
//    public ResponseEntity<?> addAddressToUser(@RequestBody Address address, @PathVariable("userId") Long id) {
//        if (!userRepository.existsById(id)) return ResponseEntity.badRequest().body("Not find user");
//        Address add = addressRepository.save(address);
//        User user = userRepository.findById(id).orElseThrow(() -> new NullPointerException("Not find user"));
//        user.addAddress(add);
//        userRepository.save(user);
//        return ResponseEntity.ok("add address to user success");
//    }
//    @PutMapping("/restaurant/{restaurantId}/address")
//    public ResponseEntity<?> addAddressToRestaurant(@RequestBody Address address, @PathVariable("restaurantId") Long id) {
//        if (!restaurantRepository.existsById(id)) return ResponseEntity.badRequest().body("Not find restaurant");
//        Address add = addressRepository.save(address);
//        Restaurant restaurant = restaurantRepository.findById(id).orElseThrow(() -> new NullPointerException("Not find restaurant"));
//        restaurant.setAddress(add);
//        restaurantRepository.save(restaurant);
//        return ResponseEntity.ok("add address to restaurant success");
//    }
//    @GetMapping("/test")
//    public List<User> getAll() {
//        return userRepository.findAll();
//    }
    @PostMapping
    public ResponseEntity<?> saveAddress(@RequestBody AddressRequest addressRequest) {
        Address address = new Address();
        if(addressRequest.getUserId() != null && userRepository.existsById(addressRequest.getUserId())) {
            User user = userRepository.findById(addressRequest.getUserId()).get();
            address.setUser(user);
        }
        address.setAwards(addressRequest.getAwards());
        address.setProvince(addressRequest.getProvince());
        address.setDetail(addressRequest.getDetail());
        address.setDistrict(addressRequest.getDistrict());
        address.setActive(addressRequest.getActive());
        if(addressRequest.getActive()) {
            try {
                List<Address> address1 = addressRepository.findAddressByUser(address.getUser().getId(), true);
                if(address1.size() != 0) address1.get(0).setActive(false);
                address.setActive(true);
                address1.add(address);
                addressRepository.saveAll(address1);
            }
            catch (NullPointerException e) {
                throw new ResourceNotFoundException("Địa chỉ update không thỏa mãn");
            }
        }
        addressRepository.save(address);
        return ResponseEntity.status(HttpStatus.CREATED).body("Tao dia chi thanh cong");
    }
    @PutMapping
    public ResponseEntity<?> updateAddress(@RequestBody AddressRequest addressRequest) {
        Address address = addressRepository.findById(addressRequest.getAddressId()).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy địa chỉ"));
        address.setAwards(addressRequest.getAwards());
        address.setProvince(addressRequest.getProvince());
        address.setDetail(addressRequest.getDetail());
        address.setDistrict(addressRequest.getDistrict());
        address.setActive(addressRequest.getActive());
        if(addressRequest.getActive()) {
            try {
                List<Address> address1 = addressRepository.findAddressByUser(address.getUser().getId(), true);
                if(address1.size() != 0) address1.get(0).setActive(false);
                address.setActive(true);
                address1.add(address);
                addressRepository.saveAll(address1);
            }
            catch (NullPointerException e) {
                throw new ResourceNotFoundException("Địa chỉ update không thỏa mãn");
            }
        }
        addressRepository.save(address);
        return ResponseEntity.ok().body("update dia chi thanh cong");
    }
}
@Data
class AddressRequest {
    private Long addressId;
    private String awards;
    private String district;
    private String detail;
    private String province;
    private Boolean active;
    private Long userId;
}
