package co.edu.unal.bog2016699.graduation.structures;

// orden en el que van primero las materias disponibles

// (es decir, que se pueden cursar),
// y luego las que están bloqueadas. desempate por el número de dependientes
// (menos dependientes van primero). **mas dependientes va primero no?

public class HeapPlan {

	private DynamicArray<Course> heap;
	private int size;

	public HeapPlan() {

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
		heap.set(j, temp);
	}

	private void siftUp(int i) {
		if (i == 0 || heap.get(i).comparePriority(heap.get(parent(i))) < 0)
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
		heap.add(heap.size(), materia);
		siftUp(size);
		size++;
	}

	private Course extractMax_priv() {
		Course max = heap.get(0);
		swap(0, size - 1);
		heap.remove(size - 1);
		size--;
		if (size > 0)
			siftDown(0);
		return max;
	}

	/*
	 * public Course extractMax() { // quitarse del prerrequisito de los dependintes
	 * Course max = extractMax_priv();
	 * for (int i = 0; i < max.dependents.size(); i++) {
	 * max.dependents.get(i).prerequisites.remove(0);
	 * for (int j = 0; j < max.dependents.get(i).prerequisites.size(); j++) {
	 * if (max == max.dependents.get(i).prerequisites.get(j)) {
	 * max.dependents.get(i).prerequisites.remove(j);
	 * }
	 * }
	 * }
	 * return max; //metodo sin el remove(val) de arraylist
	 * }
	 */

	public Course extractMax() {
		if (size == 0)
			throw new IllegalStateException("El HeapPlan está vacío");

		return extractMax_priv();
	}

	public Course extractMaxCredits(int creditos) {
		DynamicArray<Course> temp = new DynamicArray<>();

		Course removed = null;
		Course max = heap.get(0);
		Course maxOut = null;

		while (size > 0 && max.credits > creditos) {
			removed = extractMax_priv();
			if (size > 0) {
				max = heap.get(0);
			}
			temp.add(removed);
		}
		if (size > 0 && max.isAvailable()) {
			maxOut = extractMax();
		}
		for (int i = 0; i < temp.size(); i++) {
			put(temp.get(i));
		}
		return maxOut;
	}

	public int size() {
		return size;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Estado actual del HeapPlan (Materias disponibles):\n");

		for (int i = 0; i < heap.size(); i++) {
			sb.append("[").append(i).append("] ")
					.append(heap.get(i).getId())
					.append("\n");
		}
		return sb.toString();
	}
}
