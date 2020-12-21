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

    private final String name;
    private final Integer[] initial;
    private Stack<Integer[]> saves = new Stack<>();
    private final List<Cell> cells;
    private final Map<Integer, DistinctiveElement> rows;
    private final Map<Integer, DistinctiveElement> cols;
    private final Map<Integer, DistinctiveElement> blocks;
    private final List<DistinctiveElement> allDistinctiveElements;

    private static final AtomicInteger COUNTER = new AtomicInteger(0);
    private final int count;

    public Sudoku(String name, Integer[] initial) {
        this.initial = initial;
        this.count = COUNTER.incrementAndGet();
        this.name = name;
        this.size = Long.valueOf(Math.round(Math.sqrt(initial.length))).intValue();
        saves.push(Arrays.copyOf(initial, initial.length));
        int sqrtSize = Long.valueOf(Math.round(Math.sqrt(size))).intValue();
        cells = new ArrayList<>(initial.length);

        rows = fluff(DistinctiveElementType.ROW, size);
        cols = fluff(DistinctiveElementType.COLUMN, size);
        blocks = fluff(DistinctiveElementType.BLOCK, size);

        for(int i = 0 ; i < initial.length ; i++){
            int rowIndex = Math.floorDiv(i, size);
            DistinctiveElement row = rows.get(rowIndex);
            int colIndex = i % size;
            DistinctiveElement col = cols.get(colIndex);

            int blockIndex = Math.floorDiv(rowIndex, sqrtSize) * sqrtSize + Math.floorDiv(colIndex, sqrtSize);
            DistinctiveElement block = blocks.get(blockIndex);

            Cell cell = new Cell(row, col, block, initial[i]);
            row.add(cell);
            col.add(cell);
            block.add(cell);
            cells.add(cell);
        }

        this.allDistinctiveElements = ImmutableList.<DistinctiveElement>builder()
                .addAll(rows.values())
                .addAll(cols.values())
                .addAll(blocks.values())
                .build();

        fill(initial);
        if(1==1){
            return;
        }
    }

    private Map<Integer, DistinctiveElement> fluff(DistinctiveElementType type, int size) {
        Map<Integer, DistinctiveElement> fluffed = new HashMap<>();
        for(int i = 0 ; i < size; i++){
            fluffed.put(i, new DistinctiveElement(type, size));
        }
        return fluffed;
    }

    private void fill(Integer[] values){
        for(int i = 0; i < values.length; i++){
            cells.get(i).setContent(values[i]);
        }
    }

    public int getCount() {
        return count;
    }

    @Override
    public String toString() {
        return "Puzzle: " + name + "\n" + rows.values().stream()
                .map(c -> c.toString())
                .collect(Collectors.joining("\n"));
    }

    public int getSize() {
        return size;
    }

    public boolean setValue(int row, int col, Integer value){
        Cell cell = this.cells.get(size*row+col);
        if(cell.isInitial()){
            return false;
        }else {
            cell.setContent(value);
            return true;
        }
    }

    public Sudoku copy(String name){
        return new Sudoku(name != null ? name : this.name, initial);
    }

    public List<DistinctiveElement> findInvalidElements(){
        return allDistinctiveElements.parallelStream()
                .filter(de -> !de.isDistinctive())
                .collect(Collectors.toList());
    }

    public List<Cell> getCells() {
        return this.cells;
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
        private final DistinctiveElement row;
        private final DistinctiveElement col;
        private final DistinctiveElement block;
        private Integer content;

        private final boolean initial;

        public Cell(DistinctiveElement row, DistinctiveElement col, DistinctiveElement block, Integer content) {
            this.row = row;
            this.col = col;
            this.block = block;
            initial = content != null;
            this.content = content;
        }

        @Override
        public String toString() {
            return "Cell{" +
                    "row=" + row.getIndex() +
                    ", col=" + col.getIndex() +
                    ", block=" + block.getIndex() +
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

        public boolean isEmpty(){
            return this.content == null;
        }

        public Set<Integer> possibleValues(){
            Set<Integer> values = new HashSet<>(row.getFreeValues());
            values.retainAll(col.getFreeValues());
            values.retainAll(block.getFreeValues());
            return values;
        }
    }

    public enum DistinctiveElementType {
        ROW, COLUMN, BLOCK;
    }

    public static class DistinctiveElement {
        private final DistinctiveElementType type;
        private final int index;
        private final List<Cell> cells = new ArrayList<>();
        private int trueSum;

        private DistinctiveElement(DistinctiveElementType type, int index){
            this.type = type;
            this.index = index;
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

        public void add(Cell cell) {
            this.cells.add(cell);
            trueSum = Math.round(cells.size()/2f * (cells.size()+1));
        }

        public void set(int col, Cell cell) {
            this.cells.set(col, cell);
            trueSum = Math.round(cells.size()/2f * (cells.size()+1));
        }

        @Override
        public String toString() {
            return cells.stream()
                    .map(c -> {
                        return c.getContent() == null ? "n" : String.valueOf(c.getContent());
                    })
                    .collect(Collectors.joining(" "));
        }

        public Stream<Cell> cells() {
            return cells.stream();
        }

        public List<Cell> getCells() {
            return this.cells;
        }
    }

}
