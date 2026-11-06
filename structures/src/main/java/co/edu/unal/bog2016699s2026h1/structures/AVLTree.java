package co.edu.unal.bog2016699s2026h1.structures;

import java.util.NoSuchElementException;
import java.util.Optional;

class Node {
	Materia materia;
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

	Node(Materia materia) {
		this.materia = materia;
	}
}

public class AVLTree {
	private Node root = null;

	public void put(Materia materia) {
		if (materia == null) {
			throw new IllegalArgumentException("no null 🤬🤬🤬🤬🤬🤬🤬🤬🤬🤬🤬");
		}

		root = put(materia, root);
	}

	private static Node put(Materia materia, Node root) {
		if (root == null) {
			return new Node(materia);
		}

		if (materia.id < root.materia.id) {
			root.left = put(materia, root.left);
		} else if (materia.id > root.materia.id) {
			root.right = put(materia, root.right);
		} else {
			root.materia = materia;
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

		if (root.materia.id == id) {
			if (root.right == null) {
				return root.left;
			}

			root.materia = leftmostIn(root.right).materia;
			root.right = delete(root.materia.id, root.right);
			root.updateHeight();
			return rebalance(root);
		}

		if (id < root.materia.id) {
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

	public Materia find(int id) {
		return maybeFind(id).orElseThrow(
				() -> new NoSuchElementException());
	}

	public Optional<Materia> maybeFind(int id) {
		Node current = root;
		while (current != null) {
			if (id < current.materia.id) {
				current = current.left;
			} else if (id > current.materia.id) {
				current = current.right;
			} else {
				return Optional.of(current.materia);
			}
		}
		return Optional.empty();
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
