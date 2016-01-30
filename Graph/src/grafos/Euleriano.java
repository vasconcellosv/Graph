package grafos;

import java.util.Set;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;

public class Euleriano {

    Graph<Object, DefaultEdge> g;

    public Euleriano(Graph<Object, DefaultEdge> g) {
        this.g = g;
    }

    public boolean ehEuleriano() {

        Set<DefaultEdge> arestas;
        int contaGrau = 0;
        boolean euleriano = true;

        for (Object v : g.vertexSet()) {
            Vertice t = (Vertice) v;
            arestas = g.edgesOf(t);
            for (DefaultEdge aresta : arestas) {
                contaGrau++;
            }
            if (contaGrau % 2 != 0) {
                euleriano = false;
            }
            contaGrau = 0;
        }
        return euleriano;
    }

}
