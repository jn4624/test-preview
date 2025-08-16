package com.example;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class LibraryServiceTest {

	private final PushService pushService = mock(PushService.class);

	@Test
	@DisplayName("도서 이용 가능 여부를 확인한다.")
	void isBookAvailable() {
		BookRepository bookRepository = mock(BookRepository.class);
		when(bookRepository.findBookByIsbn(anyString())).thenReturn(Optional.of(new Book("1234", "Stub", true)));

		LibraryService libraryService = new LibraryService(bookRepository, pushService);

		boolean bookAvailable = libraryService.isBookAvailable("1234");

		assertTrue(bookAvailable);
	}

	public static Stream<Arguments> borrowBookDataProvider() {
		// boolean bookExists, String isbn, String title, boolean available, Optional<String> expected
		return Stream.of(Arguments.of(true, "1234", "JUnit", true, Optional.of("JUnit")),
			Arguments.of(true, "5678", "Mockito", false, Optional.empty()),
			Arguments.of(false, "9999", "Not used", true, Optional.empty()));
	}

	@ParameterizedTest(name = "bookExists={0}, isbn={1}, title={2}, available={3} => expected: {4}")
	@MethodSource("borrowBookDataProvider")
	@DisplayName("대출 요청시 도서 상태에 따른 처리 결과를 확인한다.")
	void borrowBook(boolean bookExists, String isbn, String title, boolean available, Optional<String> expected) {
		BookRepository bookRepository = mock(BookRepository.class);

		when(bookRepository.findBookByIsbn(anyString())).thenReturn(
			bookExists ? Optional.of(new Book(isbn, title, available)) : Optional.empty());

		LibraryService libraryService = new LibraryService(bookRepository, pushService);

		Optional<String> borrowedBook = libraryService.borrowBook(isbn);

		assertEquals(expected, borrowedBook);
	}

	@Test
	@DisplayName("대출에 성공하면 알림이 발송되어야 한다.")
	void borrowBookPush() {
		BookRepository bookRepository = mock(BookRepository.class);
		when(bookRepository.findBookByIsbn(anyString())).thenReturn(Optional.of(new Book("1234", "Stub", true)));

		LibraryService libraryService = new LibraryService(bookRepository, pushService);

		Optional<String> borrowedBook = libraryService.borrowBook("1234");

		assertEquals(Optional.of("Stub"), borrowedBook);
		verify(pushService, times(1)).notification(anyString());
	}

	@Test
	@DisplayName("도서 조회 중에 예외가 발생하면 대출 요청시 예외를 던진다.")
	void borrowBookException() {
		BookRepository bookRepository = mock(BookRepository.class);
		when(bookRepository.findBookByIsbn(anyString())).thenThrow(new RuntimeException("Database error"));

		LibraryService libraryService = new LibraryService(bookRepository, pushService);

		assertThrows(RuntimeException.class, () -> libraryService.borrowBook("1234"));

		// Spock와 다르게 블럭으로 구분되어 있지 않아 가독성이 좋지 않아 분리하여 관리
		Executable executable = () -> libraryService.borrowBook("1234");
		assertThrows(RuntimeException.class, executable);
	}

	@Test
	@DisplayName("Spy 테스트")
	void borrowBookUsingSpy() {
		BookRepository bookRepository = mock(BookRepository.class);
		when(bookRepository.findBookByIsbn(anyString())).thenReturn(Optional.of(new Book("1234", "Spy", true)));

		// Spy는 Mock이랑 다르게 원하는 부분만 타겟팅해서 오버라이딩 할 수 있고 나머지 기능은 원본을 그대로 사용
		LibraryService libraryService = spy(new LibraryService(bookRepository, pushService));

		doReturn(Optional.of("Overridden Spy")).when(libraryService).borrowBook("1234");

		boolean bookAvailable = libraryService.isBookAvailable("1234");
		Optional<String> borrowedBook = libraryService.borrowBook("1234");

		assertTrue(bookAvailable);
		assertEquals(Optional.of("Overridden Spy"), borrowedBook);
	}
}
