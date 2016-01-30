package grafos;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;

public class BuscaEmProfundidade {

    int nmComponentesConexas = 0;
    int tempo = 0;
    
    boolean biPartido = true;

    Graph<Object, DefaultEdge> g;
    List<DefaultEdge> arvoreProfundidade;
    List<DefaultEdge> arestaDeRetorno;
    List<Integer> profundidadeSaida;
    List<Stack<DefaultEdge>> blocos;
    List<Vertice> articulacoes;
    List<DefaultEdge> pontes;
    
    Stack<DefaultEdge> tempBloco;

    public BuscaEmProfundidade(Graph<Object, DefaultEdge> g) {
        this.g = g;
    }

    public void bep(Graph<Object, DefaultEdge> g) {
        while (temPeZero(g)) {
            nmComponentesConexas++;
            busca(escolheRaiz());
            profundidadeSaida.add(maiorPS());
        }
        
    }

    public void busca(Vertice v) {
        tempo = tempo + 1;
        v.setpE(tempo);
        v.setBack(v.getpE());

        for (Vertice w : filhos(v)) {
            if (w.getpE() == 0) {
                arvoreProfundidade.add(getAresta(v, w));
                tempBloco.add(getAresta(v, w));
                w.setPai(v);
                busca(w);
                if(w.getBack() >= v.getpE()){
                    articulacoes.add(v);
                    blocos.add(tempBloco);
                    if(tempBloco.size() == 1){
                        pontes.add(getAresta(v, w));
                    }
                    tempBloco.removeAllElements();
                }
                v.setBack(Math.min(v.getBack(), w.getBack()));
            } else if (w.getpS() == 0 && (!v.getPai().equals(w))) {
                arestaDeRetorno.add(getAresta(v, w));
                tempBloco.add(getAresta(v, w));
                v.setBack(Math.min(v.getBack(), w.getpE()));
                cicloImpar(v, w);
            }
        }
        tempo = tempo + 1;
        v.setpS(tempo);
    }

    public boolean temPeZero(Graph<Object, DefaultEdge> g) {
        for (Object v : g.vertexSet()) {
            Vertice t = (Vertice) v;
            if (t.getpE() == 0) {
                return true;
            }
        }
        return false;
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

    private List<Vertice> filhos(Vertice v) {

        List<Vertice> list = new ArrayList<>();
        Set<DefaultEdge> arestas = g.edgesOf(v);

        for (DefaultEdge aresta : arestas) {
            if (g.getEdgeSource(aresta).equals(v)) {
                list.add((Vertice) g.getEdgeTarget(aresta));
            } else if (g.getEdgeTarget(aresta).equals(v)) {
                list.add((Vertice) g.getEdgeSource(aresta));
            }
        }
        return list;
    }

    private DefaultEdge getAresta(Vertice v, Vertice w) {
        DefaultEdge aresta = null;
        aresta = g.getEdge(v, w);
        if (aresta == null) {
            aresta = g.getEdge(w, v);
        }
        return aresta;
    }

    private void cicloImpar(Vertice v, Vertice w) {
        int count = 1;
        Vertice temp = v;
        while(!temp.getPai().equals(w)){
            count++;
        }
        if(count%2 != 0){
            biPartido = false;
        }
    }

    private Integer maiorPS() {
        int maior = 0;
        for (Object v : g.vertexSet()) {
            Vertice t = (Vertice) v;
            if (t.getpS() > maior) {
                maior = t.getpS();
            }
        }
        return maior;
    }

}
