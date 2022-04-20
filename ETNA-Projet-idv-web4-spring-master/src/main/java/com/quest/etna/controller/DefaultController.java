package com.quest.etna.controller;


import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import com.quest.etna.model.User;
import com.quest.etna.repositories.UserRepository;


@RestController
public class DefaultController {

	@Autowired
	private UserRepository userRepository;
	
	@GetMapping(value="/testSuccess")
	@ResponseStatus(HttpStatus.OK)
	public String testSuccess(){
		return "success";
	}
	
	@GetMapping(value="/testNotFound")
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public String testNotFound(){
		return "not found";
	}
	
	@GetMapping(value="/testError")
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public String testError(){
		return "error";
	}
	

	@GetMapping(value="/{name}")
	public Optional<User> getUser(@PathVariable("name") String nom){
		return userRepository.findByUsername(nom);
	}
	

	
}
