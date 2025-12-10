package implementations;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.io.Serializable;


import utilities.BSTreeADT;
import utilities.Iterator;

/**
 * Implementation of a Binary Search Tree (BST) data structure.
 * This class stores elements in a hierarchical structure where each node
 * has at most two children, with left children containing smaller elements
 * and right children containing larger elements.
 *
 * @param <E> the type of elements stored in this tree, must implement Comparable
 * @author Group 8 
 * @version 1.0
 * @see BSTreeNode
 * @see BSTreeADT
 */
public class BSTree<E extends Comparable<? super E>> implements BSTreeADT<E>, Serializable {
    
    private static final long serialVersionUID = 1L;
    private BSTreeNode<E> root;
    
    /**
     * Constructs an empty Binary Search Tree.
     */
    public BSTree() {
        this.root = null;
    }

    /**
     * Constructs a Binary Search Tree with a single element as the root.
     *
     * @param newRoot the element to store at the root of the tree
     */
    public BSTree(E newRoot) {
        this.root = new BSTreeNode<E>(newRoot, null, null);
    }
    
    /**
     * Returns the node at the root of the Binary Search Tree.
     *
     * @return the root node of the tree
     * @throws NullPointerException if the tree is empty
     */
    @Override
    public BSTreeNode<E> getRoot() throws NullPointerException {
        if (root == null) throw new NullPointerException("root node is null");
        return root;
    }

    /**
     * Recursively calculates the height of a subtree.
     * The height is defined as the number of edges on the longest path
     * from the given node to a leaf.
     *
     * @param node the root of the subtree to measure
     * @return the height of the subtree (0 if node is null)
     */
    private int findHeight(BSTreeNode<E> node) {
        if (node == null) return 0;
        return 1 + Math.max(findHeight(node.getLeftChild()), findHeight(node.getRightChild()));
    }
    
    /**
     * Returns the height of the tree.
     * An empty tree has height 0, a tree with only a root has height 1.
     *
     * @return the height of the tree
     */
    @Override
    public int getHeight() {
        return findHeight(root);
    }
    
    /**
     * Recursively counts the number of descendants of a node.
     * This includes all children, grandchildren, etc.
     *
     * @param node the node to count descendants for
     * @return the total number of descendants of the node
     */
    private int countChildren(BSTreeNode<E> node) {
        if (node == null) return 0;
        int left = (node.getLeftChild() != null) ? 1 + countChildren(node.getLeftChild()) : 0;
        int right = (node.getRightChild() != null) ? 1 + countChildren(node.getRightChild()) : 0;
        return left + right;
    }
    
    /**
     * Returns the number of elements currently stored in the tree.
     *
     * @return the total number of nodes in the tree
     */
    @Override
    public int size() {
        if (root == null) return 0;
        return 1 + countChildren(root);
    }

    /**
     * Checks if the tree is currently empty.
     *
     * @return true if the tree is empty, false otherwise
     */
    @Override
    public boolean isEmpty() {
        return (root == null);
    }

    /**
     * Removes all elements from the tree, making it empty.
     */
    @Override
    public void clear() {
        root = null;
    }

    /**
     * Recursively checks if a subtree contains the specified element.
     *
     * @param node the root of the subtree to search
     * @param entry the element to search for
     * @return true if the element is found in the subtree, false otherwise
     */
    private boolean containsRecursive(BSTreeNode<E> node, E entry) {
        if (node == null) return false;
        
        int comparison = entry.compareTo(node.getValue());
        if (comparison == 0) {
            return true;
        } else if (comparison < 0) {
            return containsRecursive(node.getLeftChild(), entry);
        } else {
            return containsRecursive(node.getRightChild(), entry);
        }
    }
    
    /**
     * Checks if the tree contains the specified element.
     *
     * @param entry the element to search for
     * @return true if the element is found, false otherwise
     * @throws NullPointerException if the element is null
     */
    @Override
    public boolean contains(E entry) throws NullPointerException {
        if (entry == null) throw new NullPointerException("Entry is null");
        return containsRecursive(root, entry);
    }
    
    /**
     * Recursively searches for a node containing the specified element.
     *
     * @param node the root of the subtree to search
     * @param entry the element to search for
     * @return the node containing the element, or null if not found
     */
    private BSTreeNode<E> searchRecursive(BSTreeNode<E> node, E entry) {
        if (node == null) return null;
        
        int comparison = entry.compareTo(node.getValue());
        if (comparison == 0) {
            return node;
        } else if (comparison < 0) {
            return searchRecursive(node.getLeftChild(), entry);
        } else {
            return searchRecursive(node.getRightChild(), entry);
        }
    }
    
    /**
     * Searches for a node containing the specified element.
     *
     * @param entry the element to search for
     * @return the node containing the element, or null if not found
     * @throws NullPointerException if the element is null
     */
    @Override
    public BSTreeNode<E> search(E entry) throws NullPointerException {
        if (entry == null) throw new NullPointerException("Entry is null");
        return searchRecursive(root, entry);
    }

    /**
     * Recursively adds a new element to a subtree.
     *
     * @param node the root of the subtree
     * @param newEntry the element to add
     * @return true if the element was added successfully, false if duplicate
     */
    private boolean addRecursive(BSTreeNode<E> node, E newEntry) {
        int comparison = newEntry.compareTo(node.getValue());
        
        if (comparison == 0) {
            return false;
        } else if (comparison < 0) {
            if (node.getLeftChild() == null) {
                node.setLeftChild(new BSTreeNode<E>(newEntry, null, null));
                return true;
            } else {
                return addRecursive(node.getLeftChild(), newEntry);
            }
        } else {
            if (node.getRightChild() == null) {
                node.setRightChild(new BSTreeNode<E>(newEntry, null, null));
                return true;
            } else {
                return addRecursive(node.getRightChild(), newEntry);
            }
        }
    }
    
    /**
     * Adds a new element to the tree according to the natural ordering
     * established by the Comparable implementation.
     *
     * @param newEntry the element to add to the tree
     * @return true if the element was added successfully, false if duplicate
     * @throws NullPointerException if the element is null
     */
    @Override
    public boolean add(E newEntry) throws NullPointerException {
        if (newEntry == null) throw new NullPointerException("NewEntry is null");
        
        if (root == null) {
            root = new BSTreeNode<E>(newEntry, null, null);
            return true;
        } else {
            return addRecursive(root, newEntry);
        }
    }

    /**
     * Helper function for finding the minimum node in a subtree.
     * The minimum is found by repeatedly following left children.
     *
     * @param node the root of the subtree
     * @return the node containing the minimum element in the subtree
     */
    private BSTreeNode<E> findMin(BSTreeNode<E> node) {
        while (node.getLeftChild() != null) {
            node = node.getLeftChild();
        }
        return node;
    }
    
    /**
     * Helper function for removing the smallest node in a subtree.
     * When the smallest node is found, it is replaced by its right child.
     *
     * @param node the root of the subtree
     * @return the new root of the subtree after removal
     */
    private BSTreeNode<E> removeMinRecursive(BSTreeNode<E> node) {
        if (node.getLeftChild() == null) {
            return node.getRightChild();
        }
        node.setLeftChild(removeMinRecursive(node.getLeftChild()));
        return node;
    }
    
    /**
     * Removes the smallest element from the tree.
     *
     * @return the node containing the removed smallest element, or null if tree is empty
     */
    @Override
    public BSTreeNode<E> removeMin() {
        if (root == null) return null;
        
        BSTreeNode<E> minNode = findMin(root);
        root = removeMinRecursive(root);
        return minNode;
    }
    
    /**
     * Helper function to find the maximum node in a subtree.
     * The maximum is found by repeatedly following right children.
     *
     * @param node the root of the subtree
     * @return the node containing the maximum element in the subtree
     */
    private BSTreeNode<E> findMax(BSTreeNode<E> node) {
        while (node.getRightChild() != null) {
            node = node.getRightChild();
        }
        return node;
    }

    /**
     * Helper function to remove the largest node in a subtree.
     * When the largest node is found, it is replaced by its left child.
     *
     * @param node the root of the subtree
     * @return the new root of the subtree after removal
     */
    private BSTreeNode<E> removeMaxRecursive(BSTreeNode<E> node) {
        if (node.getRightChild() == null) {
            return node.getLeftChild();
        }
        node.setRightChild(removeMaxRecursive(node.getRightChild()));
        return node;
    }
    
    /**
     * Removes the largest element from the tree.
     *
     * @return the node containing the removed largest element, or null if tree is empty
     */
    @Override
    public BSTreeNode<E> removeMax() {
        if (root == null) return null;
        
        BSTreeNode<E> maxNode = findMax(root);
        root = removeMaxRecursive(root);
        return maxNode;
    }

    /**
     * Returns an iterator that traverses the tree in in-order.
     * In-order traversal visits nodes in ascending order (left, root, right).
     *
     * @return an in-order iterator
     */
    @Override
    public Iterator<E> inorderIterator() {
        return new Iterator<E>() {
            private final ArrayList<E> elements = new ArrayList<E>();
            private int index = 0;
            
            /**
             * Recursively copies elements from a subtree to a list in in-order.
             *
             * @param node the root of the subtree
             * @param list the list to copy elements to
             */
            private void inorderCopy(BSTreeNode<E> node, ArrayList<E> list) {
                if (node == null) return;
                inorderCopy(node.getLeftChild(), list);
                list.add(node.getValue());
                inorderCopy(node.getRightChild(), list);
            }
            
            // Initializer block to populate the list when iterator is created
            {
                inorderCopy(root, elements);
            }
            
            /**
             * Checks if there are more elements to iterate over.
             *
             * @return true if there are more elements, false otherwise
             */
            @Override
            public boolean hasNext() {
                return index < elements.size();
            }

            /**
             * Returns the next element in the iteration.
             *
             * @return the next element
             * @throws NoSuchElementException if there are no more elements
             */
            @Override
            public E next() throws NoSuchElementException {
                if (!hasNext()) throw new NoSuchElementException();
                return elements.get(index++);
            }
        };
    }

    /**
     * Returns an iterator that traverses the tree in pre-order.
     * Pre-order traversal visits the root first, then left subtree, then right subtree.
     *
     * @return a pre-order iterator
     */
    @Override
    public Iterator<E> preorderIterator() {
        return new Iterator<E>() {
            private final ArrayList<E> elements = new ArrayList<E>();
            private int index = 0;
            
            /**
             * Recursively copies elements from a subtree to a list in pre-order.
             *
             * @param node the root of the subtree
             * @param list the list to copy elements to
             */
            private void preorderCopy(BSTreeNode<E> node, ArrayList<E> list) {
                if (node == null) return;
                list.add(node.getValue());
                preorderCopy(node.getLeftChild(), list);
                preorderCopy(node.getRightChild(), list);
            }
            
            // Initializer block to populate the list when iterator is created
            {
                preorderCopy(root, elements);
            }

            /**
             * Checks if there are more elements to iterate over.
             *
             * @return true if there are more elements, false otherwise
             */
            @Override
            public boolean hasNext() {
                return index < elements.size();
            }

            /**
             * Returns the next element in the iteration.
             *
             * @return the next element
             * @throws NoSuchElementException if there are no more elements
             */
            @Override
            public E next() throws NoSuchElementException {
                if (!hasNext()) throw new NoSuchElementException();
                return elements.get(index++);
            }
        };
    }

    /**
     * Returns an iterator that traverses the tree in post-order.
     * Post-order traversal visits left subtree, then right subtree, then the root.
     *
     * @return a post-order iterator
     */
    @Override
    public Iterator<E> postorderIterator() {
        return new Iterator<E>() {
            private final ArrayList<E> elements = new ArrayList<E>();
            private int index = 0;
            
            /**
             * Recursively copies elements from a subtree to a list in post-order.
             *
             * @param node the root of the subtree
             * @param list the list to copy elements to
             */
            private void postorderCopy(BSTreeNode<E> node, ArrayList<E> list) {
                if (node == null) return;
                postorderCopy(node.getLeftChild(), list);
                postorderCopy(node.getRightChild(), list);
                list.add(node.getValue());
            }
            
            // Initializer block to populate the list when iterator is created
            {
                postorderCopy(root, elements);
            }

            /**
             * Checks if there are more elements to iterate over.
             *
             * @return true if there are more elements, false otherwise
             */
            @Override
            public boolean hasNext() {
                return index < elements.size();
            }

            /**
             * Returns the next element in the iteration.
             *
             * @return the next element
             * @throws NoSuchElementException if there are no more elements
             */
            @Override
            public E next() throws NoSuchElementException {
                if (!hasNext()) throw new NoSuchElementException();
                return elements.get(index++);
            }
        };
    }
}

