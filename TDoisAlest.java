import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

public class TDoisAlest {
    private static char[][] graph;
    private static int rows;
    private static int cols;
    private static int totalDistance = 0;
    private static HashMap<Integer, int[]> positions = new HashMap<>();

    public static void main(String[] args) {
        In arq = new In("data.txt");
        rows = Integer.parseInt(arq.readString());
        cols = Integer.parseInt(arq.readString());
        graph = new char[rows][cols];

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

        for (int i = 0; i < rows; i++) {
            char[] rowz = row[i].toCharArray();
            for (int j = 0; j < cols; j++) {
                graph[i][j] = rowz[j];
                if (Character.isDigit(graph[i][j])) {
                    positions.put((Integer.parseInt("" + graph[i][j])), new int[] { i, j });
                }
            }
        }

        for (int i = 1; i < 10; i++) {
            int[] startPosition = positions.get(i);
            if (i < 9) {
                int destValue = i + 1;
                int[] desPosition = positions.get(destValue);
                int distance = bfs(startPosition[0], startPosition[1], desPosition[0], desPosition[1]);
                if (distance != -1) {
                    System.out.printf("%d to %d: %d\n", i, destValue, distance);
                }
                if (distance == -1 && ++destValue < 10) {
                    while (destValue < 10 && distance == -1) {
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
            } else { // TODO
                startPosition = positions.get(9);
                int[] desPosition = positions.get(1);
                int distance = bfs(startPosition[0], startPosition[1], desPosition[0], desPosition[1]);
                if (distance != -1) {
                    System.out.printf("%d to %d: %d\n", 9, 1, distance); // Distance 9 to 1
                    totalDistance += distance;

                }
            }

        }

        System.out.println("Total distance: " + totalDistance);

    }

    private static int bfs(int startRow, int startCol, int destRow, int destCol) {
        Set<Character> set = new HashSet<>(Arrays.asList('1', '2', '3', '4', '5', '6', '7', '8', '9'));
        set.remove(graph[startRow][startCol]);
        set.remove(graph[destRow][destCol]);

        // Initialize the BFS queue with the starting point
        LinkedList<int[]> queue = new LinkedList<>();
        queue.add(new int[] { startRow, startCol });
        boolean[][] visited = new boolean[rows][cols];
        visited[startRow][startCol] = true;
        final int[][] DIRECTIONS = new int[][] { { 1, 0 }, { -1, 0 }, { 0, 1 }, { 0, -1 } };
        // Initialize the distance array
        int[][] distance = new int[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                distance[i][j] = Integer.MAX_VALUE;
            }
        }
        distance[startRow][startCol] = 0;

        // Perform the BFS
        while (!queue.isEmpty()) {
            int[] curr = queue.removeFirst();
            int currRow = curr[0];
            int currCol = curr[1];

            // Check if we've reached the destination
            if (currRow == destRow && currCol == destCol) {
                return distance[currRow][currCol];
            }

            // Visit each adjacent point
            for (int[] dir : DIRECTIONS) {
                int nextRow = currRow + dir[0];
                int nextCol = currCol + dir[1];

                // Check if the next point is a valid point to visit
                if (nextRow >= 0 && nextRow < rows && nextCol >= 0 && nextCol < cols
                        && graph[nextRow][nextCol] != '*' && !visited[nextRow][nextCol]
                        && !set.contains(graph[nextRow][nextCol])) {
                    // Mark the next point as visited
                    visited[nextRow][nextCol] = true;

                    // Update the distance to the next point
                    distance[nextRow][nextCol] = distance[currRow][currCol] + 1;

                    // Add the next point to the BFS queue
                    queue.add(new int[] { nextRow, nextCol });
                }
            }
        }

        // If we reach this point, there is no path from the start node to the
        // destination node
        return -1;
    }

}