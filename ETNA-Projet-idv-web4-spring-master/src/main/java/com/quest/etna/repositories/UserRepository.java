package com.quest.etna.repositories;


import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.quest.etna.model.User;

@CrossOrigin("*")
@Repository
public interface UserRepository extends PagingAndSortingRepository<User , Integer>{
		
	@Query ("SELECT u FROM User u WHERE u.username=:nom" )
	public Optional<User> findByUsername(String nom);
	
		
	@Query ("SELECT u FROM User u ORDER BY u.id ASC" )
	public List<User> getListByPage(Pageable pageable);
	
	boolean existsUserByUsername(String username);
    List<User> findAll();

}
