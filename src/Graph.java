// A Java program to print topological
// sorting of a DAG
// источник: https://www.geeksforgeeks.org/topological-sorting/
import java.util.*;

// This class represents a directed graph
// using adjacency_matrixacency list representation
class Graph {
    private final int number_of_vertices;  // число вершин в графе
    private final ArrayList<ArrayList<Integer>> adjacency_matrix;  // УДОБНАЯ матрица смежности

    Graph(int vertices_count) {
        number_of_vertices = vertices_count;
        // инициализация матрицы смежности
        adjacency_matrix = new ArrayList<ArrayList<Integer> >(vertices_count);
        for (int i = 0; i < vertices_count; ++i) {
            adjacency_matrix.add(new ArrayList<Integer>(vertices_count));
        }
    }

    /**
     * Функция добавляет однонаправленное ребро (v, m) в граф
     * @param v - индекс откуда
     * @param w - индекс куда
     */
    public void addEdge(int v, int w) {
        adjacency_matrix.get(v).add(w);
    }

    // source: https://takeuforward.org/data-structure/detect-a-cycle-in-directed-graph-topological-sort-kahns-algorithm-g-23/
    /**
     * Функция возвращает true, если в графе есть цикл, false в противном случае
     * @return - true/false
     */
    public boolean isCyclic() {
        int[] inDegree = new int[number_of_vertices];
        for (int i = 0; i < number_of_vertices; i++) {
            for (Integer it : adjacency_matrix.get(i)) {
                inDegree[it]++;
            }
        }

        Queue<Integer> q = new LinkedList<Integer>();
        for (int i = 0; i < number_of_vertices; i++) {
            if (inDegree[i] == 0) {
                q.add(i);
            }
        }
        int cnt = 0;
        while (!q.isEmpty()) {
            Integer node = q.poll();
            cnt++;
            for (Integer it : adjacency_matrix.get(node)) {
                inDegree[it]--;
                if (inDegree[it] == 0) {
                    q.add(it);
                }
            }
        }
        return !(cnt == number_of_vertices);
    }

    /**
     * Рекурсивная реализация топологической сортировки
     * @param v - текущая вершина
     * @param visited - массив посещенных вершин
     * @param stack - стек значений вершин, нужен для рекурсии
     */
    private void topologicalSortUtil(int v, boolean[] visited, Stack<Integer> stack) {
        visited[v] = true;  // теперь текущая вершина считается посещенной
        Integer i;

        // рекурсивно итерируемся по всем вершинам, к которым есть путь из v
        for (Integer integer : adjacency_matrix.get(v)) {
            i = integer;
            if (!visited[i])
                topologicalSortUtil(i, visited, stack);
        }

        // добавляем текущую вершину в стек, который хранит результат
        stack.push(v);
    }

    /**
     * Главная функция топологической сортировки, она использует
     * рекурсивную функцию выше
     * @param vec - вектор вершин
     */
    public void topologicalSort(Vector<Integer> vec) {
        Stack<Integer> stack = new Stack<Integer>();

        // Mark all the vertices as not visited
        // помечаем все вершины как неотмеченные
        boolean[] visited = new boolean[number_of_vertices];
        for (int i = 0; i < number_of_vertices; i++)
            visited[i] = false;

        for (int i = 0; i < number_of_vertices; i++)
            if (!visited[i])
                topologicalSortUtil(i, visited, stack);

        // печатаем стек
        while (!stack.empty()) {
            int elem = stack.pop();
            vec.addElement(elem);
        }
        Collections.reverse(vec);
    }
}
