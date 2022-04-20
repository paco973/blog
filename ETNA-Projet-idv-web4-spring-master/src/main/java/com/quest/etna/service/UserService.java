package com.quest.etna.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.quest.etna.model.User;
import com.quest.etna.repositories.UserRepository;

@Service
public class UserService implements IModelService<User> {
	
	@Autowired
	UserRepository userRepository;
	CheckUserAuthority userAuthority;
	
	@Autowired
    public UserService(UserRepository userRepository, CheckUserAuthority userAuthority) {
        this.userRepository = userRepository;
        this.userAuthority = userAuthority;
    }

	@Override
	public List<User> getList(Integer page, Integer limit) {
		PageRequest pageable = PageRequest.of(page, limit);
		return userRepository.getListByPage(pageable);
	}

	@Override
	public User getOneByName(String name) {
		Optional<User> user = userRepository.findByUsername(name);
		if (user.isEmpty()) return null;
		return user.get();
	}

	@Override
	public User createEntity(User entity) {
		userRepository.save(entity);
		return entity;
	}

	@Override
	public User update(String username, User entity) {
		Optional<User> user = userRepository.findByUsername(username);
		System.out.println(user.get().toString());
		if (user.isEmpty()) return null;
		
		User userFound = user.get();
		userFound.setUsername(entity.getUsername());
		userFound.setPassword(entity.getPassword());
		userFound.setRole(entity.getRole());
		userFound.update_date();;
		userRepository.save(userFound);
		return userFound;
	}

	@Override
	public Boolean delete(String username) {
		try {
			Optional<User> user = userRepository.findByUsername(username);
			if (user.isEmpty()) return null;
			User userFound = user.get();
			userRepository.delete(userFound);
			
		}catch (Exception e) {
			return false;
		}
		return true;
	}

	public ResponseEntity<List<User>> checkAllUsers() {
        List<User> listUser = this.userRepository.findAll();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(listUser);
    }

    public ResponseEntity<User> checkUserById(User mUser) {
            Optional<User> user = this.userRepository.findByUsername(mUser.getUsername());
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(user.get());
    }

    public ResponseEntity<?> updateUser(int userId, User upUser, User oldUser){
        if(userAuthority.checkUserAuthority(oldUser, userId)){
            if(upUser.getUsername()!=null){
                oldUser.setUsername(upUser.getUsername());
            }
            if(upUser.getRole()!=null){
                oldUser.setRole(upUser.getRole());
            }
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(oldUser);
        }else{
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("{ \"error\": \"User Role UNAUTHORIZED\"");
        }
    }
	
	
}
