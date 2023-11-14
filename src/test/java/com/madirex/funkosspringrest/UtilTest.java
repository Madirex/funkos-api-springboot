package com.madirex.funkosspringrest;

import com.madirex.funkosspringrest.utils.Util;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;

import static org.springframework.test.util.AssertionErrors.assertTrue;

/**
 * Clase UtilTest
 */
class UtilTest {

    /**
     * Test para comprobar que el constructor es privado
     *
     * @throws Exception excepción
     */
    @Test
    void testPrivateConstructor() throws Exception {
        Constructor<Util> constructor = Util.class.getDeclaredConstructor();
        assertTrue("El constructor no es privado", Modifier.isPrivate(constructor.getModifiers()));
        constructor.setAccessible(true);
        constructor.newInstance();
    }
}