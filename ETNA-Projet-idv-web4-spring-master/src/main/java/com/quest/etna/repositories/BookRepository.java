package com.quest.etna.repositories;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.quest.etna.model.Book;

@CrossOrigin("*")
@Repository
public interface BookRepository extends CrudRepository<Book, Integer>{ 
	
	@Query ("SELECT u FROM Book u WHERE u.title=title" )
	public List<Book> findByTitle( String title);
		
	@Query ("SELECT u FROM Book u ORDER BY u.id ASC" )
	public List<Book> getListByPage(Pageable pageable);
	
	boolean existsUserByTitle(String title);
    List<Book> findAll();
    
    @Query ("SELECT u FROM Book u WHERE u.author=author")
	public List<Book> findByAuthor(String author);

}
