package implementations;

import java.io.Serializable;

/**
 * his class implements the Serializable interface to support object serialization.
 * 
 * @param <E> the type of element stored in the node, must be comparable
 */
public class BSTreeNode<E> implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private E value;
 
	private BSTreeNode<E> leftChild;
	private BSTreeNode<E> rightChild;
 
	/**
	 * 
	 * @param value the element to store in this node
     * @param leftChild the left child node, or null if none
     * @param rightChild the right child node, or null if none
	 * @return
	 */
	public BSTreeNode(E value, BSTreeNode<E> leftChild, BSTreeNode<E> rightChild ) {
		
		super();
		this.value = value;
        this.leftChild = leftChild;
        this.rightChild = rightChild;
	}
	
	/**
	 * 
	 * @return the element stored in this node
	 */
	public E getValue() {
		return value;
	}
	
	/** 
	 * 
	 * @param value the new element to store in this node
	 */
	public void setValue(E value) {
		this.value = value;
	}
	
	/**
	 * 
	 * @return the left child node, or null if no left child exists
	 */
	public BSTreeNode<E> getLeftChild() {
		return leftChild;
	
	}
	
	/**
	 * 
	 * @param leftChild the new left child node
	 */
	public void setLeftChild(BSTreeNode<E> leftChild) {
		this.leftChild = leftChild;
	}
	
	
	/**
	 * 
	 * @return rightChild the new right child node
	 */
	public BSTreeNode<E> getRightChild() {
		return rightChild;
	}
	
	/** 
	 * 
	 * @param rightChild the new right child node
	 */
	public void setRightChild(BSTreeNode<E> rightChild) {
		this.rightChild = rightChild;
	}
 
	/**
	 * 
	 * @return the element stored in this node
	 */
	public E getElement() {
		return value;
	}
}
 