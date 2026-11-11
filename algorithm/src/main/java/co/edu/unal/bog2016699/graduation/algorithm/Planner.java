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

	public static DynamicArray<DynamicArray<Course>> planificar(ListPlan plan) {
		DynamicArray<DynamicArray<Course>> semestres = new DynamicArray<>();
		while (plan.getRoots().size() > 0) {
			DynamicArray<Course> semestreActual = new DynamicArray<>();
			int creditosUsados = 0;

			DynamicArray<Course> materiasCandidatas = new DynamicArray<Course>(plan.getRoots());
			while (materiasCandidatas.size() > 0) {
				Course mejorMateria = null;
				for (Course materia : materiasCandidatas) {
					if (creditosUsados + materia.getCredits() <= maxCredits) {
						if (mejorMateria == null
								|| materia.getDependentsCount() > mejorMateria.getDependentsCount()) {
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

			for (Course materia : semestreActual) {
				plan.eliminar(materia);
			}
			semestres.add(semestreActual);
		}
		return semestres;
	}

	public static DynamicArray<DynamicArray<Course>> planificar(HeapPlan plan) {
		DynamicArray<DynamicArray<Course>> semestres = new DynamicArray<>();
		while (plan.size() > 0) {
			DynamicArray<Course> semestreActual = new DynamicArray<>();
			int creditosUsados = 0;

			while (true) {
				if (plan.size() == 0)
					break;

				Course materia = plan.extractMaxGivenMaximumCredits(maxCredits - creditosUsados);
				if (materia == null)
					break;

				semestreActual.add(materia);
				creditosUsados += materia.getCredits();
			}

			if (semestreActual.size() == 0)
				throw new Error("Existen materias con más creditos que " + maxCredits);

			semestres.add(semestreActual);
		}
		return semestres;
	}
}
