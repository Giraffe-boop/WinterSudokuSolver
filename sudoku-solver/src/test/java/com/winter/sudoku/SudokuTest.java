package com.winter.sudoku;

import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

class SudokuTest {

    @Test
    public void startingSudokuIsNotValid () {
        Sudoku sudoku = new Sudoku("name", new Integer[]{null,3,4,null,4,null,null,2,1,null,null,3,null,2,1,null});
        assertFalse(sudoku.findInvalidElements().isEmpty());
    }

    @Test
    public void solvedPuzzleIsValid () {
        Sudoku sudoku = new Sudoku("name", new Integer[]{1,4,3,2,3,2,4,1,4,1,2,3,2,3,1,4});
        List<Sudoku.DistinctiveElement> invalidElements = sudoku.findInvalidElements();
        System.out.println(invalidElements);
        assertTrue(invalidElements.isEmpty());
    }

    @Test
    public void solvedPuzzleIsNotValid () {
        Sudoku toCopy = new Sudoku("name", new Integer[]{null,4,3,2,3,2,4,1,4,1,2,3,2,3,1,4});
        assertFalse(toCopy.findInvalidElements().isEmpty());
        Sudoku copy = toCopy.copy("name copy");
        copy.setValue(0,0,1);
        assertEquals(0, copy.findInvalidElements().size());

        copy.setValue(0,0,4);
        assertEquals(3, copy.findInvalidElements().size());
        assertEquals("0,0,0", copy.findInvalidElements().stream().map(e -> "" + e.getIndex()).collect(Collectors.joining(",")));
    }

    @Test
    public void solvedPuzzleIsNotValidC2 () {
        Sudoku toCopy = new Sudoku("name", new Integer[]{1,4,null,2,3,2,4,1,4,1,2,3,2,3,1,4});
        assertFalse(toCopy.findInvalidElements().isEmpty());
        Sudoku copy = toCopy.copy("name copy");
        copy.setValue(0,2,3);
        assertEquals(0, copy.findInvalidElements().size());

        copy.setValue(0,2,4);
        assertEquals(3, copy.findInvalidElements().size());
        assertEquals("ROW-0,COLUMN-2,BLOCK-1", copy.findInvalidElements().stream().map(e -> "" + e.getId()).collect(Collectors.joining(",")));
    }

    static final Integer n = null;
    @Test
    public void mutateAndCheck9(){

        Integer[] i9 = {
                1,n,n,   n,7,9,    n,8,n,
                5,9,n,   n,n,2,    7,3,4,
                7,n,n,   5,3,8,    n,n,9,

                n,n,n,   3,4,n,    n,2,n,
                3,4,n,   7,2,n,    n,5,1,
                n,5,n,   8,n,n,    n,n,3,

                n,n,7,   9,n,3,    5,n,8,
                9,1,n,   n,n,n,    3,n,n,
                6,n,n,   n,n,n,    1,9,n
        };
        long start = System.currentTimeMillis();
        //use IntStream to constrain attempts and give an incrementing #
        Optional<Sudoku> validSolutions = IntStream.range(0,10000000)
                .mapToObj(i -> new Sudoku("i9", i9).solve())
                .parallel()
                .filter(Optional::isPresent)
                .map(Optional::get)
                .filter(m -> {
                    List<Sudoku.DistinctiveElement> invalidElements = m.findInvalidElements();
                    return invalidElements.isEmpty();
                })
                .findAny();

        System.out.println("Seconds to solve: " + (System.currentTimeMillis() - start)/1000d);

        assertTrue(validSolutions.isPresent());
        System.out.println("Instance #: " + validSolutions.get().getCount());
        System.out.println(validSolutions.get());

    }

    @Test
    public void mutateAndCheck4(){

        Integer[] i4 = {
                2,n,  n,n,
                n,1,  n,2,

                n,n,  3,n,
                n,n,  n,4,
        };
        long start = System.currentTimeMillis();
        Optional<Sudoku> validSolutions = IntStream.range(0,10000)
                //.parallel()
                .mapToObj(i -> new Sudoku("i4", i4).solve())
                .filter(Optional::isPresent)
                .map(Optional::get)
                .filter(m -> {
                    List<Sudoku.DistinctiveElement> invalidElements = m.findInvalidElements();
                    return invalidElements.isEmpty();
                })
                .findAny();

        System.out.println("Seconds to solve: " + (System.currentTimeMillis() - start)/1000d);
        assertTrue(validSolutions.isPresent());
        System.out.println(validSolutions.get());

    }

}