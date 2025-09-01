package com.example.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.entity.Book;
import com.example.repository.BookRepository;

@Service
public class LibraryService {

	private final BookRepository bookRepository;
	private final PushService pushService;

	public LibraryService(BookRepository bookRepository, PushService pushService) {
		this.bookRepository = bookRepository;
		this.pushService = pushService;
	}

	public boolean isBookAvailable(String isbn) {
		return bookRepository.findBookByIsbn(isbn).map(Book::isAvailable).orElse(false);
	}

	public Optional<String> borrowBook(String isbn) {
		return bookRepository
			.findBookByIsbn(isbn)
			.filter(Book::isAvailable)
			.map(
				book -> {
					pushService.notification("대출 완료: " + book.getTitle());
					return book.getTitle();
				});
	}
}
