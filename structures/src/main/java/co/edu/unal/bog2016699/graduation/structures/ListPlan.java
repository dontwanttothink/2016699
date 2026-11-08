package co.edu.unal.bog2016699.graduation.structures;

import java.util.List;

public class ListPlan {
	DynamicArray<Course> roots = new DynamicArray<>();

	public List<Course> getRoots() {
		return List.copyOf(roots);
	}

	public void agregarRaiz(Course materia) {
		roots.add(materia);
	}

	public void eliminar(Course materia) {
		if (roots.contains(materia)) {
			for (Course dependiente : materia.dependents) {
				dependiente.prerequisites.remove(materia);
				if (dependiente.prerequisites.size() == 0) {
					roots.add(dependiente);
				}
			}

			roots.remove(materia);
		} else {
			for (Course padre : materia.prerequisites) {
				for (Course dependiente : materia.dependents) {
					padre.dependents.add(dependiente);
					dependiente.prerequisites.add(padre);

					dependiente.prerequisites.remove(materia);

					if (dependiente.prerequisites.size() == 0) {
						roots.add(dependiente);
					}
				}
				padre.dependents.remove(materia);
			}
		}
	}
}
