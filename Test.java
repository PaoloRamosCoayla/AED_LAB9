package Lab9;

public class Test{

    public static void main(String[] args) {
        BTree<Integer> bTree = new BTree<>(3);

        // Insertar elementos
        bTree.insert(10);
        bTree.insert(20);
        bTree.insert(5);
        bTree.insert(6);
        bTree.insert(12);
        bTree.insert(30);
        bTree.insert(7);
        bTree.insert(17);
        bTree.insert(50);

        System.out.println("Árbol B después de inserciones:");
        System.out.println(bTree.toString());

        System.out.println(bTree.search(6));

        bTree.remove(6);
        bTree.remove(30);

        System.out.println("Árbol B después de eliminaciones:");
        System.out.println(bTree.toString());

        System.out.println("6 no se encuentra en el árbol."+ bTree.search(6));
    }
}
