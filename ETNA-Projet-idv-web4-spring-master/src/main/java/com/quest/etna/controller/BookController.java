package com.quest.etna.controller;


import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.quest.etna.model.Book;
import com.quest.etna.model.Erreur;
import com.quest.etna.model.JwtUserDetails;
import com.quest.etna.model.User;
import com.quest.etna.model.UserRole;
import com.quest.etna.repositories.UserRepository;
import com.quest.etna.service.BookService;


@CrossOrigin(origins = "*")
@RestController
public class BookController {
	@Autowired
    private final UserRepository userRepository;
   
	@Autowired
	private final BookService bookService;
    
    public BookController(UserRepository userRepository, BookService bookService) {
		this.userRepository = userRepository;
		this.bookService = bookService;
	}    
    
  	@PostMapping(value="/book")
  	public ResponseEntity<?> addbook(@RequestBody Book book) {
    try {
		JwtUserDetails userDetails = (JwtUserDetails) SecurityContextHolder.getContext()
	            .getAuthentication().getPrincipal();
		String userName = userDetails.getUsername();
		Optional<User> user = userRepository.findByUsername(userName);
		 if (user.get().getRole() == UserRole.ROLE_USER ) 
			 return ResponseEntity.status(HttpStatus.UNAUTHORIZED) 
				 .body(new Erreur("Utilisateur non habilité"));
		 
		
	    return this.bookService.createBook(book);
	    }
    	catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new Erreur("Mauvaise requête"));
        }
  	}
  	
  	@CrossOrigin(origins = "*")
 	@PutMapping(value="/book/{id}")
  	public ResponseEntity<?> updatebook(@RequestBody Book book, @PathVariable int id ) {
    try {
		JwtUserDetails userDetails = (JwtUserDetails) SecurityContextHolder.getContext()
	            .getAuthentication().getPrincipal();
		String userName = userDetails.getUsername();
		Optional<User> user = userRepository.findByUsername(userName);
		 if (user.get().getRole() == UserRole.ROLE_USER ) 
			 return ResponseEntity.status(HttpStatus.UNAUTHORIZED) 
				 .body(new Erreur("Utilisateur non habilité"));
		 
		return this.bookService.update(id, book);
	    }
    	catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new Erreur("Mauvaise requête"));
        }
  	}
 	
	@GetMapping("/books")
	public ResponseEntity<?> getList(@RequestParam(defaultValue="0") Integer page , @RequestParam(defaultValue="10") Integer limit){
		return ResponseEntity
                .status(HttpStatus.OK)
                .body( bookService.getList(page, limit));
	}
  	
  	
 
  	
	@DeleteMapping("/book/{id}")
    ResponseEntity<?> removeBooksById(@PathVariable int id){
		try {
			JwtUserDetails userDetails = (JwtUserDetails) SecurityContextHolder.getContext()
		            .getAuthentication().getPrincipal();
			String userName = userDetails.getUsername();
			Optional<User> user = userRepository.findByUsername(userName);
			 if (user.get().getRole() == UserRole.ROLE_USER ) 
				 return ResponseEntity.status(HttpStatus.UNAUTHORIZED) 
					 .body(new Erreur("Utilisateur non habilité"));
			 return bookService.delete(id);
			}
			catch (Exception ex) {
	        	ex.printStackTrace();
	        	return ResponseEntity
	                .status(HttpStatus.BAD_REQUEST)
	                .body(new Erreur("Mauvaise requête"));
	    }
  	}
	
	
	@GetMapping("/book/{id}")
    ResponseEntity<?> getBooksById(@PathVariable int id){
		return bookService.checkBookById(id);
  	}
  	
  	
 }
