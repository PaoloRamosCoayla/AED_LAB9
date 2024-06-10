package Lab9;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class BTree<E extends Comparable<E>> {
    public BNode<E> root;
    private int orden;
    private boolean up;
    private BNode<E> nDes;

    public BTree(int orden) {
        this.orden = orden;
        this.root = null;
    }

    public void insert(E cl) {
        up = false;
        E mediana;
        BNode<E> pnew;
        mediana = push(this.root, cl);
        if (up) {
            pnew = new BNode<E>(this.orden);
            pnew.count = 1;
            pnew.keys.set(0, mediana);
            pnew.childs.set(0, this.root);
            pnew.childs.set(1, nDes);
            this.root = pnew;
        }
    }

    private E push(BNode<E> current,E cl){
        int pos[] = new int[1];
        E mediana;
        if(current == null){
            up = true;
            nDes = null;
            return cl;
        }
        else{
            boolean fl;
            fl = current.searchNode(cl, pos);
            if(fl){
                System.out.println("Item duplicado\n");
                up = false;
                return null;
            }
            mediana = push(current.childs.get(pos[0]),cl);
            if(up){
                if(current.nodeFull(this.orden-1))
                    mediana = dividedNode(current,mediana,pos[0]);
                else{
                    up = false;
                    putNode(current,mediana,nDes,pos[0]);
                }
            }
            return mediana;
        }
    }

    private void putNode(BNode<E> current,E cl,BNode<E> rd,int k){
        int i;
        for(i = current.count-1; i >= k; i--) {
            current.keys.set(i+1,current.keys.get(i));
            current.childs.set(i+2,current.childs.get(i+1));
        }
        current.keys.set(k,cl);
        current.childs.set(k+1,rd);
        current.count++;
    }

    private E dividedNode(BNode<E> current,E cl,int k){
        BNode<E> rd = nDes;
        int i, posMdna;
        posMdna = (k <= this.orden/2) ? this.orden/2 : this.orden/2+1;
        nDes = new BNode<E>(this.orden);
        for(i = posMdna; i < this.orden-1; i++) {
            nDes.keys.set(i-posMdna,current.keys.get(i));
            nDes.childs.set(i-posMdna+1,current.childs.get(i+1));
        }
        nDes.count = (this.orden - 1) - posMdna;
        current.count = posMdna;
        if(k <= this.orden/2)
            putNode(current,cl,rd,k);
        else
            putNode(nDes,cl,rd,k-posMdna);
        E median = current.keys.get(current.count-1);
        nDes.childs.set(0,current.childs.get(current.count));
        current.count--;
        return median;
    }

    public String toString(){
        String s = "";
        if (isEmpty())
            s += "BTree is empty...";
        else
            s = writeTree(this.root, -1);
        return s;
    } 

    private String writeTree(BNode<E> current, int parentId){
        if (current == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Id.Nodo: ").append(current.idNode)
          .append(", Claves Nodo: ").append(current.keys.subList(0, current.count))
          .append(", Id.Padre: ").append(parentId == -1 ? "--" : parentId)
          .append(", Id.Hijos: [");
        boolean firstChild = true;
        for (int i = 0; i <= current.count; i++) {
            if (current.childs.get(i) != null) {
                if (!firstChild) sb.append(", ");
                sb.append(current.childs.get(i).idNode);
                firstChild = false;
            }
        }
        sb.append("]\n");
        for (int i = 0; i <= current.count; i++) {
            if (current.childs.get(i) != null) {
                sb.append(writeTree(current.childs.get(i), current.idNode));
            }
        }
        return sb.toString();
    }

    public boolean isEmpty() {
        return root == null;
    }

    public String search(E cl){
        return searchNode(root, cl);
    }

    private String searchNode(BNode<E> current, E cl) {
        if (isEmpty()) {
            return cl + " no se encuentra en el árbol.";
        }
        int pos[] = new int[1];
        boolean encontrado = current.searchNode(cl, pos);
        if (encontrado) {
            return cl + " se encuentra en el nodo " + current.idNode + " en la posición " + pos[0];
        } else {
            return searchNode(current.childs.get(pos[0]), cl);
        }
    }   


    public void remove(E cl){
        remove(root, cl);
    }

    private void remove(BNode<E> node, E key) {
        if (node == null) {
            return;
        }
        int pos[] = new int[1];
        boolean found = node.searchNode(key, pos);
        if (found) {
            if (node.childs.get(pos[0]) == null) {
                removeKey(node, key, pos[0]);
            } else {
                remplazoPredecesor(node, key, pos[0]);
            }
        } else {
            remove(node.childs.get(pos[0]), key);
            if (node.childs.get(pos[0]) != null && node.childs.get(pos[0]).count < (orden - 1) / 2) {
                restaurar(node, pos[0]);
            }
        }
    }

    private void removeKey(BNode<E> node, E key, int pos) {
        for (int i = pos; i < node.count - 1; i++) {
            node.keys.set(i, node.keys.get(i + 1));
        }
        node.count--;
    }

    private void remplazoPredecesor(BNode<E> node, E key, int pos) {
        BNode<E> predNode = node.childs.get(pos);
        E predKey = null;
        while (predNode.childs.get(predNode.count) != null) {
            predNode = predNode.childs.get(predNode.count);
        }
        predKey = predNode.keys.get(predNode.count - 1);
        remove(predNode, predKey);
        node.keys.set(pos, predKey);
    }

    private void restaurar(BNode<E> node, int pos) {
        if (pos > 0 && node.childs.get(pos - 1).count > (orden - 1) / 2) {
            moveRight(node, pos);
        } else if (pos < node.count && node.childs.get(pos + 1).count > (orden - 1) / 2) {
            moveLeft(node, pos);
        } else {
            if (pos == node.count) {
                pos--;
            }
            fusion(node, pos);
        }
    }

    private void moveRight(BNode<E> node, int pos) {
        BNode<E> leftChild = node.childs.get(pos - 1);
        BNode<E> rightChild = node.childs.get(pos);
        rightChild.count++;
        for (int i = rightChild.count - 1; i > 0; i--) {
            rightChild.keys.set(i, rightChild.keys.get(i - 1));
        }
        rightChild.keys.set(0, node.keys.get(pos - 1));
        node.keys.set(pos - 1, leftChild.keys.get(leftChild.count - 1));
        leftChild.count--;
    }

    private void moveLeft(BNode<E> node, int pos) {
        BNode<E> leftChild = node.childs.get(pos);
        BNode<E> rightChild = node.childs.get(pos + 1);
        leftChild.keys.set(leftChild.count, node.keys.get(pos));
        node.keys.set(pos, rightChild.keys.get(0));
        leftChild.count++;
        for (int i = 0; i < rightChild.count - 1; i++) {
            rightChild.keys.set(i, rightChild.keys.get(i + 1));
        }
        rightChild.count--;
    }

    private void fusion(BNode<E> node, int pos) {
        BNode<E> leftChild = node.childs.get(pos);
        BNode<E> rightChild = node.childs.get(pos + 1);
        leftChild.keys.set(leftChild.count, node.keys.get(pos));
        leftChild.count++;
        for (int i = 0; i < rightChild.count; i++) {
            leftChild.keys.set(leftChild.count + i, rightChild.keys.get(i));
        }
        leftChild.count += rightChild.count;
        for (int i = pos; i < node.count - 1; i++) {
            node.keys.set(i, node.keys.get(i + 1));
            node.childs.set(i + 1, node.childs.get(i + 2));
        }
        node.count--;
    }

    public static <E extends Comparable<E>> BTree<E> building_Btree(String fileName) throws Exception {
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        int orden = Integer.parseInt(reader.readLine());
        BTree<E> tree = new BTree<>(orden);
        String line;
        List<BNode<E>> nodes = new ArrayList<>();
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(",");
            int nivel = Integer.parseInt(parts[0]);
            int idNodo = Integer.parseInt(parts[1]);
            BNode<E> node = new BNode<>(orden);
            node.idNode = idNodo;
            for (int i = 2; i < parts.length; i++) {
                node.keys.set(i - 2, (E) Integer.valueOf(parts[i]));
            }
            node.count = parts.length - 2;
            if (nivel == 0) {
                tree.root = node;
            }
            nodes.add(node);
        }
        reader.close();
    
        for (BNode<E> node : nodes) {
            for (int i = 0; i < node.count; i++) {
                int childId = node.keys.get(i).hashCode() % nodes.size();
                if (childId != node.idNode) {
                    node.childs.set(i, nodes.get(childId));
                }
            }
        }
    
        if (!tree.arbolInvalido()) {
            System.out.println("Árbol B no válido:");
            System.out.println(tree.toString());
    
            throw new ItemNoFound();
        }
    
        return tree;
    }
    

    public boolean arbolInvalido() {
        return validarNodos(root, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    private boolean validarNodos(BNode<E> node, int min, int max) {
        if (node == null) return true;
        for (int i = 0; i < node.count; i++) {
            if (node.keys.get(i) == null) continue;
            int key = (Integer) node.keys.get(i);
            if (key < min || key > max) return false;
            if (i > 0 && node.keys.get(i).compareTo(node.keys.get(i - 1)) < 0) return false;
        }
        for (int i = 0; i <= node.count; i++) {
            if (!validarNodos(node.childs.get(i), i == 0 ? min : (Integer) node.keys.get(i - 1), i == node.count ? max : (Integer) node.keys.get(i))) {
                return false;
            }
        }
        return true;
    }
}
