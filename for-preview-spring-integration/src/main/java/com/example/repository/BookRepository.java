package com.example.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import com.example.entity.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

	Optional<Book> findBookByIsbn(@NonNull String isbn);
}
