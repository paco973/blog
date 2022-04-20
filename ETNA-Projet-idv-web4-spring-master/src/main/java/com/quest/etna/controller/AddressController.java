package com.quest.etna.controller;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.quest.etna.model.Address;
import com.quest.etna.model.JwtUserDetails;
import com.quest.etna.model.User;
import com.quest.etna.model.UserRole;
import com.quest.etna.repositories.UserRepository;
import com.quest.etna.service.AddressService;

@Controller
public class AddressController {

    private final UserRepository userRepository;
    private final AddressService addressService;
	
    public AddressController(UserRepository userRepository, AddressService addressService) {
		this.userRepository = userRepository;
		this.addressService = addressService;
	}
	

    
    @GetMapping("/address")
    ResponseEntity<?> getAllAddressesForUser()
    {
        JwtUserDetails userDetails = (JwtUserDetails) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        String userName = userDetails.getUsername();
        Optional<User> user = userRepository.findByUsername(userName);
        if (user.get().getRole() == UserRole.ROLE_ADMIN)
        	return addressService.checkAllAddress(user.get());
        else return addressService.checkAllAddressForUser(user.get());
    }


    @GetMapping("/address/{id}")
    ResponseEntity<?> getAddressById(@PathVariable int id)
    {
        JwtUserDetails userDetails = (JwtUserDetails) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        String userName = userDetails.getUsername();
        Optional<User> user = userRepository.findByUsername(userName);
        return addressService.checkAddressById(user.get(), id);
    }

    @PostMapping("/address")
    ResponseEntity<?> createAddress(@RequestBody Address mAddress){
        JwtUserDetails userDetails = (JwtUserDetails) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        String userName = userDetails.getUsername();
        Optional<User> user = userRepository.findByUsername(userName);
        return addressService.createAddress(mAddress, user.get(), userRepository);
    }

    @PutMapping("/address/{address}")
    ResponseEntity<?> updateAddress(@PathVariable(value = "address") int addressId,
                                         @RequestBody Address mAddress)
    {
        JwtUserDetails userDetails = (JwtUserDetails) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        String userName = userDetails.getUsername();
        Optional<User> user = userRepository.findByUsername(userName);
        return addressService.updateAddress(addressId, mAddress, user.get());
    }

    
    @DeleteMapping("/address/{address}")
    ResponseEntity<?> deleteAddress(@PathVariable(value = "address") int addressId){
        JwtUserDetails userDetails = (JwtUserDetails) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        String userName = userDetails.getUsername();
        Optional<User> user = userRepository.findByUsername(userName);
        return addressService.deleteAddress(addressId, user.get());
    }

}
