package co.edu.unal.bog2016699.graduation.structures;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;

class Node<T> {
	T value;
	Node<T> left;
	Node<T> right;

	int height = 1;

	private static int heightOf(Node<?> node) {
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

	Node(T value) {
		this.value = value;
	}
}

public class AVLTree<T> {
	private final ToIntFunction<T> keyOf;
	private final boolean verbose;
	private Node<T> root = null;

	public AVLTree(ToIntFunction<T> keyOf) {
		this(keyOf, false);
	}

	public AVLTree(ToIntFunction<T> keyOf, boolean verbose) {
		this.keyOf = Objects.requireNonNull(keyOf);
		this.verbose = verbose;
	}

	public void put(T item) {
		if (item == null) {
			throw new IllegalArgumentException("Item must not be null.");
		}

		root = put(item, root);
		lookingGlass();
	}

	private Node<T> put(T item, Node<T> node) {
		if (node == null) {
			return new Node<>(item);
		}

		int key = keyOf.applyAsInt(item);
		int nodeKey = keyOf.applyAsInt(node.value);

		if (key < nodeKey) {
			node.left = put(item, node.left);
		} else if (key > nodeKey) {
			node.right = put(item, node.right);
		} else {
			node.value = item;
		}

		node.updateHeight();
		return rebalance(node);
	}

	void delete(int id) {
		root = delete(id, root);
		lookingGlass();
	}

	private static <T> Node<T> leftmostIn(Node<T> node) {
		Node<T> current = node;
		while (current.left != null) {
			current = current.left;
		}
		return current;
	}

	private Node<T> delete(int id, Node<T> node) {
		if (node == null) {
			throw new NoSuchElementException();
		}

		int nodeKey = keyOf.applyAsInt(node.value);

		if (nodeKey == id) {
			if (node.right == null) {
				return node.left;
			}

			node.value = leftmostIn(node.right).value;
			node.right = delete(keyOf.applyAsInt(node.value), node.right);
			node.updateHeight();
			return rebalance(node);
		}

		if (id < nodeKey) {
			node.left = delete(id, node.left);
		} else {
			node.right = delete(id, node.right);
		}
		node.updateHeight();
		return rebalance(node);
	}

	public boolean has(int id) {
		return maybeFind(id).isPresent();
	}

	public T find(int id) {
		return maybeFind(id).orElseThrow(
				() -> new NoSuchElementException());
	}

	public Optional<T> maybeFind(int id) {
		Node<T> current = root;
		while (current != null) {
			int nodeKey = keyOf.applyAsInt(current.value);
			if (id < nodeKey) {
				current = current.left;
			} else if (id > nodeKey) {
				current = current.right;
			} else {
				return Optional.of(current.value);
			}
		}
		return Optional.empty();
	}

	private static <T> void collectPreorder(Node<T> node, Node<T> replaced, Node<T> replacement,
			DynamicArray<String> tokens) {
		if (node == null) {
			tokens.add("-");
			return;
		}
		if (node == replaced) {
			node = replacement;
			replaced = null;
		}
		tokens.add(String.valueOf(node.value));
		collectPreorder(node.left, replaced, replacement, tokens);
		collectPreorder(node.right, replaced, replacement, tokens);
	}

	private void lookingGlass() {
		lookingGlass(null, null);
	}

	private void lookingGlass(Node<T> replaced, Node<T> replacement) {
		if (!verbose) {
			return;
		}

		DynamicArray<String> tokens = new DynamicArray<>();
		collectPreorder(root, replaced, replacement, tokens);
		IO.println("Estado del AVL:\n" + tokens.stream().collect(Collectors.joining(" ")));
	}

	Node<T> rebalance(Node<T> node) {
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

	Node<T> leftRotate(Node<T> node) {
		lookingGlass();
		Node<T> newRoot = node.right;

		node.right = newRoot.left;
		node.updateHeight();

		newRoot.left = node;
		newRoot.updateHeight();

		lookingGlass(node, newRoot);
		return newRoot;
	}

	Node<T> rightRotate(Node<T> node) {
		lookingGlass();
		Node<T> newRoot = node.left;

		node.left = newRoot.right;
		node.updateHeight();

		newRoot.right = node;
		newRoot.updateHeight();

		lookingGlass(node, newRoot);
		return newRoot;
	}
}
