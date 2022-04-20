package com.quest.etna.service;

import com.google.gson.Gson;
import com.quest.etna.model.Address;
import com.quest.etna.model.Erreur;
import com.quest.etna.model.UserRole;
import com.quest.etna.model.Success;
import com.quest.etna.model.User;
import com.quest.etna.repositories.AddressRepository;
import com.quest.etna.repositories.UserRepository;
import org.apache.tomcat.util.json.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class AddressService {

    public final AddressRepository addressRepository;
    public final CheckUserAuthority userAuthority;

    @Autowired
    public AddressService(AddressRepository addressRepository, CheckUserAuthority userAuthority) {
        this.addressRepository = addressRepository;
        this.userAuthority = userAuthority;
    }

    public ResponseEntity<?> checkAllAddress(User user) {
        if (user.getRole() == UserRole.ROLE_ADMIN) {
            List<Address> listAddresses = this.addressRepository.findAll();
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(listAddresses);
        } else {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(new Erreur("User Role UNAUTHORIZED"));
        }
    }

    public ResponseEntity<?> checkAllAddressForUser(User user) {
        List<Address> listAddresses = this.addressRepository.findAllForUserId(user.getId());

        String json = listAddresses.toString();
        json = json.replaceAll("]", "}");
        json = json.replaceAll("\\[", "{");

        json = json.replaceAll("\\)", "}");
        json = json.replaceAll("\\(", "{");
        json = json.replaceAll(":", " ");
        json = json.replaceAll("=", ":");

        JSONParser parser = new JSONParser(json);


        return ResponseEntity
                .status(HttpStatus.OK)
                .body(parser);
    }


    public ResponseEntity<?> checkAddressById(User user, int addressId) {
        if (userAuthority.checkAddressAuthority(user, addressId)) {
            Address address = this.addressRepository.findAddressById(addressId);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(address);
        } else {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(new Erreur("User Role UNAUTHORIZED"));
        }
    }

    public ResponseEntity<?> createAddress(Address mAddress, User user, UserRepository userRepository) {
        user.setAddress(mAddress);
        mAddress.setUser(user);
        userRepository.save(user);
        addressRepository.save(mAddress);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(mAddress);
    }

    public ResponseEntity<?> updateAddress(int addressId, Address upAddress, User user) {
        if (userAuthority.checkAddressAuthority(user, addressId)) {
            Address oldAddress = this.addressRepository.findAddressById(addressId);
            if (upAddress.getCity() != null) {
                oldAddress.setCity(upAddress.getCity());
            }
            if (upAddress.getCountry() != null) {
                oldAddress.setCountry(upAddress.getCountry());
            }
            if (upAddress.getPostal_code() != null) {
                oldAddress.setPostal_code(upAddress.getPostal_code());
            }
            if (upAddress.getStreet() != null) {
                oldAddress.setStreet(upAddress.getStreet());
            }
            oldAddress.setUpdateDate(new Date());
            addressRepository.save(oldAddress);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(oldAddress);
        } else {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(new Erreur("User Role UNAUTHORIZED"));
        }
    }

    public ResponseEntity<?> deleteAddress(int addressId, User user) {
        if (userAuthority.checkAddressAuthority(user, addressId)) {
            Address address = this.addressRepository.findAddressById(addressId);
            this.addressRepository.delete(address);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new Success("TRUE"));
        } else {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(new Success("FALSE"));
        }
    }


}
