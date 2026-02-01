package main;

import org.example.Main;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MainTest {

    @Test
    void sum_shouldReturnCorrectAddition() {
        int result = Main.sum(2, 3);
        assertEquals(5, result);
    }

    @Test
    void sum_shouldHandleZero() {
        assertEquals(3, Main.sum(3, 0));
        assertEquals(3, Main.sum(0, 3));
    }

}

