package co.edu.unal.bog2016699.graduation.app;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Collectors;

import co.edu.unal.bog2016699.graduation.algorithm.ImpossiblePlanException;
import co.edu.unal.bog2016699.graduation.algorithm.Planner;
import co.edu.unal.bog2016699.graduation.structures.*;

public class App {
	private static final String USAGE = """
			Usage: planner --algorithm <id> [--verbose]

			Arguments:
			    file        Input file, or - to read from standard input.

			Options:
			    --algorithm <id>    Set to 0 for the list-based planner.
			                        Set to 1 for the heap-based planner.
				--verbose    Output the state of internal data structures.
			""";

	public static void main(String[] args) throws IOException {
		if (args.length == 1 && args[0].equals("--help")) {
			usage();
			return;
		}

		String algorithm = null, file = null;
		boolean valid = true, verbose = false;

		for (int i = 0; i < args.length; i++) {
			switch (args[i]) {
				case "--algorithm" -> algorithm = (++i < args.length) ? args[i] : null;
				case "--verbose" -> verbose = true;
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

		if (valid && algorithmOk) {
			String input = file.equals("-") ? new String(System.in.readAllBytes()) : Files.readString(Path.of(file));
			try {
				if ("0".equals(algorithm)) {
					listPlanner(input, verbose);
				} else {
					heapPlanner(input, verbose);
				}
			} catch (ImpossiblePlanException e) {
				System.err.println(e.getMessage());
				System.exit(2);
			}
			return;
		}

		usage();
		System.exit(1);
	}

	static String semesterToString(DynamicArray<Course> semester) {
		return semester.stream().map(c -> String.valueOf(c.getId())).collect(Collectors.joining(" "));
	}

	static void printSemesters(DynamicArray<DynamicArray<Course>> semesters) {
		System.out.println(semesters.stream().map(App::semesterToString).collect(Collectors.joining("|")));
	}

	static void listPlanner(String input, boolean verbose) {
		Course[] courses = Course.from(input);
		ListPlan plan = new ListPlan(verbose);
		for (Course course : courses) {
			if (course.isAvailable()) {
				plan.agregarRaiz(course);
			}
		}
		printSemesters(Planner.plan(plan, courses.length));
	}

	static void heapPlanner(String input, boolean verbose) {
		Course[] courses = Course.from(input);
		HeapPlan plan = new HeapPlan(verbose);

		for (Course course : courses) {
			plan.put(course);
		}
		printSemesters(Planner.plan(plan));
	}

	private static void usage() {
		System.out.println(USAGE);
	}
}
