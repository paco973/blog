package com.quest.etna.service;

import java.util.List;

import com.quest.etna.model.User;

public interface IModelService<T> {
	
	
	public List<User> getList(Integer page , Integer limit);
	public T getOneByName(String name);
	public T createEntity(T entity);
	public T update(String username, T entity);
	public Boolean delete(String username);
	
}
