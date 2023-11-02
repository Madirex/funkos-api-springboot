package com.madirex.funkosspringrest.funko;

import com.madirex.funkosspringrest.dto.funko.CreateFunkoDTO;
import com.madirex.funkosspringrest.dto.funko.GetFunkoDTO;
import com.madirex.funkosspringrest.dto.funko.PatchFunkoDTO;
import com.madirex.funkosspringrest.dto.funko.UpdateFunkoDTO;
import com.madirex.funkosspringrest.exceptions.category.CategoryNotFoundException;
import com.madirex.funkosspringrest.exceptions.category.CategoryNotValidIDException;
import com.madirex.funkosspringrest.exceptions.funko.FunkoNotFoundException;
import com.madirex.funkosspringrest.exceptions.funko.FunkoNotValidUUIDException;
import com.madirex.funkosspringrest.mappers.funko.FunkoMapperImpl;
import com.madirex.funkosspringrest.models.Category;
import com.madirex.funkosspringrest.models.Funko;
import com.madirex.funkosspringrest.repositories.FunkoRepository;
import com.madirex.funkosspringrest.services.category.CategoryService;
import com.madirex.funkosspringrest.services.funko.FunkoServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FunkoServiceImplTest {
    List<Funko> list;
    @Mock
    private FunkoRepository funkoRepository;

    @Mock
    private FunkoMapperImpl funkoMapperImpl;

    @Mock
    private CategoryService categoryService;
    @InjectMocks
    private FunkoServiceImpl funkoService;

    @BeforeEach
    void setUp() {
        list = new ArrayList<>();
        list.add(Funko.builder()
                .id(UUID.randomUUID())
                .name("Test")
                .price(2.2)
                .quantity(2)
                .image("http://tech.madirex.com/favicon.ico")
                .category(Category.builder().id(1L).type(Category.Type.MOVIE).active(true).build())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build());
        list.add(Funko.builder()
                .id(UUID.randomUUID())
                .name("Test2")
                .price(42.42)
                .quantity(42)
                .image("http://www.madirex.com/favicon.ico")
                .category(Category.builder().id(1L).type(Category.Type.MOVIE).active(true).build())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build());
    }

    @Test
    void testGetAllFunko() {
        List<GetFunkoDTO> list2 = new ArrayList<>();
        list2.add(GetFunkoDTO.builder()
                .id(UUID.randomUUID())
                .name("Test")
                .price(2.2)
                .quantity(2)
                .image("http://tech.madirex.com/favicon.ico")
                .category(Category.builder().id(1L).type(Category.Type.MOVIE).active(true).build())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build());
        list2.add(GetFunkoDTO.builder()
                .id(UUID.randomUUID())
                .name("Test2")
                .price(42.42)
                .quantity(42)
                .image("http://www.madirex.com/favicon.ico")
                .category(Category.builder().id(1L).type(Category.Type.MOVIE).active(true).build())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build());
        when(funkoRepository.findAll()).thenReturn(list);
        when(funkoMapperImpl.toFunkoList(list)).thenReturn(list2);
        var list3 = funkoService.getAllFunko();
        assertAll("Funko properties",
                () -> assertEquals(2, list.size(), "La lista debe contener 2 elementos"),
                () -> assertEquals(2, list3.size(), "La lista debe contener 2 elementos"),
                () -> assertEquals(list.get(0).getName(), list3.get(0).getName(), "El nombre debe coincidir"),
                () -> assertEquals(list.get(0).getPrice(), list3.get(0).getPrice(), "El precio debe coincidir"),
                () -> assertEquals(list.get(0).getQuantity(), list3.get(0).getQuantity(), "La cantidad debe coincidir"),
                () -> assertEquals(list.get(0).getImage(), list3.get(0).getImage(), "La imagen debe coincidir"),
                () -> assertEquals(list.get(1).getName(), list3.get(1).getName(), "El nombre debe coincidir"),
                () -> assertEquals(list.get(1).getPrice(), list3.get(1).getPrice(), "El precio debe coincidir"),
                () -> assertEquals(list.get(1).getQuantity(), list3.get(1).getQuantity(), "La cantidad debe coincidir"),
                () -> assertEquals(list.get(1).getImage(), list3.get(1).getImage(), "La imagen debe coincidir")
        );
        verify(funkoRepository, times(1)).findAll();
    }

    @Test
    void testGetAllFunkoFilterByCategory() {
        List<GetFunkoDTO> list2 = new ArrayList<>();
        list2.add(GetFunkoDTO.builder()
                .id(UUID.randomUUID())
                .name("Test")
                .price(2.2)
                .quantity(2)
                .image("http://tech.madirex.com/favicon.ico")
                .category(Category.builder().id(1L).type(Category.Type.DISNEY).active(true).build())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build());
        list2.add(GetFunkoDTO.builder()
                .id(UUID.randomUUID())
                .name("Test2")
                .price(42.42)
                .quantity(42)
                .image("http://www.madirex.com/favicon.ico")
                .category(Category.builder().id(1L).type(Category.Type.MOVIE).active(true).build())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build());
        when(funkoRepository.findAll()).thenReturn(list);
        when(funkoMapperImpl.toFunkoList(list)).thenReturn(list2);
        var tmp = funkoService.getAllFunko();
        var list3 = funkoService.getAllFunkoFilterByCategory(tmp, "MOVIE");
        assertAll("Funko properties",
                () -> assertEquals(2, list.size(), "La lista debe contener 2 elementos"),
                () -> assertEquals(1, list3.size(), "La lista debe contener 1 elemento"),
                () -> assertEquals(list.get(1).getName(), list3.get(0).getName(), "El nombre debe coincidir"),
                () -> assertEquals(list.get(1).getPrice(), list3.get(0).getPrice(), "El precio debe coincidir"),
                () -> assertEquals(list.get(1).getQuantity(), list3.get(0).getQuantity(), "La cantidad debe coincidir"),
                () -> assertEquals(list.get(1).getImage(), list3.get(0).getImage(), "La imagen debe coincidir")
        );
        verify(funkoRepository, times(1)).findAll();
    }

    @Test
    void testGetAllFunkoFilterByCategoryNull() {
        List<GetFunkoDTO> list2 = new ArrayList<>();
        list2.add(GetFunkoDTO.builder()
                .id(UUID.randomUUID())
                .name("Test")
                .price(2.2)
                .quantity(2)
                .image("http://tech.madirex.com/favicon.ico")
                .category(Category.builder().id(1L).type(Category.Type.MOVIE).active(true).build())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build());
        list2.add(GetFunkoDTO.builder()
                .id(UUID.randomUUID())
                .name("Test2")
                .price(42.42)
                .quantity(42)
                .image("http://www.madirex.com/favicon.ico")
                .category(Category.builder().id(1L).type(Category.Type.MOVIE).active(true).build())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build());
        when(funkoRepository.findAll()).thenReturn(list);
        when(funkoMapperImpl.toFunkoList(list)).thenReturn(list2);
        var tmp = funkoService.getAllFunko();
        var list3 = funkoService.getAllFunkoFilterByCategory(tmp, null);
        assertAll("Funko properties",
                () -> assertEquals(2, list.size(), "La lista debe contener 2 elementos"),
                () -> assertEquals(0, list3.size(), "La lista debe contener 2 elementos")
        );
        verify(funkoRepository, times(1)).findAll();
    }


    @Test
    void testGetFunkoById() throws FunkoNotValidUUIDException, FunkoNotFoundException {
        List<GetFunkoDTO> list2 = new ArrayList<>();
        list2.add(GetFunkoDTO.builder()
                .id(UUID.randomUUID())
                .name("Test")
                .price(2.2)
                .quantity(2)
                .image("http://tech.madirex.com/favicon.ico")
                .category(Category.builder().id(1L).type(Category.Type.MOVIE).active(true).build())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build());
        when(funkoRepository.findById(list.get(0).getId())).thenReturn(java.util.Optional.ofNullable(list.get(0)));
        when(funkoMapperImpl.toGetFunkoDTO(list.get(0))).thenReturn(list2.get(0));
        var funko = funkoService.getFunkoById(String.valueOf(list.get(0).getId()));
        assertAll("Funko properties",
                () -> assertEquals(list.get(0).getName(), funko.getName(), "El nombre debe coincidir"),
                () -> assertEquals(list.get(0).getPrice(), funko.getPrice(), "El precio debe coincidir"),
                () -> assertEquals(list.get(0).getQuantity(), funko.getQuantity(), "La cantidad debe coincidir"),
                () -> assertEquals(list.get(0).getImage(), funko.getImage(), "La imagen debe coincidir")
        );
        verify(funkoRepository, times(1)).findById(list.get(0).getId());
    }

    @Test
    void testGetFunkoByIdNotFound() {
        when(funkoRepository.findById(any(UUID.class))).thenReturn(Optional.empty());
        assertThrows(FunkoNotFoundException.class, () -> funkoService.getFunkoById(UUID.randomUUID().toString()));
        verify(funkoRepository, times(1)).findById(any(UUID.class));
    }

    @Test
    void testGetFunkoByIdNotValidUUID() {
        String invalidUUID = "UUID NO VÁLIDA";
        assertThrows(FunkoNotValidUUIDException.class, () -> funkoService.getFunkoById(invalidUUID));
    }

    @Test
    void testPostFunko() throws CategoryNotFoundException, CategoryNotValidIDException {
        var insert = CreateFunkoDTO.builder()
                .name("nombre").price(2.2).quantity(2).image("imagen").categoryId(1L).build();
        var category = Category.builder().id(1L).type(Category.Type.MOVIE).active(true).createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now()).build();
        var inserted = Funko.builder().name("nombre").price(2.2).quantity(2).image("imagen").category(category).build();
        when(categoryService.getCategoryById(1L)).thenReturn(category);
        when(funkoMapperImpl.toFunko(insert, category)).thenReturn(inserted);
        when(funkoRepository.save(inserted)).thenReturn(inserted);
        when(funkoMapperImpl.toGetFunkoDTO(inserted))
                .thenReturn(GetFunkoDTO.builder().name("nombre").price(2.2).quantity(2).image("imagen")
                        .category(category).build());
        GetFunkoDTO inserted2 = funkoService.postFunko(insert);
        assertNotNull(inserted2);
        assertAll("Funko properties",
                () -> assertEquals(insert.getName(), inserted2.getName(), "El nombre debe coincidir"),
                () -> assertEquals(insert.getPrice(), inserted2.getPrice(), "El precio debe coincidir"),
                () -> assertEquals(insert.getQuantity(), inserted2.getQuantity(), "La cantidad debe coincidir"),
                () -> assertEquals(insert.getImage(), inserted2.getImage(), "La imagen debe coincidir")
        );
        verify(funkoRepository, times(1)).save(any(Funko.class));
    }

    @Test
    void testPutFunko() throws CategoryNotFoundException, CategoryNotValidIDException, FunkoNotValidUUIDException, FunkoNotFoundException {
        var update = UpdateFunkoDTO.builder()
                .name("nombre").price(2.2).quantity(2).image("imagen").categoryId(1L).build();
        var category = Category.builder().id(1L).type(Category.Type.MOVIE).active(true).createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now()).build();
        when(funkoRepository.findById(list.get(0).getId())).thenReturn(Optional.of(list.get(0)));
        when(categoryService.getCategoryById(1L)).thenReturn(category);
        when(funkoMapperImpl.toFunko(list.get(0), update, category)).thenReturn(list.get(0));
        when(funkoRepository.save(list.get(0))).thenReturn(list.get(0));
        when(funkoMapperImpl.toGetFunkoDTO(list.get(0)))
                .thenReturn(GetFunkoDTO.builder().name("nombre").price(2.2).quantity(2).image("imagen")
                        .category(category).build());
        GetFunkoDTO updated2 = funkoService.putFunko(list.get(0).getId().toString(), update);
        assertNotNull(updated2);
        assertAll("Funko properties",
                () -> assertEquals(update.getName(), updated2.getName(), "El nombre debe coincidir"),
                () -> assertEquals(update.getPrice(), updated2.getPrice(), "El precio debe coincidir"),
                () -> assertEquals(update.getQuantity(), updated2.getQuantity(), "La cantidad debe coincidir"),
                () -> assertEquals(update.getImage(), updated2.getImage(), "La imagen debe coincidir")
        );
        verify(funkoRepository, times(1)).save(any(Funko.class));
    }

    @Test
    void testPatchFunko() throws CategoryNotFoundException, CategoryNotValidIDException, FunkoNotValidUUIDException, FunkoNotFoundException {
        var update = PatchFunkoDTO.builder()
                .name("nombre").price(2.2).quantity(2).image("imagen").categoryId(1L).build();
        var category = Category.builder().id(1L).type(Category.Type.MOVIE).active(true).createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now()).build();
        when(funkoRepository.findById(list.get(0).getId())).thenReturn(Optional.of(list.get(0)));
        when(categoryService.getCategoryById(1L)).thenReturn(category);
        when(funkoRepository.save(list.get(0))).thenReturn(list.get(0));
        when(funkoMapperImpl.toGetFunkoDTO(list.get(0)))
                .thenReturn(GetFunkoDTO.builder().name("nombre").price(2.2).quantity(2).image("imagen")
                        .category(category).build());
        GetFunkoDTO updated2 = funkoService.patchFunko(list.get(0).getId().toString(), update);
        assertNotNull(updated2);
        assertAll("Funko properties",
                () -> assertEquals(update.getName(), updated2.getName(), "El nombre debe coincidir"),
                () -> assertEquals(update.getPrice(), updated2.getPrice(), "El precio debe coincidir"),
                () -> assertEquals(update.getQuantity(), updated2.getQuantity(), "La cantidad debe coincidir"),
                () -> assertEquals(update.getImage(), updated2.getImage(), "La imagen debe coincidir")
        );
        verify(funkoRepository, times(1)).save(any(Funko.class));
    }

    @Test
    void testNotValidUUIDPutFunko() {
        assertThrows(FunkoNotValidUUIDException.class, () -> funkoService.putFunko("()", UpdateFunkoDTO.builder()
                .price(14.99)
                .quantity(7)
                .build()));
    }

    @Test
    void testNotValidUUIDPatchFunko() {
        assertThrows(FunkoNotValidUUIDException.class, () -> funkoService.patchFunko("()", PatchFunkoDTO.builder()
                .price(14.99)
                .quantity(7)
                .build()));
    }

    @Test
    void testDeleteFunko() throws FunkoNotValidUUIDException, FunkoNotFoundException {
        when(funkoRepository.findById(list.get(0).getId())).thenReturn(Optional.ofNullable(list.get(0)));
        doNothing().when(funkoRepository).delete(any(Funko.class));
        funkoService.deleteFunko(String.valueOf(list.get(0).getId()));
        assertEquals(0, funkoService.getAllFunko().size());
        verify(funkoRepository, times(1)).delete(any(Funko.class));
    }

    @Test
    void testNotValidUUIDDeleteFunko() {
        assertThrows(FunkoNotValidUUIDException.class, () -> funkoService.deleteFunko("()"));
    }

    @Test
    void testDeleteFunkoNotFound() {
        assertThrows(FunkoNotFoundException.class, () -> funkoService.deleteFunko(UUID.randomUUID().toString()));
    }

    @Test
    void testDeleteFunkoNotValidUUID() {
        String invalidUUID = "UUID NO VÁLIDA";
        assertThrows(FunkoNotValidUUIDException.class, () -> funkoService.deleteFunko(invalidUUID));
    }
}
