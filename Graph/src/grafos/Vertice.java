package grafos;

public class Vertice extends Object {

    private int id;
    private int pE;
    private int pS;
    private int back;
    private Vertice pai;

    public Vertice(int id) {
        this.id = id;
        setpE(0);
        setpS(0);
        setBack(0);
        setPai(null);
    }
    
    public Vertice getPai() {
        return pai;
    }

    public void setPai(Vertice pai) {
        this.pai = pai;
    }

    @Override
    public boolean equals(Object v) {

        if (v instanceof Integer) {
            int temp = Integer.parseInt(v.toString());
            if (this.id == (temp)) {
                return true;
            }
            return false;
        } else if (v instanceof Vertice) {
            Vertice temp = (Vertice) v;
            if (this.id == temp.getId()) {
                return true;
            }
            return false;
        }
        return false;
    }
    
    @Override
    public int hashCode(){
        return this.id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getpE() {
        return pE;
    }

    public void setpE(int pE) {
        this.pE = pE;
    }

    public int getpS() {
        return pS;
    }

    public void setpS(int pS) {
        this.pS = pS;
    }

    public int getBack() {
        return back;
    }

    public void setBack(int back) {
        this.back = back;
    }

}
