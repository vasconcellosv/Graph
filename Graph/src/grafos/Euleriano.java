package grafos;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;

public class Euleriano {

    Graph<Object, DefaultEdge> g;

    public Euleriano(Graph<Object, DefaultEdge> g) {
        this.g = g;
    }

    public boolean ehEuleriano() {

        List<Vertice> verticesImpares = new ArrayList<Vertice>();
        Set<DefaultEdge> arestas = null;
        int contaGrau = 0;
        boolean euleriano = true;

        for (Object v : g.vertexSet()) {
            Vertice t = (Vertice) v;
            arestas = g.edgesOf(t);
            for (DefaultEdge aresta : arestas) {
                contaGrau+=1;
            }
            if (contaGrau % 2 != 0 && contaGrau != 0) {
                verticesImpares.add(t);
            }
            contaGrau = 0;
        }
        if(verticesImpares.size() != 2 && !verticesImpares.isEmpty()){
            euleriano = false;
        }else{
            if(verticesImpares.size() == 2 && g.getEdge(verticesImpares.get(0), verticesImpares.get(1)) == null){
                euleriano = false;
            }
        }
            
        return euleriano;
    }
    
    public Vertice escolheRaiz() {
        for (Object v : g.vertexSet()) {
            Vertice t = (Vertice) v;
            if (t.getpE() == 0) {
                return t;
            }
        }
        return null;
    }
}
