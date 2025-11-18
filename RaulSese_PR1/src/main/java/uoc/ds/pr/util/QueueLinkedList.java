package uoc.ds.pr.util;

import edu.uoc.ds.adt.helpers.Position;
import edu.uoc.ds.adt.sequential.LinkedList;
import edu.uoc.ds.adt.sequential.List;
import edu.uoc.ds.adt.sequential.Queue;
import edu.uoc.ds.exceptions.EmptyContainerException;
import edu.uoc.ds.traversal.Iterator;
import edu.uoc.ds.traversal.Traversal;

public class QueueLinkedList<E> implements Queue<E> {

    // Usamos una LinkedList de la DSLib como estructura interna
    private List<E> list;

    public QueueLinkedList() {
        this.list = new LinkedList<>();
    }

    @Override
    public void add(E e) {
        // Añadimos al final de la lista (FIFO)
        list.insertEnd(e);
    }

    @Override
    public E poll() {
        if (list.isEmpty()) {
            throw new EmptyContainerException();
        }
        // Eliminamos y devolvemos el primero de la lista (FIFO)
        return list.deleteFirst();
    }

    @Override
    public E peek() {
        if (list.isEmpty()) {
            throw new EmptyContainerException();
        }
        // Obtenemos el recorrido de las posiciones de la lista
        Traversal<E> traversal = list.positions();

        // Obtenemos la primera posición del recorrido
        Position<E> firstPosition = traversal.next();

        // Devolvemos el elemento de esa primera posición
        return firstPosition.getElem();
    }

    @Override
    public boolean isEmpty() {
        return list.isEmpty();
    }

    public boolean isFull() {
        // Una lista enlazada no está acotada
        return false;
    }

    @Override
    public int size() {
        return list.size();
    }

    @Override
    public Iterator<E> values() {
        // Simplemente delegamos la llamada a la lista interna
        return list.values();
    }
}