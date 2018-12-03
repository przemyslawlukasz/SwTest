package slidingpuzzle;

public class UserSolution {

    private static final int BOARD_SIZE = 5;
    private static final int PATTERN_SIZE = 3;
    private static int[][] board = new int[BOARD_SIZE][BOARD_SIZE];
    private static int[][] pattern = new int[PATTERN_SIZE][PATTERN_SIZE];

    private static final int MOVES = 4, UP = 0, RIGHT = 1, DOWN = 2, LEFT = 3;
    private static final int[] dx = new int[] { 0, 1, 0, -1}, dy = new int[] { -1, 0, 1, 0};

    private static final int BLANK = 0;
    private static int blankX, blankY;

    private static final int NR_OR_COLORS = 7;
    private static final int BLOCKS_IN_COLOR = 4;

    private static final Block[][] boardBlocks = new Block[BOARD_SIZE][BOARD_SIZE];
    private static final Block[][] blocksByColor = new Block[NR_OR_COLORS][BLOCKS_IN_COLOR];

    private static final int MIN_SWAP_CALLS_LIMIT = 100, MAX_SWAP_CALLS_LIMIT = 500;
    private static int swapCallsLimit;
    private static int swapCallsCnt;
    private static final int moves[][][] = new int[PATTERN_SIZE][PATTERN_SIZE][MAX_SWAP_CALLS_LIMIT];


    public static void solve(int[][] board, int[][] pattern, int swapCallsLimit) {
        UserSolution.board = board;
        UserSolution.pattern = pattern;
        UserSolution.swapCallsLimit = swapCallsLimit;
        swapCallsCnt = 0;
        fillBlockTables();
        solve();
    }

    private static void solve() {
        solve(1, 1);
        for(int patternY = 0; patternY < PATTERN_SIZE; patternY++) {
            for(int patternX = 0; patternX < PATTERN_SIZE; patternX++) {
                solve(patternX, patternY);
            }
        }

    }

    private static void solve(int patternX, int patternY) {
        if(isCorrect(patternX, patternY))
            return;
        Block[] blocksByDistance = getBlocksByDistance(pattern[patternY][patternX], boardBlocks[patternY+1][patternX+1].positioin);
        int[] minimalSequence = new int[MAX_SWAP_CALLS_LIMIT];
        for(Block block : blocksByDistance) {
            int[] currentSequence = getMovesSequence(block, patternX+1, patternY+1);
            if(currentSequence[0] < minimalSequence[0])
                minimalSequence = currentSequence;
        }
        moves[patternY][patternX] = minimalSequence;
    }

    private static int[] getMovesSequence(Block block, int boardX, int boardY) {

    }

    private static Block[] getBlocksByDistance(int color, Position position) {
        Block[] blocksByDistance = new Block[BLOCKS_IN_COLOR];
        for(int i = 0; i < BLOCKS_IN_COLOR; i++) {
            blocksByDistance[i] = blocksByColor[color][i];
            sortByDistanceFromPosition(blocksByDistance, position);
        }
        return blocksByDistance;
    }

    private static void sortByDistanceFromPosition(Block[] array, Position position) {
        for(int i = 0; i < BLOCKS_IN_COLOR-1; i++) {
            for(int j = i; j < BLOCKS_IN_COLOR-1; j++) {
                if(array[j].distanceTo(position) > array[j+1].distanceTo(position)) {
                    Block temp = array[j];
                    array[j] = array[j+1];
                    array[j+1] = temp;
                }
            }
        }
    }

    private static boolean isOnPatternPart(int x, int y) {
        return x >= 1 && x <= 3 && y >= 1 && y <= 3;
    }

    private static boolean isCorrect(int x, int y) {
        return pattern[y][x] == board[y+1][x+1];
    }

    private static void fillBlockTables() {
        int[] blocksByColorCounters = new int[NR_OR_COLORS];
        for(int color = 0; color <= NR_OR_COLORS; color++)
            blocksByColorCounters[color] = 0;

        for(int y = 0; y < BOARD_SIZE; y++) {
            for(int x = 0; x < BOARD_SIZE; x++) {
                Block block = new Block(x, y, board[y][x]);
                boardBlocks[y][x] = block;
                blocksByColor[block.color][blocksByColorCounters[block.color]++] = block;
            }
        }
    }


    private static boolean swap(int dir) {
        swapCallsCnt++;
        if(swapCallsCnt > swapCallsLimit) {
            return false;
        }

        if(dir < UP || dir > LEFT)
            return false;

        int newX = blankX+dx[dir], newY = blankY+dy[dir];

        if(newX < 0 || newX >= BOARD_SIZE || newY < 0 || newY >= BOARD_SIZE)
            return false;

        if(isOnPatternPart(newX, newY) && isCorrect(newX-1, newY-1))
            return false;

        board[blankY][blankX] = board[newY][newX];
        board[newY][newX] = BLANK;
        blankX = newX;
        blankY = newY;
        return true;
    }

    public static class Block {
        final Position positioin;
        final int color;
        public Block(int x, int y, int color) {
            positioin = new Position(x, y);
            this.color = color;
        }

        int distanceTo(Position pos) {
            return pos.distances[positioin.y][positioin.x];
        }
    }

    static class Position {
        final int x, y;
        final int[][] distances = new int[PATTERN_SIZE][PATTERN_SIZE];
        Position(int x, int y) {
            this.x = x;
            this.y = y;
            for(int patternY = 0; patternY < PATTERN_SIZE; patternY++)
                for(int patternX = 0; patternX < PATTERN_SIZE; patternX++) {
                    distances[y][x] = calculateDistanceTo(patternX+1, patternY+1);
                }
        }

        int calculateDistanceTo(int destX, int destY) {
            int distX = destX > x ? destX - x : x - destX;
            int distY = destY > y ? destY - y : y - destY;
            return distX + distY;
        }
    }
}
