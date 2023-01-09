// A Java program to print topological
// sorting of a DAG
// источник: https://www.geeksforgeeks.org/topological-sorting/
import java.io.*;
import java.util.*;

// This class represents a directed graph
// using adjacency list representation
class Graph {
    private int V;  // число вершин в графе

    // УДОБНАЯ матрица смежности
    private ArrayList<ArrayList<Integer> > adj;

    Graph(int v) {
        V = v;
        // инициализация матрицы смежности
        adj = new ArrayList<ArrayList<Integer> >(v);
        for (int i = 0; i < v; ++i)
            adj.add(new ArrayList<Integer>(v));
    }

    // добавление однонаправленного ребра в граф
    void addEdge(int v, int w) {
        adj.get(v).add(w);
    }

    // FOR DEBUG ONLY
    void printAdjMatrix() {
        System.out.println(adj.get(0).size());
        for (int i = 0; i < V; ++i) {
            for (int j = 0; j < adj.get(i).size(); ++j) {
                System.out.print(adj.get(i).get(j) + " ");
            }
            System.out.println();
        }
    }

    // source: https://takeuforward.org/data-structure/detect-a-cycle-in-directed-graph-topological-sort-kahns-algorithm-g-23/
    // функция возвращает true, если в графе есть цикл, false в противном случае
    public boolean isCyclic() {
        int inDegree[] = new int[V];
        for (int i = 0; i < V; i++) {
            for (Integer it : adj.get(i)) {
                inDegree[it]++;
            }
        }

        Queue<Integer> q = new LinkedList<Integer>();
        for (int i = 0; i < V; i++) {
            if (inDegree[i] == 0) {
                q.add(i);
            }
        }
        int cnt = 0;
        while (!q.isEmpty()) {
            Integer node = q.poll();
            cnt++;
            for (Integer it : adj.get(node)) {
                inDegree[it]--;
                if (inDegree[it] == 0) {
                    q.add(it);
                }
            }
        }
        if (cnt == V) {
            return false;
        }
        return true;
    }

    // A recursive function used by topologicalSort
    // рекурсивная реализация топологической сортировки
    void topologicalSortUtil(int v, boolean visited[], Stack<Integer> stack) {
        visited[v] = true;  // теперь текущая вершина считается посещенной
        Integer i;

        // рекурсивно итерируемся по всем вершинам, к которым есть путь из v
        Iterator<Integer> it = adj.get(v).iterator();
        while (it.hasNext()) {
            i = it.next();
            if (!visited[i])
                topologicalSortUtil(i, visited, stack);
        }

        // добавляем текущую вершину в стек, который хранит результат
        stack.push(Integer.valueOf(v));
    }

    // The function to do Topological Sort.
    // It uses recursive topologicalSortUtil()
    // главная функция топологической сортировки, она использует
    // рекурсивную функцию выше
    void topologicalSort(Vector vec) {
        Stack<Integer> stack = new Stack<Integer>();

        // Mark all the vertices as not visited
        // помечаем все вершины как неотмеченные
        boolean visited[] = new boolean[V];
        for (int i = 0; i < V; i++)
            visited[i] = false;

        // Call the recursive helper
        // function to store
        // Topological Sort starting
        // from all vertices one by one?
        for (int i = 0; i < V; i++)
            if (visited[i] == false)
                topologicalSortUtil(i, visited, stack);

        // печатаем стек
        while (stack.empty() == false) {
            int elem = stack.pop();
            vec.addElement(elem);
//            System.out.print(elem + " ");
        }
        Collections.reverse(vec);
//        System.out.println();
//        for (int i = vec.size()-1; i >= 0; --i) {
//            System.out.print(vec.get(i) + " ");
//        }
    }

    // Driver code
    public static void main(String args[])
    {
        // Create a graph given in the above diagram
        Graph g = new Graph(6);
        g.addEdge(5, 2);
        g.addEdge(4, 0);
        g.addEdge(4, 1);
        g.addEdge(2, 3);
        g.addEdge(3, 1);

        g.printAdjMatrix();
        if (g.isCyclic()) {
            System.out.println("Cyclic");
        } else {
            System.out.println("NOT Cyclic");
        }


        System.out.println("Following is a Topological "
                + "sort of the given graph");
        // Function Call
        Vector vec = new Vector();
        g.topologicalSort(vec);
    }
}
// This code is contributed by Aakash Hasija
