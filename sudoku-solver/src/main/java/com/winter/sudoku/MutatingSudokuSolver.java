package com.winter.sudoku;

import com.google.common.collect.Sets;

import java.util.LinkedList;
import java.util.Optional;
import java.util.Random;
import java.util.Set;

public class MutatingSudokuSolver implements SudokuSolver{
    private static final Random RANDOM = new Random();

    @Override
    public Optional<Sudoku> apply(Sudoku initial) {
        final Sudoku sudoku = initial.copy(null);
        int sqrtSize = Long.valueOf(Math.round(Math.sqrt(sudoku.getSize()))).intValue();
        for(int r = 0 ; r < sudoku.getSize() ; r++){
            final Sudoku.DistinctiveElement row = sudoku.getRow(r);
            final Set<Integer> rval = row.getFreeValues();
            for(int c = 0; c < sudoku.getSize(); c++){
                Sudoku.Cell cell = row.getCells().get(c);
                if(!cell.isInitial()) {
                    final Set<Integer> cval = sudoku.getColumn(c).getFreeValues();
                    Set<Integer> rc = Sets.intersection(rval, cval).immutableCopy();
                    if(rc.isEmpty()){
                        return Optional.empty();
                    }
                    int blockRow = Math.floorDiv(r, sqrtSize);
                    int blockCol = Math.floorDiv(c, sqrtSize);
                    int blockIndex = blockRow * sqrtSize + blockCol;
                    final Set<Integer> block = sudoku.getBlock(blockIndex).getFreeValues();
                    Set<Integer> rcb = Sets.intersection(rc, block);
                    if(rcb.isEmpty()){
                        return Optional.empty();
                    }
                    LinkedList<Integer> toShuffle = new LinkedList<>(rcb);
                    cell.setContent(toShuffle.get(RANDOM.nextInt(toShuffle.size())));
                }
            }
        }
        return Optional.of(sudoku);

    }
}
