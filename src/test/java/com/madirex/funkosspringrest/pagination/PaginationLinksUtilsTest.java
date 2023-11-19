package com.madirex.funkosspringrest.pagination;

import com.madirex.funkosspringrest.rest.pagination.util.PaginationLinksUtils;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Clase para probar la clase PaginationLinksUtils
 */
class PaginationLinksUtilsTest {

    private final PaginationLinksUtils paginationLinksUtils = new PaginationLinksUtils();

    /**
     * Test para comprobar el link header FirstPage
     */
    @Test
    void createLinkHeader_FirstPage() {
        Page<?> page = new PageImpl<>(List.of("item1", "item2", "item3"),
                PageRequest.of(0, 10), 30);
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString("/api/resource");

        String linkHeader = paginationLinksUtils.createLinkHeader(page, uriBuilder);

        assertAll(
                () -> assertTrue(linkHeader.contains("<" + uriBuilder.replaceQueryParam("page", 1)
                        .replaceQueryParam("size", 10)
                        .build().encode().toUriString() + ">; rel=\"next\"")),
                () -> assertTrue(linkHeader.contains("rel=\"last\""),
                        "Link header should contain 'last' link for the first page")
        );
    }

    /**
     * Test para comprobar el link header LastPage
     */
    @Test
    void createLinkHeader_LastPage() {
        Page<?> page = new PageImpl<>(List.of("item1", "item2", "item3"), PageRequest.of(4, 10),
                30);
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString("/api/resource");

        String linkHeader = paginationLinksUtils.createLinkHeader(page, uriBuilder);

        assertAll(
                () -> assertEquals("<" + uriBuilder.replaceQueryParam("page", 3)
                        .replaceQueryParam("size", 10)
                        .build().encode().toUriString() + ">; rel=\"prev\", " +
                        "<" + uriBuilder.replaceQueryParam("page", 0)
                        .replaceQueryParam("size", 10)
                        .build().encode().toUriString() + ">; rel=\"first\"", linkHeader)
        );

    }

    /**
     * Test para comprobar el link header MiddlePage
     */
    @Test
    void createLinkHeader_MiddlePage() {
        Page<?> page = new PageImpl<>(List.of("item1", "item2", "item3"),
                PageRequest.of(1, 1), 3);
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString("/api/resource");

        String linkHeader = paginationLinksUtils.createLinkHeader(page, uriBuilder);

        assertAll(
                () -> assertEquals("<" + uriBuilder.replaceQueryParam("page", 2)
                        .replaceQueryParam("size", 1)
                        .build().encode().toUriString() + ">; rel=\"next\", " +
                        "<" + uriBuilder.replaceQueryParam("page", 0)
                        .replaceQueryParam("size", 1).build().encode().
                        toUriString() + ">; rel=\"prev\", " +
                        "<" + uriBuilder.replaceQueryParam("page", 0)
                        .replaceQueryParam("size", 1).build().encode()
                        .toUriString() + ">; rel=\"first\", " +
                        "<" + uriBuilder.replaceQueryParam("page", 2)
                        .replaceQueryParam("size", 1).build().encode()
                        .toUriString() + ">; rel=\"last\"", linkHeader)
        );
    }

    /**
     * Test para comprobar que no se genera el link header cuando solo hay una página
     */
    @Test
    void createLinkHeader_SinglePage() {
        Page<?> page = new PageImpl<>(List.of("item1", "item2", "item3"),
                PageRequest.of(0, 10), 3);
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString("/api/resource");

        String linkHeader = paginationLinksUtils.createLinkHeader(page, uriBuilder);

        assertAll(
                () -> assertEquals("", linkHeader)
        );
    }
}
