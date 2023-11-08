package com.madirex.funkosspringrest;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(useMainMethod = SpringBootTest.UseMainMethod.ALWAYS)
class FunkosSpringRestApplicationTest {

    @Test
    void testMain() {
        assertThrows(IllegalArgumentException.class, () -> FunkosSpringRestApplication.main(null));
    }
}