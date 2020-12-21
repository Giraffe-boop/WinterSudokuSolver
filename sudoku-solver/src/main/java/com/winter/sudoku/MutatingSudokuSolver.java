package com.winter.sudoku;

import com.google.common.collect.Sets;

import java.util.*;
import java.util.stream.Collectors;

public class MutatingSudokuSolver implements SudokuSolver{
    private static final Random RANDOM = new Random();

    @Override
    public Optional<Sudoku> apply(Sudoku initial) {
        final Sudoku sudoku = initial.copy(null);
        for(Sudoku.Cell c : sudoku.getCells()){
            if(!c.isInitial()){
                if(c.possibleValues().isEmpty()){
                    return Optional.empty();
                }
                Set<Integer> possibleValues = c.possibleValues();
                int randomValue = possibleValues.stream()
                        .skip(RANDOM.nextInt(possibleValues.size()))
                        .findFirst().orElseThrow(AssertionError::new);
                c.setContent(randomValue);
            }

        }

        return Optional.of(sudoku);
    }
}
