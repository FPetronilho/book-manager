package dataprovider;

import com.tracktainment.bookmanager.dataprovider.BookDataProviderSql;
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
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import testutil.TestBookDataUtil;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookDataProviderSqlTest {

    @Mock
    private BookMapperDataProvider mapper;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private EntityManager entityManager;

    @Mock
    private CriteriaBuilder criteriaBuilder;

    @Mock
    private CriteriaQuery<BookEntity> criteriaQuery;

    @Mock
    private Root<BookEntity> root;

    @Mock
    private TypedQuery<BookEntity> typedQuery;

    @InjectMocks
    private BookDataProviderSql bookDataProviderSql;

    private BookCreate bookCreate;
    private BookUpdate bookUpdate;
    private Book book;
    private BookEntity bookEntity;
    private String bookId;

    @BeforeEach
    void setUp() {
        bookId = UUID.randomUUID().toString();
        bookCreate = TestBookDataUtil.createTestBookCreate();
        bookUpdate = TestBookDataUtil.createTestBookUpdate();
        book = TestBookDataUtil.createTestBook();

        bookEntity = BookEntity.builder()
                .id(bookId)
                .title(bookCreate.getTitle())
                .author(bookCreate.getAuthor())
                .isbn(bookCreate.getIsbn())
                .publisher(bookCreate.getPublisher())
                .publishedDate(bookCreate.getPublishedDate())
                .language(bookCreate.getLanguage())
                .build();
    }

    @Test
    void shouldCreateBookSuccessfully() {
        // Arrange
        when(bookRepository.findByTitle(bookCreate.getTitle())).thenReturn(Optional.empty());
        when(mapper.toBookEntity(bookCreate)).thenReturn(bookEntity);
        when(bookRepository.save(bookEntity)).thenReturn(bookEntity);
        when(mapper.toBook(bookEntity)).thenReturn(book);

        // Act
        Book result = bookDataProviderSql.create(bookCreate);

        // Assert
        assertNotNull(result);
        assertEquals(book, result);

        verify(bookRepository).findByTitle(bookCreate.getTitle());
        verify(mapper).toBookEntity(bookCreate);
        verify(bookRepository).save(bookEntity);
        verify(mapper).toBook(bookEntity);
    }

    @Test
    void shouldThrowResourceAlreadyExistsException() {
        // Arrange
        when(bookRepository.findByTitle(bookCreate.getTitle())).thenReturn(Optional.of(bookEntity));

        // Act & Assert
        assertThrows(ResourceAlreadyExistsException.class, () -> bookDataProviderSql.create(bookCreate));

        verify(bookRepository).findByTitle(bookCreate.getTitle());
        verify(mapper, never()).toBookEntity(any());
        verify(bookRepository, never()).save(any());
    }

    @Test
    void shouldFindBookByIdSuccessfully() {
        // Arrange
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(bookEntity));
        when(mapper.toBook(bookEntity)).thenReturn(book);

        // Act
        Book result = bookDataProviderSql.findById(bookId);

        // Assert
        assertNotNull(result);
        assertEquals(book, result);

        verify(bookRepository).findById(bookId);
        verify(mapper).toBook(bookEntity);
    }

    @Test
    void shouldThrowResourceNotFoundExceptionWhenBookNotFound() {
        // Arrange
        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> bookDataProviderSql.findById(bookId));

        verify(bookRepository).findById(bookId);
        verify(mapper, never()).toBook(any());
    }

    @Test
    void shouldUpdateBookSuccessfully() {
        // Arrange
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(bookEntity));
        when(bookRepository.save(bookEntity)).thenReturn(bookEntity);
        when(mapper.toBook(bookEntity)).thenReturn(book);

        // Act
        Book result = bookDataProviderSql.update(bookId, bookUpdate);

        // Assert
        assertNotNull(result);
        assertEquals(book, result);

        verify(bookRepository).findById(bookId);
        verify(mapper).updateBookEntity(bookEntity, bookUpdate);
        verify(bookRepository).save(bookEntity);
        verify(mapper).toBook(bookEntity);
    }

    @Test
    void shouldThrowResourceNotFoundExceptionWhenUpdatingNonExistingBook() {
        // Arrange
        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> bookDataProviderSql.update(bookId, bookUpdate));

        verify(bookRepository).findById(bookId);
        verify(mapper, never()).updateBookEntity(any(), any());
        verify(bookRepository, never()).save(any());
    }

    @Test
    void shouldDeleteBookSuccessfully() {
        // Arrange
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(bookEntity));
        doNothing().when(bookRepository).deleteById(bookId);

        // Act
        assertDoesNotThrow(() -> bookDataProviderSql.delete(bookId));

        // Assert
        verify(bookRepository).findById(bookId);
        verify(bookRepository).deleteById(bookId);
    }

    @Test
    void shouldThrowResourceNotFoundExceptionWhenDeletingNonExistingBook() {
        // Arrange
        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> bookDataProviderSql.delete(bookId));

        verify(bookRepository).findById(bookId);
        verify(bookRepository, never()).deleteById((String) any());
    }

    @Test
    void shouldListBooksByCriteriaSuccessfully() {
        // Arrange
        List<BookEntity> bookEntities = Collections.singletonList(bookEntity);

        ListByCriteriaUseCase.Input input = ListByCriteriaUseCase.Input.builder()
                .offset(0)
                .limit(10)
                .orderByList(Collections.singletonList(OrderBy.TITLE))
                .orderDirectionList(Collections.singletonList(OrderDirection.ASC))
                .build();

        // Setup only the mocks that are actually used in this test
        when(entityManager.getCriteriaBuilder()).thenReturn(criteriaBuilder);
        when(criteriaBuilder.createQuery(BookEntity.class)).thenReturn(criteriaQuery);
        when(criteriaQuery.from(BookEntity.class)).thenReturn(root);
        when(criteriaQuery.where(any(Predicate[].class))).thenReturn(criteriaQuery);
        when(entityManager.createQuery(criteriaQuery)).thenReturn(typedQuery);
        when(typedQuery.setFirstResult(anyInt())).thenReturn(typedQuery);
        when(typedQuery.setMaxResults(anyInt())).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(bookEntities);

        // For ordering logic
        Path<Object> pathMock = mock(Path.class);
        when(root.get("title")).thenReturn(pathMock);
        when(criteriaBuilder.asc(pathMock)).thenReturn(mock(Order.class));
        when(criteriaQuery.orderBy(anyList())).thenReturn(criteriaQuery);

        when(mapper.toBook(bookEntity)).thenReturn(book);

        // Act
        List<Book> results = bookDataProviderSql.listByCriteria(input);

        // Assert
        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals(book, results.get(0));

        // Verify all the mocks were used
        verify(entityManager).getCriteriaBuilder();
        verify(criteriaBuilder).createQuery(BookEntity.class);
        verify(criteriaQuery).from(BookEntity.class);
        verify(criteriaQuery).where(any(Predicate[].class));
        verify(entityManager).createQuery(criteriaQuery);
        verify(typedQuery).setFirstResult(anyInt());
        verify(typedQuery).setMaxResults(anyInt());
        verify(typedQuery).getResultList();
        verify(mapper).toBook(bookEntity);
    }

    @Test
    void shouldReturnEmptyListWhenNoBooksMatch() {
        // Arrange
        List<BookEntity> bookEntities = Collections.emptyList();

        ListByCriteriaUseCase.Input input = ListByCriteriaUseCase.Input.builder()
                .offset(0)
                .limit(10)
                .build();

        // Setup only the mocks that are actually used in this test
        when(entityManager.getCriteriaBuilder()).thenReturn(criteriaBuilder);
        when(criteriaBuilder.createQuery(BookEntity.class)).thenReturn(criteriaQuery);
        when(criteriaQuery.from(BookEntity.class)).thenReturn(root);
        when(criteriaQuery.where(any(Predicate[].class))).thenReturn(criteriaQuery);
        when(entityManager.createQuery(criteriaQuery)).thenReturn(typedQuery);
        when(typedQuery.setFirstResult(anyInt())).thenReturn(typedQuery);
        when(typedQuery.setMaxResults(anyInt())).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(bookEntities);

        // Act
        List<Book> results = bookDataProviderSql.listByCriteria(input);

        // Assert
        assertNotNull(results);
        assertTrue(results.isEmpty());

        // Verify all the mocks were used
        verify(entityManager).getCriteriaBuilder();
        verify(criteriaBuilder).createQuery(BookEntity.class);
        verify(criteriaQuery).from(BookEntity.class);
        verify(criteriaQuery).where(any(Predicate[].class));
        verify(entityManager).createQuery(criteriaQuery);
        verify(typedQuery).setFirstResult(anyInt());
        verify(typedQuery).setMaxResults(anyInt());
        verify(typedQuery).getResultList();
        verify(mapper, never()).toBook(any());
    }
}
