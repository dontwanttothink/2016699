package co.edu.unal.bog2016699.graduation.structures;

public class Plan {
	DynamicArray<Course> raices = new DynamicArray<>();

	public DynamicArray<Course> getRaices() {
		return raices;
	}

	public void agregarRaiz(Course materia) {
		raices.add(materia);
	}

	public void eliminar(Course materia) {
		if (raices.contains(materia)) {
			for (Course dependiente : materia.dependientes) {
				dependiente.prerrequisitos.remove(materia);
				if (dependiente.prerrequisitos.size() == 0) {
					raices.add(dependiente);
				}
			}

			raices.remove(materia);
		} else {
			for (Course padre : materia.prerrequisitos) {
				for (Course dependiente : materia.dependientes) {
					padre.dependientes.add(dependiente);
					dependiente.prerrequisitos.add(padre);

					dependiente.prerrequisitos.remove(materia);

					if (dependiente.prerrequisitos.size() == 0) {
						raices.add(dependiente);
					}
				}
				padre.dependientes.remove(materia);
			}
		}
	}
}
