import java.io.*;
import java.util.*;

public class Main {

    static enum Direction {
        U,
        D,
        L,
        R
    }

    static class Cell {
        final int row;
        final int col;

        Direction direction = null;

        final int endCycleRow;
        final int endCycleCol;

        public Cell(int row, int col, int endCycleRow, int endCycleCol) {
            this.row = row;
            this.col = col;
            this.endCycleRow = endCycleRow;
            this.endCycleCol = endCycleCol;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Cell cell = (Cell) o;
            return row == cell.row &&
                    col == cell.col;
        }

        @Override
        public int hashCode() {
            return Objects.hash(row, col);
        }
    }

    private static List<Cell> findCycledToItselfCells(Cell[][] matrix) {
        List<Cell> cells = new ArrayList<>();
        for (Cell[] row : matrix) {
            for (Cell cell : row) {
                if (cell.row == cell.endCycleRow && cell.col == cell.endCycleCol) {
                    cells.add(cell);
                }
            }
        }
        return cells;
    }

    private static Cell leftNeighbour(Cell[][] matrix, Cell c) {
        return c.col > 0 ? matrix[c.row][c.col - 1] : null;
    }

    private static Cell rightNeighbour(Cell[][] matrix, Cell c) {
        return c.col < matrix.length - 1 ? matrix[c.row][c.col + 1] : null;
    }

    private static Cell upNeighbour(Cell[][] matrix, Cell c) {
        return c.row > 0 ? matrix[c.row - 1][c.col] : null;
    }

    private static Cell downNeighbour(Cell[][] matrix, Cell c) {
        return c.row < matrix.length - 1 ? matrix[c.row + 1][c.col] : null;
    }

    private static void joinSelfCycled(Cell[][] matrix, List<Cell> cells, long pu, long pd, long pl, long pr) {
        boolean preferHorizontalJoin = pl + pr <= pu + pd;

        Set<Cell> cellSet = new HashSet<>(cells);

        boolean removeAnyAdjacent = false;
        while (true) {
            int singleNeighbourRemoved = 0;

            for (Cell c : new ArrayList<>(cellSet)) {
                if (c.direction != null) continue;

                int neighboursCnt = 0;

                Cell left = leftNeighbour(matrix, c);
                if (left != null && cellSet.contains(left) && left.direction == null) {
                    ++neighboursCnt;
                } else left = null;

                Cell right = rightNeighbour(matrix, c);
                if (right != null && cellSet.contains(right) && right.direction == null) {
                    ++neighboursCnt;
                } else right = null;

                Cell up = upNeighbour(matrix, c);
                if (up != null && cellSet.contains(up) && up.direction == null) {
                    ++neighboursCnt;
                } else up = null;

                Cell down = downNeighbour(matrix, c);
                if (down != null && cellSet.contains(down) && down.direction == null) {
                    ++neighboursCnt;
                } else down = null;

                if (neighboursCnt == 0) return;

                if (neighboursCnt == 1) {
                    if (left != null) {
                        left.direction = Direction.R;
                        c.direction = Direction.L;
                        cellSet.remove(left);
                    } else if (right != null) {
                        right.direction = Direction.L;
                        c.direction = Direction.R;
                        cellSet.remove(right);
                    } else if (up != null) {
                        up.direction = Direction.D;
                        c.direction = Direction.U;
                        cellSet.remove(up);
                    } else {
                        down.direction = Direction.U;
                        c.direction = Direction.D;
                        cellSet.remove(down);
                    }

                    ++singleNeighbourRemoved;
                    cellSet.remove(c);
                }
            }

            if (singleNeighbourRemoved > 0) continue;

            int cellsJoinedCnt = 0;
            for (Cell c : new ArrayList<>(cellSet)) {
                if (c.direction != null) continue;

                // removing from top-left corner
                Cell up = upNeighbour(matrix, c);
                Cell left = leftNeighbour(matrix, c);
                if ((up == null || !cellSet.contains(up)) && (left == null || !cellSet.contains(left))) {
                    if (preferHorizontalJoin) {
                        Cell right = rightNeighbour(matrix, c);
                        if (right != null && cellSet.contains(right)) {
                            Cell rightUp = upNeighbour(matrix, right);
                            Cell rightDown = downNeighbour(matrix, right);
                            if ((rightUp == null || !cellSet.contains(rightUp)) && (rightDown != null && cellSet.contains(rightDown)) || removeAnyAdjacent) {
                                c.direction = Direction.R;
                                right.direction = Direction.L;
                                ++cellsJoinedCnt;
                                cellSet.remove(c);
                                cellSet.remove(right);
                                removeAnyAdjacent = false;
                                break;
                            }
                        }
                    } else {
                        Cell down = downNeighbour(matrix, c);
                        if (down != null && cellSet.contains(down)) {
                            Cell downLeft = leftNeighbour(matrix, down);
                            Cell downRight = rightNeighbour(matrix, down);
                            if ((downLeft == null || !cellSet.contains(downLeft)) && (downRight != null && cellSet.contains(downRight)) || removeAnyAdjacent) {
                                c.direction = Direction.D;
                                down.direction = Direction.U;
                                ++cellsJoinedCnt;
                                cellSet.remove(c);
                                cellSet.remove(down);
                                removeAnyAdjacent = false;
                                break;
                            }
                        }
                    }
                }

                // removing from bottom-left corner
                Cell down = downNeighbour(matrix, c);
                if ((down == null || !cellSet.contains(down)) && (left == null || !cellSet.contains(left))) {
                    if (preferHorizontalJoin) {
                        Cell right = rightNeighbour(matrix, c);
                        if (right != null && cellSet.contains(right)) {
                            Cell rightUp = upNeighbour(matrix, right);
                            Cell rightDown = downNeighbour(matrix, right);
                            if ((rightDown == null || !cellSet.contains(rightDown)) && (rightUp != null && cellSet.contains(rightUp)) || removeAnyAdjacent) {
                                c.direction = Direction.R;
                                right.direction = Direction.L;
                                ++cellsJoinedCnt;
                                cellSet.remove(c);
                                cellSet.remove(right);
                                removeAnyAdjacent = false;
                                break;
                            }
                        }
                    } else {
                        up = upNeighbour(matrix, c);
                        if (up != null && cellSet.contains(up)) {
                            Cell upLeft = leftNeighbour(matrix, up);
                            Cell upRight = rightNeighbour(matrix, up);
                            if ((upLeft == null || !cellSet.contains(upLeft)) && (upRight != null && cellSet.contains(upRight)) || removeAnyAdjacent) {
                                c.direction = Direction.U;
                                up.direction = Direction.D;
                                ++cellsJoinedCnt;
                                cellSet.remove(c);
                                cellSet.remove(up);
                                removeAnyAdjacent = false;
                                break;
                            }
                        }
                    }
                }

                // removing from top-right corner
                up = upNeighbour(matrix, c);
                Cell right = rightNeighbour(matrix, c);
                if ((up == null || !cellSet.contains(up)) && (right == null || !cellSet.contains(right))) {
                    if (preferHorizontalJoin) {
                        left = leftNeighbour(matrix, c);
                        if (left != null && cellSet.contains(left)) {
                            Cell leftUp = upNeighbour(matrix, left);
                            Cell leftDown = downNeighbour(matrix, left);
                            if ((leftUp == null || !cellSet.contains(leftUp)) && (leftDown != null && cellSet.contains(leftDown)) || removeAnyAdjacent) {
                                c.direction = Direction.L;
                                left.direction = Direction.R;
                                ++cellsJoinedCnt;
                                cellSet.remove(c);
                                cellSet.remove(left);
                                removeAnyAdjacent = false;
                                break;
                            }
                        }
                    } else {
                        down = downNeighbour(matrix, c);
                        if (down != null && cellSet.contains(down)) {
                            Cell downLeft = leftNeighbour(matrix, down);
                            Cell downRight = rightNeighbour(matrix, down);
                            if ((downRight == null || !cellSet.contains(downRight)) && (downLeft != null && cellSet.contains(downLeft)) || removeAnyAdjacent) {
                                c.direction = Direction.D;
                                down.direction = Direction.U;
                                ++cellsJoinedCnt;
                                cellSet.remove(c);
                                cellSet.remove(down);
                                removeAnyAdjacent = false;
                                break;
                            }
                        }
                    }
                }

                // removing from bottom-right corner
                down = downNeighbour(matrix, c);
                if ((down == null || !cellSet.contains(down)) && (right == null || !cellSet.contains(right))) {
                    if (preferHorizontalJoin) {
                        left = rightNeighbour(matrix, c);
                        if (left != null && cellSet.contains(left)) {
                            Cell leftUp = upNeighbour(matrix, left);
                            Cell leftDown = downNeighbour(matrix, left);
                            if ((leftDown == null || !cellSet.contains(leftDown)) && (leftUp != null && cellSet.contains(leftUp)) || removeAnyAdjacent) {
                                c.direction = Direction.L;
                                left.direction = Direction.R;
                                ++cellsJoinedCnt;
                                cellSet.remove(c);
                                cellSet.remove(left);
                                removeAnyAdjacent = false;
                                break;
                            }
                        }
                    } else {
                        up = upNeighbour(matrix, c);
                        if (up != null && cellSet.contains(up)) {
                            Cell upLeft = leftNeighbour(matrix, up);
                            Cell upRight = rightNeighbour(matrix, up);
                            if ((upRight == null || !cellSet.contains(upRight)) && (upLeft != null && cellSet.contains(upLeft))) {
                                c.direction = Direction.U;
                                up.direction = Direction.D;
                                ++cellsJoinedCnt;
                                cellSet.remove(c);
                                cellSet.remove(up);
                                removeAnyAdjacent = false;
                                break;
                            }
                        }
                    }
                }
            }

            if (cellsJoinedCnt == 0) {
                if (!removeAnyAdjacent) {
                    removeAnyAdjacent = true;
                } else {
                    return;
                }
            }
        }
    }

    private static long countCost(Cell[][] matrix, long pu, long pd, long pl, long pr) {
        long result = 0;
        for (Cell[] row : matrix) {
            for (Cell cell : row) {
                if (cell.direction == null) return -1;
                switch (cell.direction) {
                    case D:
                        result += pd;
                        break;
                    case L:
                        result += pl;
                        break;
                    case R:
                        result += pr;
                        break;
                    case U:
                        result += pu;
                        break;
                }
            }
        }

        return result;
    }

    private static boolean sameCycle(Cell c1, Cell c2) {
        return c1.endCycleRow == c2.endCycleRow && c1.endCycleCol == c2.endCycleCol;
    }

    private static class Path implements Comparable<Path> {
        final LinkedHashSet<Cell> visitedCells;
        final List<Direction> directions;
        final Cell lastCell;
        final long totalSum;

        public Path(LinkedHashSet<Cell> visitedCells, List<Direction> directions, Cell lastCell, long totalSum) {
            this.visitedCells = visitedCells;
            this.directions = directions;
            this.lastCell = lastCell;
            this.totalSum = totalSum;
        }

        public Path move(Cell moveTo, Direction d, long cost) {
            LinkedHashSet<Cell> newVisitedCells = new LinkedHashSet<>(this.visitedCells);
            newVisitedCells.add(moveTo);
            List<Direction> newDirections = new ArrayList<>(this.directions);
            newDirections.add(d);
            return new Path(newVisitedCells, newDirections, moveTo, totalSum + cost);
        }

        public static Path forCell(Cell c) {
            LinkedHashSet<Cell> visitedCells = new LinkedHashSet<>();
            visitedCells.add(c);
            return new Path(visitedCells, new ArrayList<>(), c,0);
        }

        public int compareTo(Path p) {
            return compareResults(totalSum, directions.size(), p.totalSum, p.directions.size());
        }
    }

    private static Cell chooseAnyNonVisited(Set<Cell> cellsToJoin) {
        for (Cell c : cellsToJoin) {
            return c;
        }
        return null;
    }

    private static long gcd(long a, long b) {
        return b == 0 ? a : gcd(b, a % b);
    }

    private static long lcm(long a, long b) {
        return a * (b / gcd(a, b));
    }

    private static int compareResults(long sum1, long count1, long sum2, long count2) {
        if (count1 == 0 || count2 == 0) return 0;
        long commonCount = lcm(count1, count2);
        return Long.compare(sum1 * (commonCount) / count1, sum2 * (commonCount) / count2);
    }

    private static void trySmartJoinCellToCycles(Cell[][] matrix, Cell cycleEndpoint, List<Cell> cellsToJoin, long pu, long pd, long pl, long pr) {
        long minCost = Math.min(Math.min(pu, pd), Math.min(pl, pr));
        Set<Cell> cellsToJoinSet = new HashSet<>(cellsToJoin);


        while (!cellsToJoinSet.isEmpty()) {
            PriorityQueue<Path> paths = new PriorityQueue<>();
            Path bestCandidate = null;

            for (Cell startCell : cellsToJoinSet) {
                paths.add(Path.forCell(startCell));
            }

            while (!paths.isEmpty()) {
                Path path = paths.poll();
                if (bestCandidate != null) {
                    if (path.directions.size() > 0) {
                        if (compareResults(
                                bestCandidate.totalSum,
                                bestCandidate.directions.size(),
                                path.totalSum + (cellsToJoinSet.size() - path.directions.size()) * minCost,
                                cellsToJoinSet.size()) <= 0) {
                            continue;
                        }
                    }
                }

                if (path.lastCell.direction != null) {
                    if (bestCandidate == null || path.compareTo(bestCandidate) < 0) {
                        bestCandidate = path;
                    }
                    continue;
                }

                Cell up = upNeighbour(matrix, path.lastCell);
                if (up != null && sameCycle(up, cycleEndpoint) && !path.visitedCells.contains(up)) {
                    paths.add(path.move(up, Direction.U, pu));
                }

                Cell down = downNeighbour(matrix, path.lastCell);
                if (down != null && sameCycle(down, cycleEndpoint) && !path.visitedCells.contains(down)) {
                    paths.add(path.move(down, Direction.D, pd));
                }

                Cell left = leftNeighbour(matrix, path.lastCell);
                if (left != null && sameCycle(left, cycleEndpoint) && !path.visitedCells.contains(left)) {
                    paths.add(path.move(left, Direction.L, pl));
                }

                Cell right = rightNeighbour(matrix, path.lastCell);
                if (right != null && sameCycle(right, cycleEndpoint) && !path.visitedCells.contains(right)) {
                    paths.add(path.move(right, Direction.R, pr));
                }
            }

            if (bestCandidate == null) {
                return;
                // should never happen
            }

            List<Cell> visitedCells = new ArrayList<>(bestCandidate.visitedCells);
            for (int i = 0; i < bestCandidate.directions.size(); ++i) {
                visitedCells.get(i).direction = bestCandidate.directions.get(i);
                cellsToJoinSet.remove(visitedCells.get(i));
            }
        }
    }

    private static void joinCellsToCycles(Cell[][] matrix, List<Cell> cycleEndpoints, long pu, long pd, long pl, long pr) {
        for (Cell cycleEndpoint : cycleEndpoints) {

            Set<Cell> visited = new HashSet<>();
            for (Cell[] row : matrix) {
                for (Cell c : row) {
                    if (visited.contains(c) || !sameCycle(cycleEndpoint, c) || c.direction != null) {
                        visited.add(c);
                        continue;
                    }

                    Set<Cell> cellsToJoin = new HashSet<>();

                    List<Cell> currentCells = new ArrayList<>();
                    currentCells.add(c);
                    cellsToJoin.add(c);
                    visited.add(c);

                    boolean targetMet = false;
                    while (!currentCells.isEmpty()) {
                        Set<Cell> newCells = new HashSet<>();
                        for (Cell currentCell : currentCells) {
                            List<Cell> neighbours = new ArrayList<>();

                            Cell up = upNeighbour(matrix, currentCell);
                            if (up != null && sameCycle(up, cycleEndpoint)) neighbours.add(up);
                            Cell down = downNeighbour(matrix, currentCell);
                            if (down != null && sameCycle(down, cycleEndpoint)) neighbours.add(down);
                            Cell left = leftNeighbour(matrix, currentCell);
                            if (left != null && sameCycle(left, cycleEndpoint)) neighbours.add(left);
                            Cell right = rightNeighbour(matrix, currentCell);
                            if (right != null && sameCycle(right, cycleEndpoint)) neighbours.add(right);

                            for (Cell neighbour : neighbours) {
                                if (neighbour.direction == null && !visited.contains(neighbour)) {
                                    newCells.add(neighbour);
                                }
                                if (neighbour == cycleEndpoint) targetMet = true;
                                visited.add(neighbour);
                            }
                        }
                        cellsToJoin.addAll(newCells);
                        currentCells = new ArrayList<>(newCells);
                    }

                    if (!targetMet) return;

                    if (!cellsToJoin.isEmpty()) {
                        trySmartJoinCellToCycles(matrix, cycleEndpoint, new ArrayList<>(cellsToJoin), pu, pd, pl, pr);
                    }
                }
            }


        }
    }

    static long solve(Cell[][] matrix, long pu, long pd, long pl, long pr) {
        List<Cell> cycledToItselfCells = findCycledToItselfCells(matrix);
        joinSelfCycled(matrix, cycledToItselfCells, pu, pd, pl, pr);

        for (Cell c : cycledToItselfCells) {
            if (c.direction == null) return -1;
        }

        joinCellsToCycles(matrix, cycledToItselfCells, pu, pd, pl, pr);

        return countCost(matrix, pu, pd, pl, pr);
    }

    public static void main(String[] params) throws IOException {
        InputReader in = new InputReader(System.in);

        int n = in.readInt();
        int pu = in.readInt();
        int pl = in.readInt();
        int pd = in.readInt();
        int pr = in.readInt();

        Cell[][] matrix = new Cell[n][n];
        for (int i = 0; i < n; ++i) {
            int[] endpoints = new int[2 * n];
            in.readIntArray(endpoints, 0, endpoints.length);
            for (int j = 0; j < n; j++) {
                matrix[i][j] = new Cell(i, j, endpoints[j * 2] - 1, endpoints[j * 2 + 1] - 1);
            }
        }

        long cost = solve(matrix, pu, pd, pl, pr);

        if (cost > 0) {
            printResult(matrix, cost);
        } else {
            System.out.println("-1");
        }
    }

    private static void printResult(Cell[][] m, long cost) {
        System.out.println(cost);
        for (int i = 0; i < m.length; ++i) {
            for (int j = 0; j < m.length; ++j) {
                System.out.print(m[i][j].direction);
            }
            System.out.println();
        }
    }

    static class InputReader {
        static final int bufferSize = 1 << 25;

        private char[] content;
        private int pos = 0;
        private int[] readTo = new int[1];

        public InputReader(InputStream in) throws IOException {
            Reader charReader = new InputStreamReader(in);
            content = new char[bufferSize];
            charReader.read(content, 0, content.length);
            charReader.close();
        }

        public int readInt() {
            readIntArray(readTo, 0, 1);
            return readTo[0];
        }

        public void readIntArray(int[] arr, int from, int length) {
            for (int i = from, k = 0; k < length; ++k, ++i) {
                while (pos < content.length && content[pos] < '0' && content[pos] != '+' && content[pos] != '-') ++pos;

                int num = 0;
                int sign = 1;
                if (content[pos] == '-') {
                    sign = -1;
                    ++pos;
                } else if (content[pos] == '+') ++pos;

                while (pos < content.length) {
                    char ch = content[pos++];
                    if (ch < '0') {
                        break;
                    } else {
                        int digit = ch - '0';
                        num = (num << 3) + (num << 1) + digit;
                    }
                }
                arr[from++] = num * sign;
            }
        }
    }
}