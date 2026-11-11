package co.edu.unal.bog2016699.graduation.app;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Collectors;

import co.edu.unal.bog2016699.graduation.algorithm.Planner;
import co.edu.unal.bog2016699.graduation.structures.*;

public class App {
	private static final String USAGE = """
			Usage: planner --algorithm <id> [--avl <orden>] <file>

			Arguments:
			    file        Input file, or - to read from standard input.

			Options:
			    --algorithm <id>    Set to 0 for the list-based planner.
			                        Set to 1 for the heap-based planner.
			    --avl <orden>       Show the prerequisite pairs {prerequisite, course it unlocks}
			                        next to the plan. <orden> is "inorder" (sorted by id)
			                        or "levels" (level by level in the AVL tree).
			""";

	public static void main(String[] args) throws IOException {
		if (args.length == 1 && args[0].equals("--help")) {
			usage();
			return;
		}

		String algorithm = null, avl = null, file = null;
		boolean valid = true;

		for (int i = 0; i < args.length; i++) {
			switch (args[i]) {
				case "--algorithm" -> algorithm = (++i < args.length) ? args[i] : null;
				case "--avl" -> avl = (++i < args.length) ? args[i] : null;
				default -> {
					if (file == null)
						file = args[i];
					else
						valid = false;
				}
			}
		}

		if (algorithm == null || file == null) {
			valid = false;
		}

		boolean algorithmOk = "0".equals(algorithm) || "1".equals(algorithm);
		boolean avlOk = avl == null || "inorder".equals(avl) || "levels".equals(avl);

		if (valid && algorithmOk && avlOk) {
			String input = file.equals("-") ? new String(System.in.readAllBytes()) : Files.readString(Path.of(file));
			if ("0".equals(algorithm)) {
				listPlanner(input, avl);
			} else {
				heapPlanner(input, avl);
			}
			return;
		}

		usage();
		System.exit(1);
	}

	static String semesterToString(DynamicArray<Course> semester) {
		return semester.stream().map(c -> String.valueOf(c.getId())).collect(Collectors.joining(", ", "[", "]"));
	}

	static void printSemesters(DynamicArray<DynamicArray<Course>> semesters) {
		System.out.println(semesters.stream().map(App::semesterToString).collect(Collectors.joining(", ", "[", "]")));
	}

	private static final class Entry {
		final Course course;
		final DynamicArray<Course> prerequisites;
		int shown = 0;

		Entry(Course course) {
			this.course = course;
			this.prerequisites = new DynamicArray<>(course.getPrerequisites());
		}
	}

	static DynamicArray<Entry> snapshot(Course[] courses, String avlOrder) {
		AVLTree<Course> tree = new AVLTree<>(Course::getId);
		for (Course course : courses)
			tree.put(course);
		DynamicArray<Course> ordered = "levels".equals(avlOrder) ? tree.levelOrder() : tree.inOrder();

		DynamicArray<Entry> entries = new DynamicArray<>();
		for (Course course : ordered)
			entries.add(new Entry(course));
		return entries;
	}

	static String pairToString(Course prerequisite, Course unlocked) {
		return "{" + prerequisite.getId() + " " + prerequisite.getName() + ", " + unlocked.getId() + " "
				+ unlocked.getName() + "}";
	}

	static DynamicArray<String> buildPairs(DynamicArray<Entry> entries, DynamicArray<DynamicArray<Course>> semesters) {
		DynamicArray<String> pairs = new DynamicArray<>();
		for (Entry entry : entries) {
			if (entry.prerequisites.size() > 0)
				pairs.add(pairToString(entry.prerequisites.get(0), entry.course));
		}

		AVLTree<Course> passed = new AVLTree<>(Course::getId);
		for (DynamicArray<Course> semester : semesters) {
			for (Course course : semester)
				passed.put(course);
			for (Entry entry : entries) {
				int before = entry.shown;
				while (entry.shown < entry.prerequisites.size()
						&& passed.has(entry.prerequisites.get(entry.shown).getId()))
					entry.shown++;
				if (entry.shown != before && entry.shown < entry.prerequisites.size())
					pairs.add(pairToString(entry.prerequisites.get(entry.shown), entry.course));
			}
		}
		return pairs;
	}

	static void printSideBySide(DynamicArray<String> left, DynamicArray<DynamicArray<Course>> semesters,
			String rightHeader) {
		DynamicArray<String> right = new DynamicArray<>();
		for (DynamicArray<Course> semester : semesters)
			right.add(semesterToString(semester));

		int width = "avl".length();
		for (String cell : left)
			width = Math.max(width, cell.length());

		StringBuilder sb = new StringBuilder();
		sb.append(String.format("%-" + width + "s", "avl")).append(" || ").append(rightHeader).append('\n');
		int rows = Math.max(left.size(), right.size());
		for (int i = 0; i < rows; i++) {
			String l = i < left.size() ? left.get(i) : "";
			String r = i < right.size() ? right.get(i) : "";
			sb.append((String.format("%-" + width + "s", l) + " || " + r).stripTrailing()).append('\n');
		}
		System.out.print(sb);
	}

	static void output(DynamicArray<Entry> entries, DynamicArray<DynamicArray<Course>> semesters, String rightHeader) {
		if (entries == null) {
			printSemesters(semesters);
		} else {
			printSideBySide(buildPairs(entries, semesters), semesters, rightHeader);
		}
	}

	static void listPlanner(String input, String avlOrder) {
		Course[] courses = Course.from(input);
		DynamicArray<Entry> entries = avlOrder == null ? null : snapshot(courses, avlOrder);
		ListPlan plan = new ListPlan();
		for (Course course : courses) {
			plan.agregarRaiz(course);
		}
		output(entries, Planner.planificar(plan), "lista");
	}

	static void heapPlanner(String input, String avlOrder) {
		Course[] courses = Course.from(input);
		DynamicArray<Entry> entries = avlOrder == null ? null : snapshot(courses, avlOrder);
		HeapPlan plan = new HeapPlan();

		for (Course course : courses) {
			plan.put(course);
		}
		output(entries, Planner.planificar(plan), "heap");
	}

	private static void usage() {
		System.out.println(USAGE);
	}
}
