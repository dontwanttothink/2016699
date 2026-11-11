package co.edu.unal.bog2016699.graduation.structures;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ListPlan {
	DynamicArray<Course> roots = new DynamicArray<>();
	private final boolean verbose;

	public ListPlan() {
		this(false);
	}

	public ListPlan(boolean verbose) {
		this.verbose = verbose;
	}

	public List<Course> getRoots() {
		return List.copyOf(roots);
	}

	public void agregarRaiz(Course materia) {
		roots.add(materia);
		lookingGlass();
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
		lookingGlass();
	}

	private static void collectEdges(Course course, Set<Course> visited, List<String> edges) {
		if (!visited.add(course)) {
			return;
		}
		for (Course dependiente : course.dependents) {
			edges.add(String.valueOf(course.id));
			edges.add(String.valueOf(dependiente.id));
			collectEdges(dependiente, visited, edges);
		}
	}

	@Override
	public String toString() {
		List<String> edges = new ArrayList<>();
		Set<Course> visited = new HashSet<>();
		for (Course root : roots) {
			collectEdges(root, visited, edges);
		}
		return edges.stream().collect(Collectors.joining(" "));
	}

	private void lookingGlass() {
		if (!verbose) {
			return;
		}

		IO.println("Estado del plan (lista):\n" + this);
	}
}
