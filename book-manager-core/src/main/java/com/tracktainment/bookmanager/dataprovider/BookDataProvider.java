package com.tracktainment.bookmanager.dataprovider;

import com.tracktainment.bookmanager.domain.Book;
import com.tracktainment.bookmanager.dto.BookCreate;
import com.tracktainment.bookmanager.dto.BookUpdate;

import java.util.List;

public interface BookDataProvider {

    Book create(BookCreate bookCreate);

    Book findById(String id);

    Book findByTitle(String title);

//    List<Book> listByCriteria(UseCase);

    Book update(String id, BookUpdate bookUpdate);

    Void delete(String id);
}
