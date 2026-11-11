package co.edu.unal.bog2016699.graduation.structures;

import java.util.NoSuchElementException;
import java.util.Optional;

class Node {
	Course course;
	Node left;
	Node right;

	int height = 1;

	private static int heightOf(Node node) {
		if (node == null) {
			return 0;
		}
		return node.height;
	}

	void updateHeight() {
		height = Math.max(heightOf(left), heightOf(right)) + 1;
	}

	int balanceFactor() {
		return -heightOf(left) + heightOf(right);
	}

	Node(Course materia) {
		this.course = materia;
	}
}

public class AVLTree {
	private Node root = null;

	public void put(Course materia) {
		if (materia == null) {
			throw new IllegalArgumentException("no null 🤬🤬🤬🤬🤬🤬🤬🤬🤬🤬🤬");
		}

		root = put(materia, root);
	}

	private static Node put(Course materia, Node root) {
		if (root == null) {
			return new Node(materia);
		}

		if (materia.id < root.course.id) {
			root.left = put(materia, root.left);
		} else if (materia.id > root.course.id) {
			root.right = put(materia, root.right);
		} else {
			root.course = materia;
		}

		root.updateHeight();
		return rebalance(root);
	}

	void delete(int id) {
		root = delete(id, root);
	}

	private static Node leftmostIn(Node root) {
		Node current = root;
		while (current.left != null) {
			current = current.left;
		}
		return current;
	}

	private static Node delete(int id, Node root) {
		if (root == null) {
			throw new NoSuchElementException();
		}

		if (root.course.id == id) {
			if (root.right == null) {
				return root.left;
			}

			root.course = leftmostIn(root.right).course;
			root.right = delete(root.course.id, root.right);
			root.updateHeight();
			return rebalance(root);
		}

		if (id < root.course.id) {
			root.left = delete(id, root.left);
		} else {
			root.right = delete(id, root.right);
		}
		root.updateHeight();
		return rebalance(root);
	}

	public boolean has(int id) {
		return maybeFind(id).isPresent();
	}

	public Course find(int id) {
		return maybeFind(id).orElseThrow(
				() -> new NoSuchElementException());
	}

	public Optional<Course> maybeFind(int id) {
		Node current = root;
		while (current != null) {
			if (id < current.course.id) {
				current = current.left;
			} else if (id > current.course.id) {
				current = current.right;
			} else {
				return Optional.of(current.course);
			}
		}
		return Optional.empty();
	}
    public DynamicArray<Course> inOrder() {
		DynamicArray<Course> result = new DynamicArray<>();
		inOrder(root, result);
		return result;
	}
 
	private static void inOrder(Node node, DynamicArray<Course> out) {
		if (node == null) {
			return;
		}
		inOrder(node.left, out);
		out.add(node.course);
		inOrder(node.right, out);
	}
 
	
	public DynamicArray<Course> levelOrder() {
		DynamicArray<Course> result = new DynamicArray<>();
		if (root == null) {
			return result;
		}
		for (int level = 1; level <= root.height; level++) {
			collectLevel(root, level, result);
		}
		return result;
	}
 
	private static void collectLevel(Node node, int level, DynamicArray<Course> out) {
		if (node == null) {
			return;
		}
		if (level == 1) {
			out.add(node.course);
			return;
		}
		collectLevel(node.left, level - 1, out);
		collectLevel(node.right, level - 1, out);
	}

	static Node rebalance(Node node) {
		if (node.balanceFactor() < -1 && node.left.balanceFactor() <= 0) {
			node = rightRotate(node);
		} else if (node.balanceFactor() < -1 && node.left.balanceFactor() >= 0) {
			node.left = leftRotate(node.left);
			node = rightRotate(node);
		} else if (node.balanceFactor() > 1 && node.right.balanceFactor() >= 0) {
			node = leftRotate(node);
		} else if (node.balanceFactor() > 1 && node.right.balanceFactor() <= 0) {
			node.right = rightRotate(node.right);
			node = leftRotate(node);
		}
		return node;
	}

	static Node leftRotate(Node node) {
		Node newRoot = node.right;

		node.right = newRoot.left;
		node.updateHeight();

		newRoot.left = node;
		newRoot.updateHeight();

		return newRoot;
	}

	static Node rightRotate(Node node) {
		Node newRoot = node.left;

		node.left = newRoot.right;
		node.updateHeight();

		newRoot.right = node;
		newRoot.updateHeight();

		return newRoot;
	}
}
