
import java.util.Arrays;
import java.util.stream.IntStream;


public class Monastery {

    /*
    3 2 6 3 6
1 8 4 1 4
13 7 13 9 4
3 0 2 6 5
9 8 8 12 13
     */



    /**
     * 返回修道院内部结构关键信息以及最佳修缮策略
     *
     * @param rooms 房间布局图，值为上下左右四个方向是否有墙的映射值之和
     * @return 修道院内部结构关键信息，累计包含6个元素，具体参见题目说明
     */
    public int[] resolve(int[][] rooms) {
        ROWS = rooms.length;
        COLS = rooms[0].length;
        WeightedQuickUnionUF uf = monasteryUnionFind(rooms);

        MAX = ROW = COL = 0;
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                int valueOfRoom = rooms[i][j];
                for (Direction dir : Direction.TO_REMOVE) {
                    if ((valueOfRoom & dir.dirNum) == dir.dirNum) {
                        uf.simulateUnion(i, j, dir);
                    }
                }

            }

        }

        int[] result = new int[]{
                uf.count,
                IntStream.of(uf.size).max().orElse(1),
                MAX,
                ROW, COL, DIR.dirNum
        };
        System.out.println(Arrays.toString(result));
        System.out.println(Arrays.toString(uf.size));

        return result;
    }

    private WeightedQuickUnionUF monasteryUnionFind(int[][] rooms) {
        WeightedQuickUnionUF uf = new WeightedQuickUnionUF(ROWS * COLS);

        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                int valusOfRoom = rooms[i][j];
                for (Direction dir :
                        Direction.values()) {
                    if ((valusOfRoom & dir.dirNum) == 0) {
                        uf.union(i, j, dir);
                    }
                }
            }
        }
        return uf;
    }

    static int ROWS, COLS, MAX, ROW, COL;
    static Direction DIR = Direction.North;

    enum Direction {
        West(1), North(2), East(4), South(8);

        private static Direction[] TO_REMOVE = {North, East};

        private int dirNum;

        Direction(int dirNum) {
            this.dirNum = dirNum;
        }

        int convertToOneDimenIndex(int row, int col) {
            if (this == West && col > 0) return ROWS * row + col - 1;
            else if (this == North && row > 0) return ROWS * (row - 1) + col;
            else if (this == East && col < COLS - 1) return ROWS * row + col + 1;
            else if (this == South && row < ROWS - 1) return ROWS * (row + 1) + col;
            return -1;
        }

    }



    public static void main(String[] args) {
        Monastery mo = new Monastery();
        int[][] rooms = new int[][] {
                {3,  2,  6,  3,  6},
                {1,  8,  4,  1,  4},
                {13, 7,  13, 9,  4},
                {3,  0,  2,  6,  5},
                {9,  8,  8,  12, 13}};
        mo.resolve(rooms);
    }


    private static class WeightedQuickUnionUF {
        private int[] parent;
        private int[] size;
        private int count;

        WeightedQuickUnionUF(int n) {
            count = n;
            parent = new int[n];
            size = new int[n];
            for (int i = 0; i < n; i++) {
                parent[i] = i;
                size[i] = 1;
            }
        }

        int find(int p) {
            while (p != parent[p])
                p = parent[p];
            return p;
        }

        void union(int p, int q) {
            int rootP = find(p);
            int rootQ = find(q);
            if (rootP == rootQ) return;

            if (size[rootP] < size[rootQ]) {
                parent[rootP] = rootQ;
                size[rootQ] += size[rootP];
            } else {
                parent[rootQ] = rootP;
                size[rootP] += size[rootQ];
            }
            count--;
        }

        void union(int r, int c, Direction dir) {
            int p = r * COLS + c;
            int q = dir.convertToOneDimenIndex(r, c);
            if (q < 0 || q >= ROWS * COLS) return;
            this.union(p, q);
        }

        void simulateUnion(int r, int c, Direction dir) {
            int p = r * COLS + c;
            int q = dir.convertToOneDimenIndex(r, c);
            if (q < 0 || q >= ROWS * COLS) return;

            int rootP = find(p);
            int rootQ = find(q);
            if (rootP == rootQ) return;

            if (size[rootP] + size[rootQ] > MAX) {
                MAX = size[rootP] + size[rootQ];
                ROW = r + 1;
                COL = c + 1;
                DIR = dir;
            }
        }
    }

}
