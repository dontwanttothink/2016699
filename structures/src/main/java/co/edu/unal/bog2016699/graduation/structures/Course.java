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
}
