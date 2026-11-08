package co.edu.unal.bog2016699.graduation.structures;

public class Course {
	int id;
	String nombre;
	int creditos;
	DynamicArray<Course> dependientes;
	DynamicArray<Course> prerrequisitos;

	public Course(int id, String nombre, int creditos) {
		this.id = id;
		this.nombre = nombre;
		this.creditos = creditos;
		this.dependientes = new DynamicArray<>();
		this.prerrequisitos = new DynamicArray<>();
	}

	public void insertarDependiente(Course materia) {
		this.dependientes.add(materia);
		materia.prerrequisitos.add(this);
	}

	public int getCreditos() {
		return creditos;
	}

	public int getDependientesSize() {
		return dependientes.size();
	}

	// Los agregue pero no se si sean necesarios, los crei necesarios para el heap
	public boolean isAvailable() {
		if (prerrequisitos.size() == 0)
			return true;
		return false;
	}

	public int comparePriority(Course materia) {
		if (this.isAvailable() && materia.isAvailable())
			return dependientes.size() - materia.dependientes.size();

		else if (this.isAvailable())
			return 1;
		else if (!this.isAvailable() && !materia.isAvailable())
			return 0;

		return -1;
	}
}
