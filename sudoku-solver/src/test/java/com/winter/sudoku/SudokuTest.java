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



}