package com.winter.sudoku;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ReattemptingSudokuSolverTest {

    static final Integer n = null;

    @Test
    void apply() {

        SudokuSolver solver = new ReattemptingSudokuSolver(new MutatingSudokuSolver(), 1000);

        Integer[] i4 = {
                2,n,  n,n,
                n,1,  n,2,

                n,n,  3,n,
                n,n,  n,4,
        };
        long start = System.currentTimeMillis();
        Optional<Sudoku> validSolutions = solver.apply(new Sudoku("i4", i4));
        System.out.println("Seconds to solve: " + (System.currentTimeMillis() - start)/1000d);
        assertTrue(validSolutions.isPresent());
        System.out.println(validSolutions.get());

    }
}