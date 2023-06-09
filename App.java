import java.util.*;

/**
 * Autores: Pedro Tigre e Rodrigo Renck
 */
public class App {

    private Scanner sc = new Scanner(System.in);
    private static int rows;
    private static int cols;
    private static char[][] graph;
    private static int totalDistance = 0;
    private static final HashMap<Integer, int[]> positions = new HashMap<>(); //guarda a posicao x,y de cada porto


    public void run() {
        System.out.print("Informe o nome do arquivo com extensao: ");
        var fileName = sc.nextLine();
        var in = readFile(fileName);
        var row = populateGraph(in);
        savePortsCoordinates(row);
        calculateDistance();
    }

    private In readFile(String fileName) {
        In arq = new In(fileName);
        rows = Integer.parseInt(arq.readString());
        cols = Integer.parseInt(arq.readString());
        graph = new char[rows][cols];

        return arq;
    }


    private String[] populateGraph(In arq) {
        String[] row = new String[rows];
        for (int i = 0; i < rows; i++) {
            row[i] = arq.readString();
        }
        for (int i = 0; i < rows; i++) {
            char[] rowz = row[i].toCharArray();
            for (int j = 0; j < cols; j++) {
                graph[i][j] = rowz[j];
            }
        }
        return row;
    }

    private void savePortsCoordinates(String[] row) {
        for (int i = 0; i < rows; i++) {
            char[] rowz = row[i].toCharArray();
            for (int j = 0; j < cols; j++) {
                graph[i][j] = rowz[j];
                if (Character.isDigit(graph[i][j])) {
                    positions.put((Integer.parseInt("" + graph[i][j])), new int[]{i, j});
                }
            }
        }
    }

    private void calculateDistance() {
        for (int i = 1; i < 9; i++) {
            int[] startPosition = positions.get(i);
            int destValue = i + 1;
            int[] desPosition = positions.get(destValue);
            int distance = bfs(startPosition[0], startPosition[1], desPosition[0], desPosition[1]);
            if (distance != -1) {
                System.out.printf("%d to %d: %d\n", i, destValue, distance);
            } else {
                destValue++;
                while (destValue < 10) {
                    desPosition = positions.get(destValue);
                    distance = bfs(startPosition[0], startPosition[1], desPosition[0], desPosition[1]);
                    if (distance == -1) {
                        destValue++;
                    } else {
                        System.out.printf("%d to %d: %d\n", i, destValue, distance);
                        i = destValue - 1;
                        break;
                    }
                }
            }
            totalDistance += distance;
            if (destValue >= 10) {
                desPosition = positions.get(1);
                distance = bfs(startPosition[0], startPosition[1], desPosition[0], desPosition[1]);
                System.out.printf("%d to %d: %d\n", i, 1, distance);
                totalDistance += distance + 1;
                break;
            }
            if (destValue == 9) {
                startPosition = positions.get(9);
                desPosition = positions.get(1);
                distance = bfs(startPosition[0], startPosition[1], desPosition[0], desPosition[1]);
                System.out.printf("%d to %d: %d\n", 9, 1, distance);
                totalDistance += distance;
            }
        }
        System.out.println("Total distance: " + totalDistance);
    }


    //retorna a distancia ou retorna -1 caso não exista caminho possivel
    private static int bfs(int startRow, int startCol, int destRow, int destCol) {
        Set<Character> set = new HashSet<>(Arrays.asList('1', '2', '3', '4', '5', '6', '7', '8', '9'));
        set.remove(graph[startRow][startCol]);
        set.remove(graph[destRow][destCol]);

        LinkedList<int[]> queue = new LinkedList<>();
        queue.add(new int[]{startRow, startCol});
        boolean[][] visited = new boolean[rows][cols];
        visited[startRow][startCol] = true;
        final int[][] DIRECTIONS = new int[][]{{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
        int[][] distance = new int[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                distance[i][j] = Integer.MAX_VALUE;
            }
        }
        distance[startRow][startCol] = 0;

        while (!queue.isEmpty()) {
            int[] curr = queue.removeFirst();
            int currRow = curr[0];
            int currCol = curr[1];

            if (currRow == destRow && currCol == destCol) {
                return distance[currRow][currCol];
            }

            for (int[] dir : DIRECTIONS) {
                int nextRow = currRow + dir[0];
                int nextCol = currCol + dir[1];

                if (nextRow >= 0 && nextRow < rows && nextCol >= 0 && nextCol < cols
                        && graph[nextRow][nextCol] != '*' && !visited[nextRow][nextCol]
                        && !set.contains(graph[nextRow][nextCol])) {
                    visited[nextRow][nextCol] = true;
                    distance[nextRow][nextCol] = distance[currRow][currCol] + 1;
                    queue.add(new int[]{nextRow, nextCol});
                }
            }
        }
        return -1;
    }
}
