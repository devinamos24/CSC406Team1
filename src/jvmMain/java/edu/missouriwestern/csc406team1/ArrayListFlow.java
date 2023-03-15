package edu.missouriwestern.csc406team1;

import kotlin.collections.CollectionsKt;
import kotlinx.coroutines.flow.FlowKt;
import kotlinx.coroutines.flow.MutableStateFlow;
import kotlinx.coroutines.flow.StateFlow;
import kotlinx.coroutines.flow.StateFlowKt;
import org.jetbrains.annotations.NotNull;
import java.util.*;

public class ArrayListFlow<T> implements Iterable<T>, Collection<T>, List<T> {

    private final MutableStateFlow<List<T>> _list = StateFlowKt.MutableStateFlow(CollectionsKt.listOf());

    public ArrayListFlow() {}

    public ArrayListFlow(Collection<? extends T> c) {
        List<T> old_list = CollectionsKt.toMutableList(_list.getValue());
        old_list.addAll(c);
        _list.setValue(old_list);
    }

    public StateFlow<T> getFlow() {
        return (StateFlow<T>) FlowKt.asStateFlow(_list);
    }

    @Override
    public int size() {
        return _list.getValue().size();
    }

    @Override
    public boolean isEmpty() {
        return _list.getValue().isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return _list.getValue().contains(o);
    }

    @NotNull
    @Override
    public Iterator<T> iterator() {
        return _list.getValue().iterator();
    }

    @NotNull
    @Override
    public Object[] toArray() {
        return _list.getValue().toArray();
    }

    @NotNull
    @Override
    public <T1> T1[] toArray(@NotNull T1[] a) {
        return _list.getValue().toArray(a);
    }

    @Override
    public boolean add(T t) {
        List<T> old_list = CollectionsKt.toMutableList(_list.getValue());
        boolean return_value = old_list.add(t);
        _list.setValue(old_list);
        return return_value;
    }

    @Override
    public boolean remove(Object o) {
        List<T> old_list = CollectionsKt.toMutableList(_list.getValue());
        boolean return_value = old_list.remove(o);
        _list.setValue(old_list);
        return return_value;
    }

    @Override
    public boolean containsAll(@NotNull Collection<?> c) {
        return new HashSet<>(_list.getValue()).containsAll(c);
    }

    @Override
    public boolean addAll(@NotNull Collection<? extends T> c) {
        List<T> old_list = CollectionsKt.toMutableList(_list.getValue());
        boolean return_value = old_list.addAll(c);
        _list.setValue(old_list);
        return return_value;
    }

    @Override
    public boolean addAll(int index, @NotNull Collection<? extends T> c) {
        List<T> old_list = CollectionsKt.toMutableList(_list.getValue());
        boolean return_value = old_list.addAll(index, c);
        _list.setValue(old_list);
        return return_value;
    }

    @Override
    public boolean removeAll(@NotNull Collection<?> c) {
        List<T> old_list = CollectionsKt.toMutableList(_list.getValue());
        boolean return_value = old_list.removeAll(c);
        _list.setValue(old_list);
        return return_value;
    }

    @Override
    public boolean retainAll(@NotNull Collection<?> c) {
        List<T> old_list = CollectionsKt.toMutableList(_list.getValue());
        boolean return_value = old_list.retainAll(c);
        _list.setValue(old_list);
        return return_value;
    }

    @Override
    public void clear() {
        List<T> old_list = CollectionsKt.toMutableList(_list.getValue());
        old_list.clear();
        _list.setValue(old_list);
    }

    @Override
    public T get(int index) {
        return _list.getValue().get(index);
    }

    @Override
    public T set(int index, T element) {
        List<T> old_list = CollectionsKt.toMutableList(_list.getValue());
        T return_value = old_list.set(index, element);
        _list.setValue(old_list);
        return return_value;
    }

    @Override
    public void add(int index, T element) {
        List<T> old_list = CollectionsKt.toMutableList(_list.getValue());
        old_list.add(index, element);
        _list.setValue(old_list);
    }

    @Override
    public T remove(int index) {
        List<T> old_list = CollectionsKt.toMutableList(_list.getValue());
        T return_value = old_list.remove(index);
        _list.setValue(old_list);
        return return_value;
    }

    @Override
    public int indexOf(Object o) {
        return CollectionsKt.toMutableList(_list.getValue()).indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return CollectionsKt.toMutableList(_list.getValue()).lastIndexOf(o);
    }

    @NotNull
    @Override
    public ListIterator<T> listIterator() {
        return CollectionsKt.toMutableList(_list.getValue()).listIterator();
    }

    @NotNull
    @Override
    public ListIterator<T> listIterator(int index) {
        return CollectionsKt.toMutableList(_list.getValue()).listIterator(index);
    }

    @NotNull
    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        ArrayListFlow<T> new_list = new ArrayListFlow<>();
        new_list.addAll(_list.getValue().subList(fromIndex, toIndex));
        return new_list;
    }
}
