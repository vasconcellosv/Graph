package grafos;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;

public class BuscaEmProfundidade {

    int nmComponentesConexas = 0;
    int tempo = 0;

    boolean biPartido = true;

    Graph<Object, DefaultEdge> g;
    List<DefaultEdge> arvoreProfundidade = new ArrayList<DefaultEdge>();
    List<DefaultEdge> arestaDeRetorno = new ArrayList<DefaultEdge>();
    List<Integer> profundidadeSaida = new ArrayList<Integer>();
    List<Stack<DefaultEdge>> blocos = new ArrayList<Stack<DefaultEdge>>();
    List<Vertice> articulacoes = new ArrayList<Vertice>();
    List<DefaultEdge> pontes = new ArrayList<DefaultEdge>();
    List<List<Vertice>> componentesConexas = new ArrayList<List<Vertice>>();

    Stack<DefaultEdge> tempBloco = new Stack<DefaultEdge>();

    public BuscaEmProfundidade(Graph<Object, DefaultEdge> g) {
        this.g = g;
    }

    public void bep() {
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
                w.setPai(v);
                busca(w);
                if (w.getBack() >= v.getpE()) {
                    if(!articulacoes.contains(v)){
                        articulacoes.add(v);
                    }
                    tempBloco.add(getAresta(v, w));
                    blocos.add(tempBloco);
                    if (tempBloco.size() == 1) {
                        pontes.add(getAresta(v, w));
                    }
                    tempBloco = new Stack<DefaultEdge>();
                }else{
                    tempBloco.add(getAresta(v, w));
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

    public void componentes() {
        List<Vertice> tempComponentes = null;
        if (profundidadeSaida.size() == 1) {
            Vertice temp = null;
            for (Object v : g.vertexSet()) {
                temp = (Vertice) v;
                tempComponentes.add(temp);
            }
        } else {
            int i = 0;
            Vertice temp = null;
            for (Object v : g.vertexSet()) {
                temp = (Vertice) v;
                if (temp.getpE() < profundidadeSaida.get(i)) {
                    tempComponentes.add(temp);
                }
            }
            componentesConexas.add(tempComponentes);
            tempComponentes.clear();

            while (i < profundidadeSaida.size()) {
                for (Object v : g.vertexSet()) {
                    temp = (Vertice) v;
                    if (profundidadeSaida.get(i) < temp.getpE() && temp.getpE() < profundidadeSaida.get(i + 1)) {
                        tempComponentes.add(temp);
                    }
                }
                componentesConexas.add(tempComponentes);
                tempComponentes.clear();
                i++;
            }
        }
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
            //if (t.getpE() == 0) {
            //    return t;
            //}
            if(t.getId() == 4){
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
        aresta = (DefaultEdge) g.getEdge(v, w);
        return aresta;
    }

    private void cicloImpar(Vertice v, Vertice w) {
        int count = 1;
        Vertice temp = v;
        while (!temp.getPai().equals(w)) {
            count++;
            temp = temp.getPai();
        }
        if (count % 2 != 0) {
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

    private List<DefaultEdge> circuitoEuleriano() {

        List<DefaultEdge> circuito = null;
        List<DefaultEdge> visitadas = null;
        Queue<DefaultEdge> arestas = null;

        Euleriano euleriano = new Euleriano(g);
        if (euleriano.ehEuleriano()) {

            Vertice v = escolheRaiz();

            while (!circuito.containsAll(g.edgeSet())) {
                //Primeiro pegas as arestas que não são pontes
                for (DefaultEdge aresta : g.edgesOf(v)) {
                    if (!pontes.contains(aresta)) {
                        arestas.add(aresta);
                    }
                }
                //Depois as que são pontes
                for (DefaultEdge aresta : g.edgesOf(v)) {
                    if (pontes.contains(aresta)) {
                        arestas.add(aresta);
                    }
                }

                DefaultEdge aresta = arestas.poll();
                if (!visitadas.contains(aresta)) {
                    visitadas.add(aresta);
                    circuito.add(aresta);
                    if (!g.getEdgeTarget(aresta).equals(v)) {
                        v = (Vertice) g.getEdgeTarget(aresta);
                    } else {
                        v = (Vertice) g.getEdgeSource(aresta);
                    }
                }
                while (!arestas.isEmpty() && !visitadas.contains(aresta)) {
                    aresta = arestas.poll();
                }
            }

        }

        return null;
    }

}
