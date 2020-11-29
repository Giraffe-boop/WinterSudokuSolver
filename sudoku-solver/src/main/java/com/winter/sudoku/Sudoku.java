package com.winter.sudoku;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;
import com.google.common.collect.Streams;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Sudoku {

    public static final Random RANDOM = new Random();
    private final int size;
    private final int sqrtSize;

    private final String name;
    private final Integer[] initial;
    private final Cell[] current;
    private final List<DistinctiveElement> rows;
    private final List<DistinctiveElement> cols;
    private final List<DistinctiveElement> blocks;
    private final List<DistinctiveElement> allDistinctiveElements;

    private static final AtomicInteger COUNTER = new AtomicInteger(0);
    private final int count;

    public Sudoku(String name, Integer[] initial) {
        this.count = COUNTER.incrementAndGet();
        this.name = name;
        this.size = Long.valueOf(Math.round(Math.sqrt(initial.length))).intValue();
        this.initial = Arrays.copyOf(initial, initial.length);
        sqrtSize = Long.valueOf(Math.round(Math.sqrt(size))).intValue();
        current = new Cell[size*size];
        rows = fluffList(DistinctiveElementType.ROW, size);
        cols = fluffList(DistinctiveElementType.COLUMN, size);
        blocks = new ArrayList<>(size);
        for(int i = 0; i < size; i++){
            blocks.add(new DistinctiveElement(DistinctiveElementType.BLOCK, i, new ArrayList<>(size)));
        }

        for(int i = 0; i < size*size; i++){
            int row = Math.floorDiv(i, size);
            int col = i % size;

            Cell cell;
            if(initial[i] != null){
                cell = new Cell(row, col, initial[i]);
            }else{
                cell = new Cell(row, col);
            }
            current[i] = cell;
            rows.get(row).set(col, cell);
            cols.get(col).set(row, cell);

            int blockRow = Math.floorDiv(row, sqrtSize);
            int blockCol = Math.floorDiv(col, sqrtSize);
            int blockIndex = blockRow * sqrtSize + blockCol;
            blocks.get(blockIndex).add(cell);
        }
        this.allDistinctiveElements = ImmutableList.<DistinctiveElement>builder()
                .addAll(rows)
                .addAll(cols)
                .addAll(blocks)
                .build();
    }

    public int getCount() {
        return count;
    }

    @Override
    public String toString() {
        return "Puzzle: " + name + "\n" + rows.stream()
                .map(c -> c.toString())
                .collect(Collectors.joining("\n"));
    }

    private List<DistinctiveElement> fluffList(DistinctiveElementType type, int size) {
        List<DistinctiveElement> list = new ArrayList<>(9);
        for(int i = 0 ; i < size ; i++){
            ArrayList<Cell> cells = new ArrayList<>(size);
            for(int j = 0; j < size; j++){
                cells.add(new Cell(i, j, null));
            }
            list.add(new DistinctiveElement(type, i, cells));
        }
        return list;
    }

    public int getSize() {
        return size;
    }

    public boolean setValue(int row, int col, Integer value){
        Cell cell = this.current[size*row+col];
        if(cell.isInitial()){
            return false;
        }else {
            cell.setContent(value);
            return true;
        }
    }

    public Sudoku copy(String name){
        Sudoku copy = new Sudoku(name != null ? name : this.name, initial);
        for(int i = 0 ; i < size*size ; i++){
            copy.current[i].setContent(current[i].content);
        }
        return copy;
    }

    public List<DistinctiveElement> findInvalidElements(){
        return allDistinctiveElements.parallelStream()
                .filter(de -> !de.isDistinctive())
                .collect(Collectors.toList());
    }

    public List<DistinctiveElement> getRows() {
        return rows;
    }

    public DistinctiveElement getRow(int r) {
        return this.rows.get(r);
    }

    public DistinctiveElement getColumn(int c) {
        return this.cols.get(c);
    }

    public DistinctiveElement getBlock(int blockIndex) {
        return this.blocks.get(blockIndex);
    }

    public static class Cell {
        private final int row;
        private final int col;
        private Integer content;

        private final boolean initial;

        public Cell(int row, int col, Integer content) {
            this.row = row;
            this.col = col;
            initial = true;
            this.content = content;
        }

        public Cell(int row, int col) {
            this.row = row;
            this.col = col;
            initial = false;
        }

        @Override
        public String toString() {
            return "Cell{" +
                    "row=" + row +
                    ", col=" + col +
                    ", content=" + content +
                    ", initial=" + initial +
                    '}';
        }

        public Integer getContent() {
            return content;
        }

        public Cell setContent(Integer content) {
            this.content = content;
            return this;
        }

        public boolean isInitial() {
            return initial;
        }

        public int getRow() {
            return row;
        }

        public int getCol() {
            return col;
        }
    }

    public enum DistinctiveElementType {
        ROW, COLUMN, BLOCK;
    }

    public static class DistinctiveElement {
        private final DistinctiveElementType type;
        private final int index;
        private final List<Cell> cells;
        private int trueSum;

        private DistinctiveElement(DistinctiveElementType type, int index, List<Cell> cells){
            this.type = type;
            this.index = index;
            this.cells = new ArrayList<>(cells);
            trueSum = Math.round(cells.size()/2f * (cells.size()+1));
        }

        public DistinctiveElementType getType() {
            return type;
        }

        public Set<Integer> getFreeValues(){
            Set<Integer> usedValues = cells()
                    .map(Cell::getContent)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());
            return IntStream.rangeClosed(1,cells.size())
                    .boxed()
                    .filter(v -> !usedValues.contains(v))
                    .collect(Collectors.toSet());
        }

        public boolean isDistinctive(){
            int sum = cells.stream()
                    .map(Cell::getContent)
                    .filter(Objects::nonNull)
                    .reduce(0, Integer::sum);
            if(sum != trueSum){
                return false;
            }

            int uniqueSize = cells.stream()
                    .map(Cell::getContent)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet()).size();
            return uniqueSize == cells.size();
        }

        public int getIndex() {
            return index;
        }

        public String getId(){
            return this.type.name() + "-" + index;
        }

        @Override
        public String toString() {
            return cells.stream()
                    .map(c -> String.valueOf(c.getContent()))
                    .collect(Collectors.joining(","));
        }

        public void add(Cell cell) {
            this.cells.add(cell);
            trueSum = Math.round(cells.size()/2f * (cells.size()+1));
        }

        public void set(int col, Cell cell) {
            this.cells.set(col, cell);
            trueSum = Math.round(cells.size()/2f * (cells.size()+1));
        }

        public Stream<Cell> cells() {
            return cells.stream();
        }

        public List<Cell> getCells() {
            return this.cells;
        }
    }

}
