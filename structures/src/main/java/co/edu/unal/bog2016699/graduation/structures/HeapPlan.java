package co.edu.unal.bog2016699.graduation.structures;

import java.util.NoSuchElementException;

public class HeapPlan {
	private static final class CourseIndexRecord {
		Course course;
		int index;

		CourseIndexRecord(Course course, int index) {
			this.course = course;
			this.index = index;
		}

		@Override
		public String toString() {
			return course.id + "," + index;
		}
	}

	private final AVLTree<CourseIndexRecord> indices;
	private final boolean verbose;
	private DynamicArray<Course> heap;
	private int size;

	public HeapPlan() {
		this(false);
	}

	public HeapPlan(boolean verbose) {
		this.verbose = verbose;
		indices = new AVLTree<CourseIndexRecord>(record -> record.course.id, verbose);
		heap = new DynamicArray<>();
		size = 0;
	}

	private int parent(int i) {
		return (i - 1) / 2;
	}

	private int leftChild(int i) {
		return 2 * i + 1;
	}

	private int rightChild(int i) {
		return 2 * i + 2;
	}

	private void swap(int i, int j) {
		Course temp = heap.get(i);
		heap.set(i, heap.get(j));
		indices.find(heap.get(j).id).index = i;
		heap.set(j, temp);
		indices.find(temp.id).index = j;
	}

	private void siftUp(int i) {
		if (i == 0 || heap.get(i).comparePriority(heap.get(parent(i))) <= 0)
			return;
		swap(i, parent(i));
		siftUp(parent(i));
	}

	private void siftDown(int i) {
		int max = i;

		if (leftChild(i) < heap.size() && heap.get(leftChild(i)).comparePriority(heap.get(i)) > 0)
			max = leftChild(i);
		if (rightChild(i) < heap.size() && heap.get(rightChild(i)).comparePriority(heap.get(max)) > 0)
			max = rightChild(i);

		if (max != i) {
			swap(i, max);
			siftDown(max);
		}
	}

	public void put(Course materia) {
		if (indices.has(materia.id)) {
			throw new IllegalArgumentException("course " + materia.id + " is already in the heap");
		}
		heap.add(materia);
		indices.put(new CourseIndexRecord(materia, size));
		siftUp(size);
		size++;
		lookingGlass();
	}

	public Course peek() {
		if (size == 0) {
			throw new NoSuchElementException();
		}
		return heap.get(0);
	}

	public Course extractMax() {
		Course max = peek();
		swap(0, size - 1);
		indices.delete(heap.getLast().id);
		heap.remove(size - 1);

		size--;
		if (size > 0) {
			siftDown(0);
		}
		lookingGlass();
		return max;
	}

	public void notifyPriorityChanged(Course materia) {
		indices.maybeFind(materia.id).ifPresent(record -> {
			siftUp(record.index);
			siftDown(record.index);
			lookingGlass();
		});
	}

	public Course extractMaxAvailableGivenMaximumCredits(int credits) {
		DynamicArray<Course> removed = new DynamicArray<>();

		while (size > 0 && peek().isAvailable() && peek().credits > credits) {
			Course r = extractMax();
			removed.add(r);
		}

		Course out = null;
		if (size > 0 && peek().isAvailable()) {
			out = extractMax();
		}

		for (int i = 0; i < removed.size(); i++) {
			put(removed.get(i));
		}

		return out;
	}

	public int size() {
		return size;
	}

	private void lookingGlass() {
		if (!verbose) {
			return;
		}

		StringBuilder sb = new StringBuilder();
		sb.append("Estado del Heap:\n");
		for (int i = 0; i < heap.size(); i++) {
			sb.append(heap.get(i).getId()).append(" ");
		}
		IO.println(sb.toString());
	}
}
