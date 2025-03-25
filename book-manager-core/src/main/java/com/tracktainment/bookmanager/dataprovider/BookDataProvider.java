package com.tracktainment.bookmanager.dataprovider;

import com.tracktainment.bookmanager.domain.Book;
import com.tracktainment.bookmanager.dto.BookCreate;
import com.tracktainment.bookmanager.dto.BookUpdate;
import com.tracktainment.bookmanager.usecases.ListByCriteriaUseCase;

import java.util.List;

public interface BookDataProvider {

    Book create(BookCreate bookCreate);

    Book findById(String id);

    Book findByTitle(String title);

    List<Book> listByCriteria(ListByCriteriaUseCase.Input input);

    Book update(String id, BookUpdate bookUpdate);

    void delete(String id);
}
