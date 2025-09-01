package com.example.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.service.LibraryService;

@RestController
@RequestMapping("/api")
public class LibraryController {

	private final LibraryService libraryService;

	public LibraryController(LibraryService libraryService) {
		this.libraryService = libraryService;
	}

	@GetMapping("/books/{isbn}/availability")
	public ResponseEntity<String> isBookAvailable(@PathVariable String isbn) {
		return ResponseEntity.ok(String.format("%s : 대출 %s", isbn, libraryService.isBookAvailable(isbn) ? "가능" : "불가"));
	}

	@PostMapping("/books/{isbn}/borrow")
	public ResponseEntity<String> borrowBook(@PathVariable String isbn) {
		return libraryService.borrowBook(isbn)
			.map(title -> ResponseEntity.ok(String.format("%s : %s", isbn, title)))
			.orElse(ResponseEntity.ok(String.format("%s : 대출 불가", isbn)));
	}
}
