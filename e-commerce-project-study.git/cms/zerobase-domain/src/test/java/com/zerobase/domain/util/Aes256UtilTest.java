package com.zerobase.domain.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Aes256UtilTest {

    @Test
    void encrypt() {
        //given
        String encrypted = Aes256Util.encrypt("Hello World");

        //when
        //then
        assertEquals("Hello World", Aes256Util.decrypt(encrypted));

    }

}