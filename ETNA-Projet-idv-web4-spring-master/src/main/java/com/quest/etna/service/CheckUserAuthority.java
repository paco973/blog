package com.quest.etna.service;

import com.quest.etna.model.Address;
import com.quest.etna.model.User;
import com.quest.etna.model.UserRole;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CheckUserAuthority {

    public Boolean checkAddressAuthority(User user, int addressId) {
        if (user.getRole() == UserRole.ROLE_ADMIN) {
            return true;
        } else {
            try {
                List<Address> addresses = user.getAddressesList();
                List<Address> userAllowedAddress = new ArrayList<>();
                for (Address address : addresses) {
                    if (address.getId() == addressId) {
                        userAllowedAddress.add(address);
                        break;
                    }
                }
                return userAllowedAddress.size() > 0;

            } catch (Exception ex) {
                return false;
            }
        }
    }


    public Boolean checkUserAuthority(User user, int userId) {
        if (user.getRole() == UserRole.ROLE_ADMIN) {
            return true;
        } else{
            return user.getId() == userId;
        }
    }
    
    public Boolean checkUserAuthority(User user) {
            return user.getRole() == UserRole.ROLE_ADMIN;
    }
    
}
