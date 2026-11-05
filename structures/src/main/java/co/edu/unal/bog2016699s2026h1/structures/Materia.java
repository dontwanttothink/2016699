package co.edu.unal.bog2016699s2026h1.structures;

public class Materia {
	private int id;
	private String nombre;
	private int creditos;
	private ArrayList<Materia> dependientes;
	private ArrayList<Materia> prerrequisitos;

	public Materia(int id, String nombre, int créditos) {
		this.id = id;
		this.nombre = nombre;
		this.creditos = creditos;
		this.dependientes = new ArrayList<>();
		this.prerrequisitos = new ArrayList<>();
	}
}
