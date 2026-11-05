package co.edu.unal.bog2016699s2026h1.structures;

import java.util.ArrayList;

public class Plan {
	ArrayList<Materia> raices = new ArrayList<>();

	public void agregarRaiz(Materia materia) {
		raices.add(materia);
	}

	public void eliminar(Materia materia) {
		if (raices.contains(materia)) {
			for (Materia dependiente : materia.dependientes) {
				dependiente.prerrequisitos.remove(materia);
				if (dependiente.prerrequisitos.size() == 0) {
					raices.add(dependiente);
				}
			}

			raices.remove(materia);
		} else {
			for (Materia padre : materia.prerrequisitos) {
				for (Materia dependiente : materia.dependientes) {
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
