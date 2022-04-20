package com.quest.etna.controller;


import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
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
import com.fasterxml.jackson.databind.ObjectMapper;
import com.quest.etna.config.Encryption;
import com.quest.etna.config.JwtCheckingService;
import com.quest.etna.config.JwtTokenUtil;
import com.quest.etna.model.Address;
//import com.quest.etna.config.JwtUserDetailsService;
import com.quest.etna.model.Erreur;
import com.quest.etna.model.JwtUserDetails;
import com.quest.etna.model.User;
import com.quest.etna.model.UserRole;
import com.quest.etna.repositories.AddressRepository;
import com.quest.etna.repositories.UserRepository;
import com.quest.etna.service.AddressService;

@CrossOrigin(origins = "*")
@RestController
public class AuthenticationController {

	@Autowired
	UserRepository userRepository;
	
	@Autowired
	private AuthenticationManager authentificationManager;
		
	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	private Encryption passwordEncoder;

	private JwtCheckingService jwtCheckingService;
	
	
	private AddressRepository addressRepository;
	
    public AuthenticationController(UserRepository userRepository,JwtCheckingService jwtCheckingService,
	    Encryption passwordEncoder, AddressService addressService,
	    AddressRepository addressRepository) {
		this.userRepository = userRepository;
		this.jwtCheckingService = jwtCheckingService;
		this.passwordEncoder = passwordEncoder;
		this.addressRepository = addressRepository;
	}
	
  
	@PostMapping(value="/register")
	public ResponseEntity<HashMap<String, String>> enregister(@RequestBody User user) {

		HashMap<String, String> result = new HashMap<String, String>();
		String username = user.getUsername();
		
		//Bad request 
		if (username == null || user.getPassword() == null) {
			result.put("status","requête invalide");
			return new ResponseEntity<HashMap<String, String>>(result, HttpStatus.BAD_REQUEST);
		}
		
		//The username already exist
		else if (!userRepository.findByUsername(username).isEmpty()) {
			result.put("status","username déjà utilisé");
			return new ResponseEntity<HashMap<String, String>>(result, HttpStatus.CONFLICT);
		}
		//create a new user
		else {
			User u = new User(username, passwordEncoder.passwordEncoder().encode(user.getPassword()));
			userRepository.save(u);
			result.put("username", username);
			result.put("role", u.getRole().toString());
			return new ResponseEntity<HashMap<String, String>>(result , HttpStatus.CREATED);
			}
	}
	
	
	
   @PostMapping("/authenticate")
    ResponseEntity<?> authenticate(@RequestBody User mUser) {
        try {
            jwtCheckingService.setupUser(mUser.getUsername(),
                    mUser.getPassword());
           
            String mToken = jwtCheckingService.getValidToken();
            ObjectMapper mapper = new ObjectMapper();
            HashMap<String, String> result = new HashMap<String, String>();
            result.put("token", mToken);

            String jsonString = mapper.writeValueAsString(result);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(jsonString);
        } catch (Exception ex) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(new Erreur("Mauvais utilisateur / mot de passe"));
        }
    }
   


   @PutMapping("/update")
    public ResponseEntity<?> update(@RequestBody User userbody) {
        try {
            JwtUserDetails userDetails = (JwtUserDetails) SecurityContextHolder.getContext()
                    .getAuthentication().getPrincipal();
            String userName = userDetails.getUsername();
            Optional<User> user = userRepository.findByUsername(userName);
            
            if ( userbody.getUsername() != "")  user.get().setUsername(userbody.getUsername());
            if (userbody.getPassword() != "") 
            	user.get().setPassword(passwordEncoder.passwordEncoder().encode(userbody.getPassword()));
            if (userbody.getRole().toString() != "")
            	user.get().setRole(UserRole.ROLE_ADMIN);
            if (userbody.getUsername() != "" || userbody.getPassword() != "" )
            	userRepository.save(user.get());

            String mToken = jwtCheckingService.getValidToken();
            ObjectMapper mapper = new ObjectMapper();
            HashMap<String, String> result = new HashMap<String, String>();
            result.put("token", mToken);

            String jsonString = mapper.writeValueAsString(result);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(jsonString);
        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new Erreur("Error"));
        }
    }
   

//fonction who i am 
 //return l'identité de la personne connectée 
   @GetMapping("/me")
    public ResponseEntity<?> me() {
        try {
            JwtUserDetails userDetails = (JwtUserDetails) SecurityContextHolder.getContext()
                    .getAuthentication().getPrincipal();
            String userName = userDetails.getUsername();
            Optional<User> user = userRepository.findByUsername(userName);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(user.get());
        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new Erreur("Error"));
        }
    }
   
	@GetMapping("/users")
	public ResponseEntity<?> getList(@RequestParam(defaultValue="0") Integer page , @RequestParam(defaultValue="5") Integer limit){
		try {
            JwtUserDetails userDetails = (JwtUserDetails) SecurityContextHolder.getContext()
                    .getAuthentication().getPrincipal();
            String userName = userDetails.getUsername();
            Optional<User> user = userRepository.findByUsername(userName);
            if (user.get().getRole() == UserRole.ROLE_ADMIN) {
            	PageRequest pageable = PageRequest.of(page, limit);
            	
            	return ResponseEntity.status(HttpStatus.OK) 
        				 .body(userRepository.getListByPage(pageable)); 
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED) 
     				 .body(new Erreur("Utilisateur non habilité"));
       
		}catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new Erreur("Error"));
        }

	}
   
   

   @DeleteMapping("/delete")
    public ResponseEntity<?> delete( ) {
        try {
            JwtUserDetails userDetails = (JwtUserDetails) SecurityContextHolder.getContext()
                    .getAuthentication().getPrincipal();
            String userName = userDetails.getUsername();
            Optional<User> user = userRepository.findByUsername(userName);
            
            List<Address> list= this.addressRepository.findAllForUserId(user.get().getId());
            for (int i=0;i<list.size();i++) {
            	this.addressRepository.delete(list.get(i));;
            }
            userRepository.delete(user.get());
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(user.get());
        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new Erreur("Error"));
        }
    }   
   
   
   @DeleteMapping("/delete/{id}")
   public ResponseEntity<?> deleteOther(@PathVariable int id ) {
       try {
           JwtUserDetails userDetails = (JwtUserDetails) SecurityContextHolder.getContext()
                   .getAuthentication().getPrincipal();
           String userName = userDetails.getUsername();
           Optional<User> user = userRepository.findByUsername(userName);
           Optional<User> userdelete = userRepository.findById(id);
           
           if (user.get().getRole()==UserRole.ROLE_ADMIN) {
        	    List<Address> list= this.addressRepository.findAllForUserId(userdelete.get().getId());
                for (int i=0;i<list.size();i++) {
                	this.addressRepository.delete(list.get(i));;
                }
        	   userRepository.delete(userdelete.get());
               return ResponseEntity
                       .status(HttpStatus.OK)
                       .body(user.get());
           }
           else {
        	   return ResponseEntity.status(HttpStatus.UNAUTHORIZED) 
      				 .body(new Erreur("Utilisateur non habilité"));
           }
           
       } catch (Exception ex) {
           ex.printStackTrace();
           return ResponseEntity
                   .status(HttpStatus.BAD_REQUEST)
                   .body(new Erreur("Error"));
       }
   }
	
}
