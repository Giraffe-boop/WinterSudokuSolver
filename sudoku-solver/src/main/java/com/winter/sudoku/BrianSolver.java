package com.winter.sudoku;

import com.google.common.collect.Sets;

import java.util.*;
import java.util.stream.Collectors;

public class BrianSolver implements SudokuSolver{
    private static final Random RANDOM = new Random();

    @Override
    public Optional<Sudoku> apply(Sudoku initial) {
        final Sudoku sudoku = initial.copy(null);
        int sqrtSize = Long.valueOf(Math.round(Math.sqrt(sudoku.getSize()))).intValue();
        for(int r = 0 ; r < sudoku.getSize() ; r++){
            Set<Integer> rowFreeValues = sudoku.getRow(r).getFreeValues();
            if(rowFreeValues.isEmpty()){
                return Optional.empty();
            }
            for(int c = 0 ; c < sudoku.getSize() ; c++) {
               if (sudoku.getRow(r).getCells().get(c).isInitial()){
                   continue;
               }
                final Set<Integer> columnFreeValues = sudoku.getColumn(c).getFreeValues();
                Set<Integer> rowColFreeValues = inCommon(rowFreeValues, columnFreeValues);
                // This next if then statement figures out free values from each of the rows/columns, if not it is thrown out.
                 if (rowColFreeValues.isEmpty()){
                     return Optional.empty();

                }
                final Set<Integer> blockFreeValues = sudoku.getBlock(getBlock(r,c,sqrtSize)).getFreeValues();
                Set<Integer> allFreeValues = inCommon(blockFreeValues, rowColFreeValues);
                // If nothing is in common between the row, column, and block, it can no longer continue.
                if (allFreeValues.isEmpty()){
                    return Optional.empty();

                }
                int newvalue= getRandomValue(allFreeValues);
                sudoku.setValue(r,c,newvalue);

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

    private int getBlock(int r, int c, int squareofSize) {

       int rowBlock = Math.floorDiv(r,squareofSize);
       int colBlock = Math.floorDiv(c, squareofSize);
        return rowBlock*squareofSize + colBlock;

    }

    private Set<Integer> inCommon(Set<Integer> rowFreeValues, Set<Integer> columnFreeValues) {
        Set<Integer> copy = new HashSet<>(rowFreeValues);
        copy.retainAll(columnFreeValues);
        return copy;
    }
}
