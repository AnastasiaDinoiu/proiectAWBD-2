package com.library.book.repository;

import com.library.book.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    @Query("SELECT b FROM Book b WHERE LOWER(b.title) LIKE LOWER(CONCAT('%', :title, '%'))")
    Page<Book> findByTitleContainingIgnoreCase(@Param("title") String title, Pageable pageable);

    Page<Book> findByCategoryId(Long categoryId, Pageable pageable);

    @Query("SELECT b FROM Book b JOIN b.authors a WHERE a.id = :authorId")
    Page<Book> findByAuthorId(@Param("authorId") Long authorId, Pageable pageable);

    Page<Book> findByPublisherId(Long publisherId, Pageable pageable);

    List<Book> findByAvailableCopiesGreaterThan(Integer copies);

    Optional<Book> findByIsbn(String isbn);
}