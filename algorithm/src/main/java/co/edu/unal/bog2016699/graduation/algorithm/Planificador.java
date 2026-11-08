package co.edu.unal.bog2016699.graduation.algorithm;

import co.edu.unal.bog2016699.graduation.structures.DynamicArray;
import co.edu.unal.bog2016699.graduation.structures.HeapPlan;
import co.edu.unal.bog2016699.graduation.structures.Plan;
import co.edu.unal.bog2016699.graduation.structures.Course;

public class Planificador {
	public static int creditosMax;

	public static void setCreditosMax(int creditos) {
		creditosMax = creditos;
	}

	public static DynamicArray<DynamicArray<Course>> planificar(Plan plan) {
		DynamicArray<DynamicArray<Course>> semestres = new DynamicArray<>();
		;
		while (plan.getRaices().size() > 0) {
			DynamicArray<Course> semestreActual = new DynamicArray<>();
			int creditosUsados = 0;

			DynamicArray<Course> materiasCandidatas = clonarArreglo(plan.getRaices());
			while (materiasCandidatas.size() > 0) {
				Course mejorMateria = null;
				for (Course materia : materiasCandidatas) {
					if (creditosUsados + materia.getCreditos() <= creditosMax) {
						if (mejorMateria == null
								|| materia.getDependientesSize() > mejorMateria.getDependientesSize()) {
							mejorMateria = materia;
						}
					}
				}

				if (mejorMateria == null)
					break;
				else {
					semestreActual.add(mejorMateria);
					creditosUsados += mejorMateria.getCreditos();

					materiasCandidatas.remove(mejorMateria);
				}
			}

			for (Course materia : semestreActual) {
				plan.eliminar(materia);
			}
			semestres.add(semestreActual);
		}
		return semestres;
	}

	private static DynamicArray<Course> clonarArreglo(DynamicArray<Course> arreglo) {
		DynamicArray<Course> copia = new DynamicArray<>();
		for (int i = 0; i < arreglo.size(); i++) {
			copia.add(arreglo.get(i));
		}
		return copia;
	}

	public static DynamicArray<DynamicArray<Course>> planificar(HeapPlan plan) {
		throw new UnsupportedOperationException("sin implementar");
	}
}
