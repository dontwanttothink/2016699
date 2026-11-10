package co.edu.unal.bog2016699.graduation.structures;

import java.util.RandomAccess;
import java.util.AbstractList;
import java.util.Collection;
import java.util.Objects;

import static java.util.Objects.checkFromToIndex;
import static java.util.Objects.checkIndex;
import static java.util.Arrays.copyOf;
import static java.util.Arrays.fill;

// Véase https://docs.oracle.com/en/java/javase/25/docs/api/java.base/java/util/AbstractList.html

public class DynamicArray<T> extends AbstractList<T> implements RandomAccess {
	@SuppressWarnings("unchecked")
	private T[] buffer = (T[]) new Object[0];

	private int size = 0;

	public DynamicArray() {
		super();
	}

	public DynamicArray(Collection<T> c) {
		Objects.requireNonNull(c, "c");
		this.addAll(c);
	}

	@Override
	public int size() {
		return size;
	}

	@Override
	public T get(int index) {
		checkIndex(index, size);
		return buffer[index];
	}

	@Override
	public T set(int index, T value) {
		checkIndex(index, size);
		T old = buffer[index];
		buffer[index] = value;
		return old;
	}

	@Override
	public void add(int index, T value) {
		if (index < 0 || index > size) {
			throw new IndexOutOfBoundsException(index);
		}

		growIfNeeded();

		System.arraycopy(buffer, index, buffer, index + 1, size - index);
		buffer[index] = value;
		++size;
		++modCount;
	}

	@Override
	public T remove(int index) {
		checkIndex(index, size);
		T old = buffer[index];

		int moved = size - 1 - index;
		if (moved > 0) {
			System.arraycopy(buffer, index + 1, buffer, index, moved);
		}

		--size;
		buffer[size] = null;

		++modCount;
		shrinkIfNeeded();
		return old;
	}

	@Override
	protected void removeRange(int from, int to) {
		checkFromToIndex(from, to, size);

		int moved = size - to;
		if (moved > 0) {
			System.arraycopy(buffer, to, buffer, from, moved);
		}
		fill(buffer, size - (to - from), size, null);

		++modCount;
		size -= (to - from);
		shrinkIfNeeded();
	}

	private void shrinkIfNeeded() {
		if (size <= buffer.length / 4) {
			buffer = copyOf(buffer, buffer.length / 2);
		}
	}

	private void growIfNeeded() {
		if (size == buffer.length) {
			buffer = copyOf(buffer, Math.max(size * 2, 1));
		}
	}

	@Override
	public int indexOf(Object value) {
		if (value == null) {
			for (int i = 0; i < size; i++) {
				if (buffer[i] == null)
					return i;
			}
		} else {
			for (int i = 0; i < size; i++) {
				if (value.equals(buffer[i]))
					return i;
			}
		}
		return -1;
	}

	@Override
	public boolean remove(Object value) {
		int index = indexOf(value);
		if (index >= 0) {
			remove(index);
			return true;
		}
		return false;
	}

}
