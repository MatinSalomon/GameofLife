import java.util.Random;

public class GameOfLife {
    public static void main(String[] args) {
        Integer width = null, height = null, generations = null, speed = 300;
        String pattern = null;
        int neighborhood = 3;

        for (String arg : args) {
            String[] param = arg.split("=");
            switch (param[0]) {
                case "w": width = Integer.parseInt(param[1]); break;
                case "h": height = Integer.parseInt(param[1]); break;
                case "g": generations = Integer.parseInt(param[1]); break;
                case "s": speed = Integer.parseInt(param[1]); break;
                case "p": pattern = param[1]; break;
                case "n": neighborhood = Integer.parseInt(param[1]); break;
                default: System.out.println("Parámetro desconocido: " + param[0]);
            }
        }

        if (width == null || height == null || generations == null || pattern == null) {
            System.out.println("Error: Debes proporcionar todos los parámetros requeridos.");
            return;
        }

        Grid grid = new Grid(width, height, pattern, neighborhood);
        Renderer renderer = new Renderer();

        for (int i = 0; i < generations || generations == 0; i++) {
            renderer.render(grid);
            grid.nextGeneration();
            try {
                Thread.sleep(speed);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

class Grid {
    private int width, height;
    private int[][] cells;
    private int neighborhood;

    public Grid(int width, int height, String pattern, int neighborhood) {
        this.width = width;
        this.height = height;
        this.neighborhood = neighborhood;
        this.cells = new int[height][width];
        initializeGrid(pattern);
    }

    private void initializeGrid(String pattern) {
        if (pattern.equals("rnd")) {
            Random rand = new Random();
            for (int i = 0; i < height; i++)
                for (int j = 0; j < width; j++)
                    cells[i][j] = rand.nextInt(2);
        } else {
            String[] rows = pattern.split("#");
            for (int i = 0; i < rows.length && i < height; i++) {
                for (int j = 0; j < rows[i].length() && j < width; j++) {
                    cells[i][j] = rows[i].charAt(j) == '1' ? 1 : 0;
                }
            }
        }
    }

    public void nextGeneration() {
        int[][] newCells = new int[height][width];
        System.out.println("Nueva generacion");
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int neighbors = countNeighbors(i, j);
                if (cells[i][j] == 1) {
                    newCells[i][j] = (neighbors == 2 || neighbors == 3) ? 1 : 0;
                } else {
                    newCells[i][j] = (neighbors == 3) ? 1 : 0;
                }
            }
        }
        cells = newCells;
    }

    private int countNeighbors(int row, int col) {
        int count = 0;

        switch (neighborhood) {
            case 2:
                int[] dxVN = {-1, 1, 0, 0};
                int[] dyVN = {0, 0, -1, 1};
                for (int k = 0; k < 4; k++) {
                    int newRow = row + dxVN[k];
                    int newCol = col + dyVN[k];
                    if (newRow >= 0 && newRow < height && newCol >= 0 && newCol < width) {
                        count += cells[newRow][newCol];
                    }
                }
                break;

            case 3:
                int[] dxM = {-1, -1, -1, 0, 0, 1, 1, 1};
                int[] dyM = {-1, 0, 1, -1, 1, -1, 0, 1};
                for (int k = 0; k < 8; k++) {
                    int newRow = row + dxM[k];
                    int newCol = col + dyM[k];
                    if (newRow >= 0 && newRow < height && newCol >= 0 && newCol < width) {
                        count += cells[newRow][newCol];
                    }
                }
                break;

            default:
                int[] dxDefault = {-1, -1, -1, 0, 0, 1, 1, 1};
                int[] dyDefault = {-1, 0, 1, -1, 1, -1, 0, 1};
                for (int k = 0; k < 8; k++) {
                    int newRow = row + dxDefault[k];
                    int newCol = col + dyDefault[k];
                    if (newRow >= 0 && newRow < height && newCol >= 0 && newCol < width) {
                        count += cells[newRow][newCol];
                    }
                }
                break;
        }

        return count;
    }
    public int[][] getCells() {
        return cells;
    }
}

class Renderer {
    public void render(Grid grid) {
        for (int[] row : grid.getCells()) {
            for (int cell : row) {
                System.out.print(cell == 1 ? "X " : ". ");
            }
            System.out.println();
        }
        System.out.println();
    }
}
