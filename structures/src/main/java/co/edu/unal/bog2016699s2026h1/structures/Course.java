package co.edu.unal.bog2016699s2026h1.structures;

import java.util.ArrayList;

public class Course {
	int id;
	String nombre;
	int creditos;
	ArrayList<Course> dependientes;
	ArrayList<Course> prerrequisitos;

	public Course(int id, String nombre, int creditos) {
		this.id = id;
		this.nombre = nombre;
		this.creditos = creditos;
		this.dependientes = new ArrayList<>();
		this.prerrequisitos = new ArrayList<>();
	}

	public void insertarDependiente(Course materia) {
		this.dependientes.add(materia);
		materia.prerrequisitos.add(this);
	}
}
