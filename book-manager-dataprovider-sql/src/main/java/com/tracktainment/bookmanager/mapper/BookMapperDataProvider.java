package com.tracktainment.bookmanager.mapper;

import com.tracktainment.bookmanager.domain.Book;
import com.tracktainment.bookmanager.dto.BookCreate;
import com.tracktainment.bookmanager.dto.BookUpdate;
import com.tracktainment.bookmanager.entity.BookEntity;
import org.mapstruct.*;

import java.util.UUID;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        imports = {UUID.class}
)
public interface BookMapperDataProvider {

    Book toBook(BookEntity bookEntity);

    BookEntity toBookEntity(BookCreate bookCreate);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateBookEntity(
            @MappingTarget BookEntity bookEntity,
            BookUpdate bookUpdate
            );
}
