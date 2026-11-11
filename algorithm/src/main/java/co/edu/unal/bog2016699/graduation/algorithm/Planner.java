package co.edu.unal.bog2016699.graduation.algorithm;

import co.edu.unal.bog2016699.graduation.structures.DynamicArray;
import co.edu.unal.bog2016699.graduation.structures.HeapPlan;
import co.edu.unal.bog2016699.graduation.structures.ListPlan;
import co.edu.unal.bog2016699.graduation.structures.Course;

public class Planner {
	public static int maxCredits = 20;

	public static void setMaxCredits(int creditos) {
		maxCredits = creditos;
	}

	public static DynamicArray<DynamicArray<Course>> plan(ListPlan plan, int totalMaterias) {
		DynamicArray<DynamicArray<Course>> semestres = new DynamicArray<>();
		int materiasPlanificadas = 0;

		while (plan.getRoots().size() > 0) {
			DynamicArray<Course> semestreActual = new DynamicArray<>();
			int creditosUsados = 0;

			DynamicArray<Course> materiasCandidatas = new DynamicArray<Course>(plan.getRoots());
			while (materiasCandidatas.size() > 0) {
				Course mejorMateria = null;
				for (Course materia : materiasCandidatas) {
					if (creditosUsados + materia.getCredits() <= maxCredits) {
						if (mejorMateria == null || materia.comparePriority(mejorMateria) > 0) {
							mejorMateria = materia;
						}
					}
				}

				if (mejorMateria == null)
					break;
				else {
					semestreActual.add(mejorMateria);
					creditosUsados += mejorMateria.getCredits();

					materiasCandidatas.remove(mejorMateria);
				}
			}

			if (semestreActual.size() == 0)
				throw new Error("Existen materias con más creditos que " + maxCredits);

			for (Course materia : semestreActual) {
				plan.eliminar(materia);
			}
			materiasPlanificadas += semestreActual.size();
			semestres.add(semestreActual);
		}

		if (materiasPlanificadas != totalMaterias) {
			throw new Error("Es imposible completar este plan de estudios.");
		}
		return semestres;
	}

	public static DynamicArray<DynamicArray<Course>> plan(HeapPlan plan) {
		DynamicArray<DynamicArray<Course>> semestres = new DynamicArray<>();
		while (plan.size() > 0) {
			DynamicArray<Course> semestreActual = new DynamicArray<>();
			int creditosUsados = 0;

			while (true) {
				Course materia = plan.extractMaxAvailableGivenMaximumCredits(maxCredits - creditosUsados);
				if (materia == null)
					break;

				semestreActual.add(materia);
				creditosUsados += materia.getCredits();
			}

			if (semestreActual.size() == 0) {
				throw new Error("Es imposible completar este plan de estudios.");
			}

			for (Course materia : semestreActual) {
				for (Course dependiente : materia.getDependents()) {
					dependiente.removePrerequisite(materia);
					plan.notifyPriorityChanged(dependiente);
				}
			}

			semestres.add(semestreActual);
		}
		return semestres;
	}
}
