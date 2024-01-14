public class Node<E>{
    //generic node class used in GenericStack
    E element;
    Node<E> next;
    public Node(E initElement){
        element = initElement;
    }
}
