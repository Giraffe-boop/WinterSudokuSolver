package com.winter.sudoku;

import com.google.common.collect.Sets;

import java.util.*;
import java.util.stream.Collectors;

public class BrianSolver implements SudokuSolver{
    private static final Random RANDOM = new Random();

    @Override
    public Optional<Sudoku> apply(Sudoku initial) {
        final Sudoku sudoku = initial.copy(null);
        for (Sudoku.Cell c : sudoku.getCells()) {
            if (!c.isInitial()) {
                if (c.possibleValues().isEmpty()) {
                    return Optional.empty();
                }
                c.setContent(getRandomValue(c.possibleValues()));
            }
        }
        return Optional.of(sudoku);
    }

    private int getRandomValue(Set<Integer> values){
        return new ArrayList<>(values).get(RANDOM.nextInt(values.size()));
    }

    private int getRandomValue2(Set<Integer> coll) {
        int num = (int) (Math.random() * coll.size());
        for(int t: coll) if (--num < 0) return t;
        throw new AssertionError();
    }

    private int getRandomValue3(Set<Integer> values){
        return values
                .stream()
                .skip(RANDOM.nextInt(values.size()))
                .findFirst()
                .orElseThrow(AssertionError::new);
    }
}
