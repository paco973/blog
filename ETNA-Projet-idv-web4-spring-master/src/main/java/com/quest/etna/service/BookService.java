package com.quest.etna.service;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.quest.etna.model.Book;
import com.quest.etna.repositories.BookRepository;

@Service
public class BookService {
	@Autowired
	BookRepository bookRepository;
	
	@Autowired
	public BookService(BookRepository bookRepository) {
		this.bookRepository = bookRepository;
	}


	public List<Book> getList(Integer page, Integer limit) {
		PageRequest pageable = PageRequest.of(page, limit);
		return bookRepository.getListByPage(pageable);
	}


	public List<Book> getByName(String title) {
		List<Book> books = bookRepository.findByTitle(title);
		return books;
	}
	
	
	  public List<Book> getByAuthor(String author) { List<Book> books =
	  bookRepository.findByAuthor(author); return books; }


	public ResponseEntity<?> createBook(Book book) {
		bookRepository.save(book);
	    return ResponseEntity
                .status(HttpStatus.OK)
                .body(book);
	}


	public ResponseEntity<?> update(int id, Book entity) {
		Optional<Book> book = bookRepository.findById(id);
		Book bookFound = book.get();
		bookFound.setTitle(entity.getTitle());;
		bookFound.setAuthor(entity.getAuthor());;
		bookFound.setImage(entity.getImage());
		bookFound.setDate_publication(entity.getDate_publication());
		bookFound.setDescription(entity.getDescription());
		bookFound.setPages(entity.getPages());
		bookFound.setQuantity(entity.getQuantity());
		bookRepository.save(bookFound);
		return ResponseEntity
                .status(HttpStatus.OK)
                .body(bookFound);
	}
	
	
	public ResponseEntity<?> updateQuantity(int id, int quantity) {
		Optional<Book> book = bookRepository.findById(id);
		Book bookFound = book.get();
		bookFound.setQuantity(bookFound.getQuantity()-quantity);
		bookRepository.save(bookFound);
		return ResponseEntity
                .status(HttpStatus.OK)
                .body(bookFound);
	}


	public ResponseEntity<?> delete(int id) {
		Optional<Book> book = bookRepository.findById(id);
		Book bookFound = book.get();
		bookRepository.delete(bookFound);
		return ResponseEntity
                .status(HttpStatus.OK)
                .body(book);
	}

	public ResponseEntity<List<Book>> checkAllBooks() {
        List<Book> listBook = this.bookRepository.findAll();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(listBook);
    }

    public ResponseEntity<Book> checkBookById(int id) {
            Optional<Book> book = this.bookRepository.findById(id);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(book.get());
    }

	
	
}
