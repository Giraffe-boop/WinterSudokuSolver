package com.winter.sudoku;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

public class ReattemptingSudokuSolver implements SudokuSolver {

    private final SudokuSolver delegate;

    private final long maxAttempts;

    public ReattemptingSudokuSolver(SudokuSolver delegate, long maxAttempts) {
        this.delegate = delegate;
        this.maxAttempts = maxAttempts;
    }

    @Override
    public Optional<Sudoku> apply(Sudoku sudoku) {
        long start = System.currentTimeMillis();
        //use IntStream to constrain attempts and give an incrementing #
        return LongStream.range(0,maxAttempts)
                .parallel()
                .mapToObj(i -> sudoku)
                .map(delegate::apply)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .filter(m -> {
                    List<Sudoku.DistinctiveElement> invalidElements = m.findInvalidElements();
                    return invalidElements.isEmpty();
                })
                .findAny();

    }
}
