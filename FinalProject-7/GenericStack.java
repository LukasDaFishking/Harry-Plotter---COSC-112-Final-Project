public class GenericStack<T>{
    //generic data structure
    public Node<T> first;

    public GenericStack(){
        first = null;
    }
    //constructor makes it empty
    public T pop(){
        //returns and removes the first element
        T toReturn = first.element;
        first = first.next;
        return toReturn;
    }
    public T popLast(){
        //returns and removes the last element
        Node<T> toReturn = first;
        while(toReturn.next != null){
            toReturn = toReturn.next;
        }
        Node<T> newLast = first;
        while(newLast.next != toReturn){
            newLast = newLast.next;
        }
        newLast.next = null;
        return(toReturn.element);
    }
    public void add(T element){
        //adds an element to the beginning
        Node<T> node = new Node(element);
        Node<T> next = first;
        first = node;
        first.next = next;
    }

    public void addLast(T element){
        //adds an element to the end
        Node<T> node = new Node(element);
        Node<T> n = first;
        while(n.next != null){
            n = n.next;
        }
        n.next = node;
    }

}
