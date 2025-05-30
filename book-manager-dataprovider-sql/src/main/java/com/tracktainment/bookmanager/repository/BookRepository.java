package com.tracktainment.bookmanager.repository;

import com.tracktainment.bookmanager.entity.BookEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<BookEntity, Long> {

    Optional<BookEntity> findByTitle(String title);

    Optional<BookEntity> findById(String id);

    void deleteById(String id);
}
