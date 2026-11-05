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

	public void insertarDependiente(Materia materia){
		this.dependientes.add(materia);
		materia.prerrequisitos.add(this);
	}

	public Materia eliminar(){
		
		for ( Materia padre: this.prerrequisitos){
			for ( Materia dependiente: this.dependientes){
				padre.dependientes.add(dependiente);
				dependiente.prerrequisitos.add(padre);
				dependiente.prerrequisitos.remove(this);
			}
			padre.dependientes.remove(this);
		}
	}


}
