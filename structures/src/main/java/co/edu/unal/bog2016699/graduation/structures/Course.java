package co.edu.unal.bog2016699.graduation.structures;

public class Course {
	int id;
	String nombre;
	int credits;
	DynamicArray<Course> dependents;
	DynamicArray<Course> prerequisites;

	public Course(int id, String nombre, int creditos) {
		this.id = id;
		this.nombre = nombre;
		this.credits = creditos;
		this.dependents = new DynamicArray<>();
		this.prerequisites = new DynamicArray<>();
	}

	public void insertarDependiente(Course materia) {
		this.dependents.add(materia);
		materia.prerequisites.add(this);
	}

	public int getCredits() {
		return credits;
	}

	public int getDependentsCount() {
		return dependents.size();
	}

	public boolean isAvailable() {
		return prerequisites.size() == 0;
	}

	public int comparePriority(Course materia) {
		if (this.isAvailable() != materia.isAvailable()) {
			return this.isAvailable() ? 1 : -1;
		}
		return dependents.size() - materia.dependents.size();
	}
}
