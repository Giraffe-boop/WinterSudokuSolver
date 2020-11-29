package com.winter.sudoku;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

public class ReattemptingSudokuSolver implements SudokuSolver {

    private final SudokuSolver delegate;

    private final int maxAttempts;

    public ReattemptingSudokuSolver(SudokuSolver delegate, int maxAttempts) {
        this.delegate = delegate;
        this.maxAttempts = maxAttempts;
    }

    public SudokuSolver getDelegate() {
        return delegate;
    }

    public int getMaxAttempts() {
        return maxAttempts;
    }

    @Override
    public Optional<Sudoku> apply(Sudoku sudoku) {
        long start = System.currentTimeMillis();
        //use IntStream to constrain attempts and give an incrementing #
        return IntStream.range(0,maxAttempts)
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
