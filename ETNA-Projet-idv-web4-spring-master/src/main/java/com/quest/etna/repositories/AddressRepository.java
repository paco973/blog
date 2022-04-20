package com.quest.etna.repositories;

import com.quest.etna.model.Address;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;

@CrossOrigin("*")
@Repository
public interface AddressRepository extends CrudRepository<Address, Integer> {
    
	@Query (value = "SELECT * FROM Address a WHERE a.user_id = :id", nativeQuery = true )
    List<Address> findAllForUserId(int id);
	
    Address findAddressById(int id);
    List<Address> findAll();   

    
}
