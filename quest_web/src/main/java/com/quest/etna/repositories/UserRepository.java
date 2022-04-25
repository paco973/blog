package com.quest.etna.repositories;

import com.quest.etna.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository< User,Integer> {

    @Query(value = "SELECT * FROM User n WHERE n.username = :username", nativeQuery = true)
    public Optional<User> findByUsername(String username);

    @Query("SELECT n FROM User n WHERE n.username = :username and n.password = :password" )
    public Optional<User> findByUsernameAndPassword(String username, String password);
}
