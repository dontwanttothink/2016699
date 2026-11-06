package co.edu.unal.bog2016699s2026h1.structures;

import java.util.ArrayList;

public class Plan {
	ArrayList<Course> raices = new ArrayList<>();

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
