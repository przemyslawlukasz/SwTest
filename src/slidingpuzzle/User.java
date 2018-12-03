package slidingpuzzle;

public class User {

    private static final int BOARD_SIZE = 5;
    private static final int PATTERN_SIZE = 3;
    private static final int[][] originalBoard = new int[BOARD_SIZE][BOARD_SIZE];
    private static final int[][] originalPattern = new int[PATTERN_SIZE][PATTERN_SIZE];

    private static final int MOVES = 4, UP = 0, RIGHT = 1, DOWN = 2, LEFT = 3;
    private static final int[] dx = new int[] { 0, 1, 0, -1}, dy = new int[] { -1, 0, 1, 0};

    private static final int BLANK = 0;
    private static int blankX, blankY;

    private static final int NR_OR_COLORS = 6;
    private static final int BLOCKS_IN_COLOR = 4;

    private static final int[] moves = new int[] {UP, LEFT,
                                                DOWN, RIGHT, RIGHT, UP, UP,
                                                DOWN, LEFT, LEFT, LEFT, LEFT,
                                                RIGHT, RIGHT, RIGHT, RIGHT, RIGHT, RIGHT,
                                                DOWN, DOWN, DOWN, DOWN, DOWN, DOWN,
                                                UP, LEFT};

    private static final int MIN_SWAP_CALLS_LIMIT = 100, MAX_SWAP_CALLS_LIMIT = 500;
    private static int swapCallsCnt, swapCallsLimit;

    public static void main(String[] args) {
        generateBoard();
        printBoard();
        generatePattern();
        printPattern();
        //testPath(moves);
        UserSolution.solve(copy2DimArray(originalBoard), copy2DimArray(originalPattern), swapCallsLimit = 500);
        printCheckPattern();
    }

    private static void testCase() {
        generateBoard();
        generatePattern();
        UserSolution.solve(copy2DimArray(originalBoard), copy2DimArray(originalPattern), swapCallsLimit = 500);
        printCheckPattern();
    }

    private static void printCheckPattern() {
        System.out.println("checkPattern = " + checkPattern());
    }

    private static void testPath(int[] path) {
        printPattern(); printCheckPattern();
        for(int move : path) {
            swapAndPrint(move);
        }
        printCheckPattern();
    }

    private static int[][] copy2DimArray(int[][] originalArray) {
        int[][] copy = new int[originalArray.length][originalArray[0].length];
        for(int y = 0; y < originalArray.length; y++) {
            for(int x = 0; x < originalArray[0].length; x++) {
                copy[y][x] = originalArray[y][x];
            }
        }
        return copy;
    }

    private static void swapAndPrint(int dir) {
        System.out.println("swap " + dir + " = " + swap(dir));
        printBoard();
    }

    public static boolean swap(int dir) {
        swapCallsCnt++;
        if(swapCallsCnt > swapCallsLimit) {
            return false;
        }

        if(dir < UP || dir > LEFT)
            return false;

        int newX = blankX+dx[dir], newY = blankY+dy[dir];

        if(newX < 0 || newX >= BOARD_SIZE || newY < 0 || newY >= BOARD_SIZE)
            return false;

        originalBoard[blankY][blankX] = originalBoard[newY][newX];
        originalBoard[newY][newX] = BLANK;
        blankX = newX;
        blankY = newY;
        return true;
    }

    private static void generateBoard() {
        swapCallsCnt = 0;
        int counter = 3;
        for(int y = 0; y < BOARD_SIZE; y++) {
            for(int x = 0; x < BOARD_SIZE; x++) {
                originalBoard[y][x] = counter++ / BLOCKS_IN_COLOR;
                if(originalBoard[y][x] == BLANK) {
                    blankX = x;
                    blankY = y;
                }
            }
        }
    }

    private static void printBoard() {
        for(int y = 0; y < BOARD_SIZE; y++) {
            for(int x = 0; x < BOARD_SIZE; x++) {
                System.out.print(originalBoard[y][x] + " ");
            }
            System.out.println();
        }
    }

    private static void generatePattern() {
        int counter = 0;
        for(int y = 0; y < PATTERN_SIZE; y++) {
            for(int x = 0; x < PATTERN_SIZE; x++) {
                originalPattern[y][x] = counter++ % NR_OR_COLORS +1;
            }
        }
    }

    private static void printPattern() {
        for(int y = 0; y < PATTERN_SIZE; y++) {
            for(int x = 0; x < PATTERN_SIZE; x++) {
                System.out.print(originalPattern[y][x] + " ");
            }
            System.out.println();
        }
    }

    private static boolean checkPattern() {
        for(int y = 0; y < PATTERN_SIZE; y++) {
            for(int x = 0; x < PATTERN_SIZE; x++) {
                if(originalBoard[y+1][x+1] != originalPattern[y][x])
                    return false;
            }
        }
        return true;
    }
}
