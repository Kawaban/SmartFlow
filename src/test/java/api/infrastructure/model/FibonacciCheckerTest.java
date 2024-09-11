package api.infrastructure.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class FibonacciCheckerTest {

    private final FibonacciChecker fibonacciChecker = new FibonacciChecker();


    @Test
    void testIsFibonacci(){
        assertTrue(fibonacciChecker.isFibonacci(89));
        assertTrue(fibonacciChecker.isFibonacci(144));
    }

    @Test
    void testIsNotFibonacci(){
        assertTrue(!fibonacciChecker.isFibonacci(90));
        assertTrue(!fibonacciChecker.isFibonacci(145));
    }
}
