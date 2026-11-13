package co.edu.unal.bog2016699.graduation.app;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import co.edu.unal.bog2016699.graduation.algorithm.Planner;
import co.edu.unal.bog2016699.graduation.structures.Course;
import co.edu.unal.bog2016699.graduation.structures.DynamicArray;
import co.edu.unal.bog2016699.graduation.structures.HeapPlan;
import co.edu.unal.bog2016699.graduation.structures.ListPlan;

public class PlannerTiempos {
	public static void main(String[] args) throws IOException {
		final int[] TAMAÑOS = { 10, 50, 100, 500, 1_000, 5_000 };
		final int WARMUP_ITERATIONS = 5;
		final int INTENTOS = 20;
		final int CREDITOS_MAX = 20;

		Random rand = new Random(42);

		Map<Integer, Map<String, Double>> resultados = new LinkedHashMap<>();

		for (int n : TAMAÑOS) {
			System.out.printf("n = %d materias\n", n);

			for (int w = 0; w < WARMUP_ITERATIONS; w++) {
				long seed = rand.nextLong();
				ListPlan planListWarmup = construirListPlan(generarMaterias(n, new Random(seed)));
				Planner.plan(planListWarmup, n, CREDITOS_MAX);
				HeapPlan planHeapWarmup = construirHeapPlan(generarMaterias(n, new Random(seed)));
				Planner.plan(planHeapWarmup, CREDITOS_MAX);
			}

			long totalNanosList = 0;
			long totalNanosHeap = 0;
			int semestresPromedioList = 0;
			int semestresPromedioHeap = 0;

			for (int rep = 0; rep < INTENTOS; rep++) {
				// Es necesario generar las materias dos veces porque los métodos de
				// planificación destruyen las estructuras :p

				long seed = rand.nextLong();

				// ----- ListPlan -----
				ListPlan listPlan = construirListPlan(generarMaterias(n, new Random(seed)));
				System.gc();

				long inicio = System.nanoTime();
				semestresPromedioList += Planner.plan(listPlan, n, CREDITOS_MAX).size();
				long fin = System.nanoTime();
				totalNanosList += (fin - inicio);

				// ----- HeapPlan -----
				HeapPlan heapPlan = construirHeapPlan(generarMaterias(n, new Random(seed)));
				System.gc();

				inicio = System.nanoTime();
				semestresPromedioHeap += Planner.plan(heapPlan, CREDITOS_MAX).size();
				fin = System.nanoTime();
				totalNanosHeap += (fin - inicio);
			}

			Map<String, Double> promedios = new LinkedHashMap<>();
			promedios.put("ListPlan", totalNanosList / (double) INTENTOS);
			promedios.put("HeapPlan", totalNanosHeap / (double) INTENTOS);
			promedios.put("ListPlanSemestres", semestresPromedioList / (double) INTENTOS);
			promedios.put("HeapPlanSemestres", semestresPromedioHeap / (double) INTENTOS);
			resultados.put(n, promedios);
		}

		guardarCSV("PlanificadorTiempos.csv", resultados);
	}

	private static DynamicArray<Course> generarMaterias(int n, Random rand) {
		DynamicArray<Course> materias = new DynamicArray<>();
		for (int i = 0; i < n; i++) {
			int creditos = 2 + rand.nextInt(4);
			materias.add(new Course(i, "" + i, creditos));
		}

		for (int i = 0; i < n; i++) {
			Course actual = materias.get(i);
			int numPrerrequisitos = rand.nextInt(3);

			for (int j = 0; j < numPrerrequisitos; j++) {
				if (i > 0) {
					int idxPadre = rand.nextInt(i);
					Course padre = materias.get(idxPadre);
					if (!actual.getPrerequisites().contains(padre)) {
						actual.getPrerequisites().add(padre);
						padre.getDependents().add(actual);
					}
				}
			}
		}
		return materias;
	}

	private static ListPlan construirListPlan(DynamicArray<Course> materias) {
		ListPlan plan = new ListPlan();
		for (Course materia : materias) {
			if (materia.isAvailable()) {
				plan.agregarRaiz(materia);
			}
		}
		return plan;
	}

	private static HeapPlan construirHeapPlan(DynamicArray<Course> materias) {
		HeapPlan plan = new HeapPlan();
		for (Course materia : materias) {
			plan.put(materia);
		}
		return plan;
	}

	private static void guardarCSV(String ruta, Map<Integer, Map<String, Double>> resultados) throws IOException {
		try (PrintWriter csv = new PrintWriter(new FileWriter(ruta))) {
			csv.println("Tamano,ListPlan_ns,HeapPlan_ns,ListPlanSemestres,HeapPlanSemestres");
			for (var fila : resultados.entrySet()) {
				int n = fila.getKey();
				double tList = fila.getValue().get("ListPlan");
				double tHeap = fila.getValue().get("HeapPlan");
				double semestresList = fila.getValue().get("ListPlanSemestres");
				double semestresHeap = fila.getValue().get("HeapPlanSemestres");

				csv.println(String.format(Locale.US, "%d,%.2f,%.2f,%.2f,%.2f", n, tList, tHeap, semestresList,
						semestresHeap));
			}
		}
	}
}
