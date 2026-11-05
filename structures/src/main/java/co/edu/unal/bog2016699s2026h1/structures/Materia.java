package co.edu.unal.bog2016699s2026h1.structures;

import java.util.ArrayList;

public class Materia {
	int id;
	String nombre;
	int creditos;
	ArrayList<Materia> dependientes;
	ArrayList<Materia> prerrequisitos;

	public Materia(int id, String nombre, int creditos) {
		this.id = id;
		this.nombre = nombre;
		this.creditos = creditos;
		this.dependientes = new ArrayList<>();
		this.prerrequisitos = new ArrayList<>();
	}

	public void insertarDependiente(Materia materia) {
		this.dependientes.add(materia);
		materia.prerrequisitos.add(this);
	}
}
