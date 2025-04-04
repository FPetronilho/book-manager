package com.tracktainment.bookmanager.mapper;

import com.tracktainment.bookmanager.domain.Book;
import com.tracktainment.bookmanager.dto.BookCreate;
import com.tracktainment.bookmanager.dto.BookUpdate;
import com.tracktainment.bookmanager.entity.BookEntity;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-04-05T01:17:10+0200",
    comments = "version: 1.6.2, compiler: javac, environment: Java 23 (Oracle Corporation)"
)
@Component
public class BookMapperDataProviderImpl implements BookMapperDataProvider {

    @Override
    public Book toBook(BookEntity bookEntity) {
        if ( bookEntity == null ) {
            return null;
        }

        Book.BookBuilder book = Book.builder();

        book.id( bookEntity.getId() );
        book.title( bookEntity.getTitle() );
        book.author( bookEntity.getAuthor() );
        book.isbn( bookEntity.getIsbn() );
        book.publisher( bookEntity.getPublisher() );
        book.publishedDate( bookEntity.getPublishedDate() );
        book.language( bookEntity.getLanguage() );
        book.createdAt( bookEntity.getCreatedAt() );
        book.updatedAt( bookEntity.getUpdatedAt() );

        return book.build();
    }

    @Override
    public BookEntity toBookEntity(BookCreate bookCreate) {
        if ( bookCreate == null ) {
            return null;
        }

        BookEntity.BookEntityBuilder<?, ?> bookEntity = BookEntity.builder();

        bookEntity.title( bookCreate.getTitle() );
        bookEntity.author( bookCreate.getAuthor() );
        bookEntity.isbn( bookCreate.getIsbn() );
        bookEntity.publisher( bookCreate.getPublisher() );
        bookEntity.publishedDate( bookCreate.getPublishedDate() );
        bookEntity.language( bookCreate.getLanguage() );

        bookEntity.id( java.util.UUID.randomUUID().toString() );

        return bookEntity.build();
    }

    @Override
    public void updateBookEntity(BookEntity bookEntity, BookUpdate bookUpdate) {
        if ( bookUpdate == null ) {
            return;
        }

        if ( bookUpdate.getTitle() != null ) {
            bookEntity.setTitle( bookUpdate.getTitle() );
        }
        if ( bookUpdate.getAuthor() != null ) {
            bookEntity.setAuthor( bookUpdate.getAuthor() );
        }
        if ( bookUpdate.getIsbn() != null ) {
            bookEntity.setIsbn( bookUpdate.getIsbn() );
        }
        if ( bookUpdate.getPublisher() != null ) {
            bookEntity.setPublisher( bookUpdate.getPublisher() );
        }
        if ( bookUpdate.getPublishedDate() != null ) {
            bookEntity.setPublishedDate( bookUpdate.getPublishedDate() );
        }
        if ( bookUpdate.getLanguage() != null ) {
            bookEntity.setLanguage( bookUpdate.getLanguage() );
        }
    }
}
