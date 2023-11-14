package com.madirex.funkosspringrest;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Clase FunkosSpringRestApplicationTest
 */
@SpringBootTest(useMainMethod = SpringBootTest.UseMainMethod.ALWAYS)
class FunkosSpringRestApplicationTest {

    /**
     * Test simple del Main
     */
    @Test
    void testMain() {
        assertThrows(IllegalArgumentException.class, () -> FunkosSpringRestApplication.main(null));
    }
}