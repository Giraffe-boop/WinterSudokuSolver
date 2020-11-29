package com.winter.sudoku;

import java.util.Optional;
import java.util.function.Function;

public interface SudokuSolver extends Function<Sudoku, Optional<Sudoku>> {
}
