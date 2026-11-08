package co.edu.unal.bog2016699.graduation.structures;

import java.util.List;

public class Plan {
	DynamicArray<Course> roots = new DynamicArray<>();

	public List<Course> getRoots() {
		return List.copyOf(roots);
	}

	public void agregarRaiz(Course materia) {
		roots.add(materia);
	}

	public void eliminar(Course materia) {
		if (roots.contains(materia)) {
			for (Course dependiente : materia.dependientes) {
				dependiente.prerrequisitos.remove(materia);
				if (dependiente.prerrequisitos.size() == 0) {
					roots.add(dependiente);
				}
			}

			roots.remove(materia);
		} else {
			for (Course padre : materia.prerrequisitos) {
				for (Course dependiente : materia.dependientes) {
					padre.dependientes.add(dependiente);
					dependiente.prerrequisitos.add(padre);

					dependiente.prerrequisitos.remove(materia);

					if (dependiente.prerrequisitos.size() == 0) {
						roots.add(dependiente);
					}
				}
				padre.dependientes.remove(materia);
			}
		}
	}
}
