package co.edu.unal.bog2016699.graduation.structures;

import java.util.Arrays;

public class Course {
	int id;
	int credits;
	String nombre;
	DynamicArray<Course> dependents;
	DynamicArray<Course> prerequisites;

	// id$créditos$nombre$id,id,id,id(dependientes)$id,id,id,id(prerrequisitos)
	public static Course[] from(String serialized) {
		String[][] rows = serialized.lines()
				.filter(line -> !line.isBlank())
				.map(line -> line.split("\\$", -1))
				.toArray(String[][]::new);

		Course[] courses = new Course[rows.length];
		AVLTree<Course> byId = new AVLTree<>(Course::getId);
		for (int i = 0; i < rows.length; i++) {
			courses[i] = new Course(Integer.parseInt(rows[i][0]), rows[i][2], Integer.parseInt(rows[i][1]));
			byId.put(courses[i]);
		}

		for (int i = 0; i < rows.length; i++) {
			Course course = courses[i];
			for (int id : parseIds(rows[i][3])) {
				Course dependent = byId.find(id);
				if (!course.dependents.contains(dependent)) {
					course.addDependent(dependent);
				}
			}
			for (int id : parseIds(rows[i][4])) {
				Course prerequisite = byId.find(id);
				if (!course.prerequisites.contains(prerequisite)) {
					prerequisite.addDependent(course);
				}
			}
		}

		return courses;
	}

	private static int[] parseIds(String ids) {
		return ids.isEmpty() ? new int[0] : Arrays.stream(ids.split(",")).mapToInt(Integer::parseInt).toArray();
	}

	public Course(int id, String nombre, int creditos) {
		this.id = id;
		this.nombre = nombre;
		this.credits = creditos;
		this.dependents = new DynamicArray<>();
		this.prerequisites = new DynamicArray<>();
	}

	public void addDependent(Course materia) {
		this.dependents.add(materia);
		materia.prerequisites.add(this);
	}

	public String getName() {
		return nombre;
	}

	public int getId() {
		return id;
	}

	public int getCredits() {
		return credits;
	}

	public DynamicArray<Course> getDependents() {
		return dependents;
	}

	public DynamicArray<Course> getPrerequisites() {
		return prerequisites;
	}

	public int getDependentsCount() {
		return dependents.size();
	}

	public boolean isAvailable() {
		return prerequisites.size() == 0;
	}

	public int comparePriority(Course materia) {
		if (this.isAvailable() != materia.isAvailable()) {
			return this.isAvailable() ? 1 : -1;
		}
		return dependents.size() - materia.dependents.size();
	}
}
