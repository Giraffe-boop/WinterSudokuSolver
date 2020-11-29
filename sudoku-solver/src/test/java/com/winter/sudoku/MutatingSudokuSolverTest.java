package com.winter.sudoku;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

class MutatingSudokuSolverTest {

    final MutatingSudokuSolver solver = new MutatingSudokuSolver();

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
                .mapToObj(i -> new Sudoku("i4", i4))
                .parallel()
                .map(solver::apply)
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
        final Sudoku initial = new Sudoku("i9", i9);
        //use IntStream to constrain attempts and give an incrementing #
        Optional<Sudoku> validSolutions = IntStream.range(0,10000000)
                .parallel()
                .mapToObj(i -> initial)
                .map(solver::apply)
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

}