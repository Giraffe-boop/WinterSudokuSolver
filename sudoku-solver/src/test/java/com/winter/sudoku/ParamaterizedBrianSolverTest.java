package com.winter.sudoku;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import javax.swing.text.AbstractDocument;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ParamaterizedBrianSolverTest {

    final SudokuSolver solver = new ReattemptingSudokuSolver(new BrianSolver(), 10_000_000_000L);

    static final Integer n = null;

    enum Puzzles {
        //https://www.websudoku.com/?level=1&set_id=5323986727
        EasyOne(
                new Integer[]{
                        n,2,n,   6,8,5,    n,n,7,
                        8,n,n,   2,n,n,    6,n,9,
                        n,n,n,   4,n,n,    n,8,3,

                        n,6,n,   n,n,4,    n,n,8,
                        4,n,9,   n,n,n,    3,n,1,
                        2,n,n,   1,n,n,    n,4,n,

                        1,9,n,   n,n,6,    n,n,n,
                        7,n,2,   n,n,8,    n,n,6,
                        3,n,n,   5,2,9,    n,7,n
                }
        ),
        //https://www.websudoku.com/?level=1&set_id=6460171288
        EasyTwo(
                new Integer[]{
                        4,n,n,   8,7,n,    n,9,2,
                        n,6,7,   2,n,n,    n,n,5,
                        2,n,8,   n,n,n,    n,n,n,

                        n,4,n,   1,2,n,    n,6,3,
                        5,n,2,   n,n,n,    9,n,7,
                        3,7,n,   n,5,9,    n,8,n,

                        n,n,n,   n,n,n,    1,n,9,
                        1,n,n,   n,n,2,    3,5,n,
                        7,8,n,   n,1,5,    n,n,6
                }
        ),
        // https://www.websudoku.com/?level=1&set_id=7755524038
        EasyThree(
                new Integer[]{
                        5,8,n,   n,2,n,    n,3,7,
                        n,n,4,   n,n,1,    n,n,8,
                        2,n,9,   n,n,n,    n,6,n,

                        n,n,5,   1,4,2,    n,8,3,
                        n,n,7,   n,n,n,    6,n,n,
                        8,4,n,   6,9,7,    5,n,n,

                        n,7,n,   n,n,n,    3,n,9,
                        3,n,n,   2,n,n,    8,n,n,
                        6,9,n,   n,5,n,    n,7,1
                }
        ),
        //https://www.websudoku.com/?level=2&set_id=8448679181
        MediumFour(
                new Integer[]{
                        n,n,7,   n,9,n,    n,6,4,
                        4,n,n,   n,n,n,    2,n,n,
                        8,3,n,   4,n,n,    n,n,n,

                        n,1,8,   n,2,n,    n,3,n,
                        3,9,2,   n,n,n,    6,8,5,
                        n,7,n,   n,3,n,    9,2,n,

                        n,n,n,   n,n,8,    n,7,2,
                        n,n,6,   n,n,n,    n,n,8,
                        2,8,n,   n,5,n,    1,n,n
                }
        ),
        MediumFive(
                //https://www.websudoku.com/?level=2&set_id=9988486970
                new Integer[]{
                        n,n,n,   5,n,n,    2,n,1,
                        1,n,n,   n,9,2,    6,8,n,
                        n,n,8,   n,7,n,    n,n,n,

                        n,2,n,   n,8,n,    9,n,5,
                        n,n,n,   7,n,1,    n,n,n,
                        8,n,1,   n,5,n,    n,3,n,

                        n,n,n,   n,6,n,    4,n,n,
                        n,8,7,   2,4,n,    n,n,6,
                        6,n,4,   n,n,5,    n,n,n
                }
        ),
        //https://www.websudoku.com/?level=2&set_id=1023255041
        MediumSix(
                new Integer[]{
                        n,2,n,   3,n,8,    9,n,4,
                        n,3,n,   7,n,n,    n,n,1,
                        6,n,8,   2,n,n,    n,n,n,

                        n,n,n,   9,n,n,    n,5,2,
                        n,9,n,   n,n,n,    n,1,n,
                        5,8,n,   n,n,7,    n,n,n,

                        n,n,n,   n,n,2,    7,n,6,
                        3,n,n,   n,n,6,    n,2,n,
                        2,n,7,   5,n,1,    n,4,n
                }
        ),
        HardSeven(
                //https://www.websudoku.com/?level=3&set_id=5969018919
                new Integer[]{
                        n,5,n,   n,n,n,    n,n,4,
                        8,n,4,   3,n,n,    n,n,n,
                        n,9,n,   2,6,n,    n,8,n,

                        n,n,n,   n,1,9,    n,5,6,
                        n,3,n,   n,n,n,    n,9,n,
                        9,7,n,   5,3,n,    n,n,n,

                        n,6,n,   n,7,2,    n,3,n,
                        n,n,n,   n,n,5,    9,n,2,
                        4,n,n,   n,n,n,    n,1,n
                }
        ),
        //https://www.websudoku.com/?level=3&set_id=6854752147
        HardEight(
                new Integer[]{
                        n,n,n,   n,n,6,    7,n,8,
                        6,n,n,   n,7,3,    n,n,n,
                        3,8,n,   n,4,n,    2,n,n,

                        4,9,n,   n,n,n,    n,n,n,
                        n,n,1,   n,6,n,    4,n,n,
                        n,n,n,   n,n,n,    n,8,9,

                        n,n,8,   n,9,n,    n,6,5,
                        n,n,n,   5,2,n,    n,n,1,
                        1,n,9,   6,n,n,    n,n,n
                }
        ),
        //https://www.websudoku.com/?level=4&set_id=9659996368
        EvilNine(
                new Integer[]{
                        n,n,n,   2,9,n,    6,n,n,
                        n,n,8,   n,n,5,    n,2,n,
                        7,n,n,   n,3,n,    n,n,n,

                        n,3,n,   n,n,n,    2,8,n,
                        n,5,4,   n,n,n,    9,6,n,
                        n,2,7,   n,n,n,    1,n,n,

                        n,n,n,   n,4,n,    n,n,8,
                        n,4,n,   3,n,n,    7,n,n,
                        n,n,9,   n,1,2,    n,n,n
                }
        ),
        EvilTen(
                //https://www.websudoku.com/?level=4&set_id=6297382530
                new Integer[]{
                        n,n,5,   n,n,n,    n,n,n,
                        n,n,9,   6,n,n,    n,8,3,
                        1,n,n,   3,9,n,    n,n,5,

                        n,2,n,   1,5,n,    n,n,n,
                        n,n,n,   n,n,n,    n,n,n,
                        n,n,n,   n,6,7,    n,2,n,

                        6,n,n,   n,4,3,    n,n,2,
                        7,8,n,   n,n,1,    4,n,n,
                        n,n,n,   n,n,n,    5,n,n
                }
        ),
        ;

        final Integer[] values;

        Puzzles(Integer[] values) {
            this.values = values;
        }
    }

    @ParameterizedTest
    @EnumSource(Puzzles.class)
    public void mutateAndCheck9(Puzzles puzzle){

        long start = System.currentTimeMillis();
        final Sudoku initial = new Sudoku(puzzle.name(), puzzle.values);
        //use IntStream to constrain attempts and give an incrementing #
        Optional<Sudoku> solution = solver.apply(initial);

        assertTrue(solution.isPresent());
        System.out.println("Seconds to solve: " + (System.currentTimeMillis() - start)/1000d);
        System.out.println("Instance #: " + solution.get().getCount());
        System.out.println(solution.get());

    }

}