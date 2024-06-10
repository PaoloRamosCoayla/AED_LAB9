package Lab9;

import java.util.ArrayList;

public class BNode<E extends Comparable<E>> {
    protected static int contador = 0;
    protected ArrayList<E> keys;
    protected ArrayList<BNode<E>> childs;
    protected int count;
    protected int idNode;

    public BNode (int n){
        idNode = contador;
        contador++;
        this.keys = new ArrayList<E>(n);
        this.childs = new ArrayList<BNode<E>>(n + 1);
        this.count = 0;
        for (int i = 0; i < n; i++) {
            this.keys.add(null);
            this.childs.add(null);
        }
    }

    public boolean nodeFull (int n) {
        return count == n;
    }

    public boolean nodeEmpty () {
        return count == 0;
    }   

    public boolean searchNode(E key, int[] pos) {
        for (int i = 0; i < count; i++) {
            if (keys.get(i).compareTo(key) == 0) {
                pos[0] = i;
                return true;
            } else if (keys.get(i).compareTo(key) > 0) {
                pos[0] = i;
                return false;
            }
        }
        pos[0] = count;
        return false;
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("ID: ").append(idNode).append(", Keys: ");
        for (int i = 0; i < count; i++) {
            if (i > 0) sb.append(", ");
            sb.append(keys.get(i));
        }
        return sb.toString();
    }
}
