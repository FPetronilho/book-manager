package com.tracktainment.bookmanager.dataprovider;

import com.tracktainment.bookmanager.domain.Book;
import com.tracktainment.bookmanager.dto.BookCreate;
import com.tracktainment.bookmanager.dto.BookUpdate;
import com.tracktainment.bookmanager.mapper.BookMapperDataProvider;
import com.tracktainment.bookmanager.repository.BookRepository;
import com.tracktainment.bookmanager.usecases.ListByCriteriaUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookDataProviderSql implements BookDataProvider {

    private final BookRepository bookRepository;
    private final BookMapperDataProvider mapper;

    @Override
    public Book create(BookCreate bookCreate) {
        return null;
    }

    @Override
    public Book findById(String id) {
        return null;
    }

    @Override
    public List<Book> listByCriteria(ListByCriteriaUseCase.Input input) {
        return null;
    }

    @Override
    public Book update(String id, BookUpdate bookUpdate) {
        return null;
    }

    @Override
    public void delete(String id) {

    }
}
