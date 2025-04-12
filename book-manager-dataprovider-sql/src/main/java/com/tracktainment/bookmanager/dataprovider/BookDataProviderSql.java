package com.tracktainment.bookmanager.dataprovider;

import com.tracktainment.bookmanager.domain.Book;
import com.tracktainment.bookmanager.domain.OrderBy;
import com.tracktainment.bookmanager.domain.OrderDirection;
import com.tracktainment.bookmanager.dto.BookCreate;
import com.tracktainment.bookmanager.dto.BookUpdate;
import com.tracktainment.bookmanager.entity.BookEntity;
import com.tracktainment.bookmanager.exception.ResourceAlreadyExistsException;
import com.tracktainment.bookmanager.exception.ResourceNotFoundException;
import com.tracktainment.bookmanager.mapper.BookMapperDataProvider;
import com.tracktainment.bookmanager.repository.BookRepository;
import com.tracktainment.bookmanager.usecases.ListByCriteriaUseCase;
import com.tracktainment.bookmanager.util.Constants;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookDataProviderSql implements BookDataProvider {

    private final BookRepository bookRepository;
    private final BookMapperDataProvider mapper;
    private final EntityManager entityManager;

    @Override
    @Transactional
    public Book create(BookCreate bookCreate) {
        if (existsByTitle(bookCreate.getTitle())) {
            throw new ResourceAlreadyExistsException(BookEntity.class, bookCreate.getTitle());
        }

        return mapper.toBook(bookRepository.save(mapper.toBookEntity(bookCreate)));
    }

    @Override
    public Book findById(String id) {
        return mapper.toBook(findBookEntityById(id));
    }

    @Override
    public List<Book> listByCriteria(ListByCriteriaUseCase.Input input) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<BookEntity> criteriaQuery = criteriaBuilder.createQuery(BookEntity.class);
        Root<BookEntity> root = criteriaQuery.from(BookEntity.class);

        Predicate[] predicates = buildPredicates(criteriaBuilder, root, input);
        criteriaQuery.where(predicates);

        applyListSorting(criteriaBuilder, criteriaQuery, root, input);
        log.info("offset: {}", input.getOffset());
        return entityManager.createQuery(criteriaQuery)
                .setFirstResult(input.getOffset() != null ? input.getOffset() : Constants.MIN_OFFSET)
                .setMaxResults(input.getLimit() != null ? input.getLimit() : Integer.parseInt(Constants.DEFAULT_LIMIT))
                .getResultList()
                .stream()
                .map(mapper::toBook)
                .toList();
    }

    @Override
    @Transactional
    public Book update(String id, BookUpdate bookUpdate) {
        BookEntity bookEntity = findBookEntityById(id);
        mapper.updateBookEntity(bookEntity, bookUpdate);
        return mapper.toBook(bookRepository.save(bookEntity));
    }

    @Override
    @Transactional
    public void delete(String id) {
        if (existsById(id)) {
            bookRepository.deleteById(id);
        } else {
            throw new ResourceNotFoundException(BookEntity.class, id);
        }
    }

    private boolean existsByTitle(String title) {
        return bookRepository.findByTitle(title).isPresent();
    }

    private boolean existsById(String id) {
        return bookRepository.findById(id).isPresent();
    }

    private BookEntity findBookEntityById(String id) {
        return bookRepository.findById(id)
                .orElseThrow(
                        () -> new ResourceNotFoundException(BookEntity.class, id)
                );
    }

    private Predicate[] buildPredicates(
            CriteriaBuilder criteriaBuilder,
            Root<BookEntity> root,
            ListByCriteriaUseCase.Input input
    ) {
        List<Predicate> predicates = new ArrayList<>();

        if (input.getIds() != null) {
            List<String> ids = List.of(input.getIds().split(","));
            predicates.add(criteriaBuilder.in(root.get("id")).value(ids));
        }

        if (input.getTitle() != null) {
            predicates.add(criteriaBuilder.like(root.get("title"), "%" + input.getTitle() + "%"));
        }

        if (input.getAuthor() != null) {
            predicates.add(criteriaBuilder.like(root.get("author"), "%" + input.getAuthor() + "%"));
        }

        if (input.getIsbn() != null) {
            predicates.add(criteriaBuilder.like(root.get("isbn"), "%" + input.getIsbn() + "%"));
        }

        if (input.getPublisher() != null) {
            predicates.add(criteriaBuilder.like(root.get("publisher"), "%" + input.getPublisher() + "%"));
        }

        if (input.getPublishedDate() != null) {
            predicates.add(criteriaBuilder.equal(root.get("publishedDate"), input.getPublishedDate()));
        }

        if (input.getLanguage() != null) {
            predicates.add(criteriaBuilder.like(root.get("language"), "%" + input.getLanguage() + "%"));
        }

        if (input.getFrom() != null) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("createdAt"), input.getFrom()));
        }

        if (input.getTo() != null) {
            predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("createdAt"), input.getTo()));
        }

        if (input.getCreatedAt() != null) {
            predicates.add(criteriaBuilder.equal(root.get("createdAt"), input.getCreatedAt()));
        }

        return predicates.toArray(new Predicate[0]);
    }

    private void applyListSorting(
            CriteriaBuilder criteriaBuilder,
            CriteriaQuery<BookEntity> criteriaQuery,
            Root<BookEntity> root,
            ListByCriteriaUseCase.Input input
    ) {
        if (input.getOrderByList() != null && input.getOrderDirectionList() != null) {
            List<Order> orderList = new ArrayList<>();
            for (int i=0; i<input.getOrderByList().size(); i++) {
                OrderBy orderBy = input.getOrderByList().get(i);
                OrderDirection orderDirection = input.getOrderDirectionList().get(i);

                if (orderBy != null && orderDirection != null) {
                    if (orderBy == OrderBy.TITLE) {
                        if (orderDirection == OrderDirection.ASC) {
                            orderList.add(criteriaBuilder.asc(root.get("title")));
                        } else {
                            orderList.add(criteriaBuilder.desc(root.get("title")));
                        }
                    }

                    if (orderBy == OrderBy.AUTHOR) {
                        if (orderDirection == OrderDirection.ASC) {
                            orderList.add(criteriaBuilder.asc(root.get("author")));
                        } else {
                            orderList.add(criteriaBuilder.desc(root.get("author")));
                        }
                    }

                    if (orderBy == OrderBy.CREATED_AT) {
                        if (orderDirection == OrderDirection.ASC) {
                            orderList.add(criteriaBuilder.asc(root.get("createdAt")));
                        } else {
                            orderList.add(criteriaBuilder.desc(root.get("createdAt")));
                        }
                    }
                }
            }

            if (!orderList.isEmpty()) {
                criteriaQuery.orderBy(orderList);
            }
        }
    }
}
