/**
 *
 * AVLTree
 *
 * An implementation of a AVL Tree with
 * distinct integer keys and info
 *
 * 
 */

public class AVLTree

{	/** 
	 * Constants
	 * VIRTUAL: key for virtual node
	 * 
	 * 
	 * 
	 */
	private static final int VIRTUAL = -1;
	private IAVLNode root;
	private int size;
	private IAVLNode max;
	private IAVLNode min;



  /**
   * public boolean empty()
   *
   * returns true if and only if the tree is empty
   * 
   * 
	 * Time complexity: O(1)
   *
   */
  public boolean empty()
  {
    return this.root == null;
  }

 /**
   * public String search(int k)
   *
   * returns the info of an item with key k if it exists in the tree
   * otherwise, returns null
   * 
   * Time complexity: O(log(n))
   */
  public String search(int k)
  {
	  if (this.empty())
	  {
		  return null;
	  }
	  
	return searchRec(this.root, k);
  }
  
  /**
	 * private String search(IAVLNode node, int key)
	 * 
	 * This recursive function returns the info of an item with key k if it exists
	 * in the tree otherwise, returns null.
	 * 
	 * Time complexity: O(logn)
	 * 
	 */

  private String searchRec(IAVLNode node ,int key )
  {

  	if (node.getHeight() == -1)
  	{
  		return null;
  	}

  	if (key == node.getKey())
  	{
  		return node.getValue();
  	}

  	if (key < node.getKey())
  	{
  		return searchRec(node.getLeft(), key);
  	}

  	return searchRec(node.getRight(), key);

  }

  /**
   * public int insert(int k, String i)
   *
   * inserts an item with key k and info i to the AVL tree.
   * the tree must remain valid (keep its invariants).
   * returns the number of rebalancing operations, or 0 if no rebalancing operations were necessary.
   * returns -1 if an item with key k already exists in the tree.
   * 
   * Time complexity: O(logn)
   */
   public int insert(int k, String i)
   {
	  if (empty())
	  {
	  	this.root = new AVLNode(k, i);
	  	this.size += 1;
		updateMinMax();
	  	return 0;
	  }	

	  ///Find where the new node should be inserted

	  IAVLNode currNode = this.root;
	  boolean isrightChild = false; ///Checks where to insert currNode
	  boolean foundParent = false;

	  while (currNode.getKey() != k && !foundParent)
	  {
	  	if (k < currNode.getKey())
	  	{
	  		if (currNode.getLeft().isRealNode())
	  		{
	  			currNode = currNode.getLeft();
	  		}
	  		else // currNode's left child
	  		{
	  			foundParent = true;
	  		}
	  	}
	  	else if (k > currNode.getKey())
	  	{
	  		if (currNode.getRight().isRealNode())
	  		{
	  			currNode = currNode.getRight();
	  		}
	  		else // currNode's right child
	  		{
	  			foundParent = true;
	  			isrightChild = true;
	  		}
	  	}
	  }
	  	/// k already exists
	  	if (!foundParent)
	  	{
	  		return -1;
	  	}
	  	
	  	
	  	
	  	/// Implement the insertion
	  	IAVLNode node = new AVLNode(k, i, currNode);   ///Constructor creates new node and uses it's parent.
	  	
	  	if (isrightChild) /// new node is a right child of it's parent.
	  	{
	  		currNode.setRight(node);
	  		node.setParent(currNode);
	  	} 
	  	
	  	else
	  	{
	  		currNode.setLeft(node);
	  		node.setParent(currNode);
	  		
	  	}
	  	
	  	/// Implement Balance of tree and return #op
	  	
	  	int x = balanceInsert(node, currNode);
	  	this.size = root.getSize();
		updateMinMax();
	  	return x;
	  	
	  	
	  	
	  	

   }	/**
	 * private int balanceInsert(IAVLNode child, IAVLNode parent)
	 * 
	 * This method takes new inserted node: child and parent which is child.getParent()
	 * Returns the number of rebalancing operations neederd to 
	 * maintain a proper AVL tree after insertion.
	 * 
	 * Number of balancing operations included:
	 * 1.Promotions 2.Rotations
	 * 
	 * Method Also calls for updateHeightSize
	 * Which runs from terminal node reached by balanceInsert and updates
	 * Heights and sizes of node until root.
	 *
	 * 
	 * 
	 * Time complexity: O(logn)
	 * 
	 */
   

	  private int balanceInsert(IAVLNode child, IAVLNode parent)
	  {
		
		if (parent == null) {
			return 0;
		}
		
	  	int parentH = parent.getHeight();
	  	int childH = child.getHeight();
	  	int parentNode = parentH - childH; //ranks diff
	  	if (parentNode == 1) // case B (terminal)
	  	{	
	  		updateHeightSize(parent);
	  		return 0;
	  	}
	  	
	  
	  	if (parent.getKey() < child.getKey()) //right child
  		{
	  		int parentOtherChild = parentH - parent.getLeft().getHeight();
	  		if  (parentOtherChild == 1) // Case 1 (non-terminal)
	  		{
	  			parent.setHeight(parentH +1);
	  			parent.updateSize(parent);
	  			return 1 + balanceInsert(parent, parent.getParent());
	  		}
	  		int childLeftChild = childH - child.getLeft().getHeight();
	  		int childRightChild = childH - child.getRight().getHeight();
	  		if (childLeftChild == 2 && childRightChild == 1) //case 2 (terminal)
	  		{
	  			rotateLeft(parent, child);
	  			updateHeightSize(child);
	  			return 1;
	  		}
	  		if (childLeftChild == 1 && childRightChild == 2) //case 3 (terminal)
	  		{
	  			rotateRight(child, child.getLeft());
	  			rotateLeft(parent, parent.getRight());
	  			updateHeightSize(parent.getParent());
	  			return 2;
	  		}
  		}
	  	
	  	else //symmetrical
	  	{
	  		int parentOtherChild = parentH - parent.getRight().getHeight();
	  		if (parentOtherChild == 1) // Case 1 (non-terminal)
	  		{
	  			parent.setHeight(parentH +1);
	  			parent.updateSize(parent);
	  			return 1 + balanceInsert(parent, parent.getParent());
	  		}

	  		int childRightChild = childH - child.getRight().getHeight();
	  		int childLeftChild = childH - child.getLeft().getHeight();
	  		if (childRightChild == 2 && childLeftChild == 1) //case 2 (terminal)
	  		{
	  			rotateRight(parent, child);
	  			updateHeightSize(child);
	  			return 1;
	  		}
	  		if (childRightChild == 1 && childLeftChild == 2) //case 3 (terminal)
	  		{
	  			rotateLeft(child, child.getRight());
	  			rotateRight(parent, parent.getLeft());
	  			updateHeightSize(parent.getParent());
	  			return 2;
	  		}
	  	}
	  	
	  		return 0;  ///Should not get to this point.
	  }
	  
	  /**
		 * private void updateHeightSize(IAVLNode node)
		 * 
		 * This method updates the height and size of a given node,
		 * continues updating until reaching root.
		 * 
		 * Time complexity: O(log(n))
		 */
	  
	 private void updateHeightSize(IAVLNode node)
	 {
			 while (node != null)
			 {
					node.updateHeight(node);
					node.updateSize(node);
					node = node.getParent();
			 }
			 this.size = root.getSize();
				 
		 
	 }
	 
	 /**
		 * private void rightRotate(IAVLNode node, IAVLNode child)
		 * 
		 * This method Applies right rotation as follows:
		 * rightRotate(a , b)
		 * 
		 *            a                  b
		 *           / \                / \
		 *          b   c      =>      d   a  
		 *         / \                    / \
		 *        d   e                  e   c
		 * 
		 * Time complexity: O(1)
		 */

	 private void rotateRight(IAVLNode node, IAVLNode child) {

	  
      IAVLNode childRight = child.getRight();
      
      // if node was the root then child should replace it
      if (root == node)
      {
        root = child;
        child.setParent(null);

      }
      // if node is NOT the root then child should be connected to node's parent
      else
      {
        if (isRight(node))	// node is a right child
          node.getParent().setRight(child);
        else
          node.getParent().setLeft(child);
      }
      
     // connect nodes
     child.setParent(node.getParent());
     child.setRight(node);
     node.setParent(child);
     node.setLeft(childRight);
    if(childRight != null) { 
    childRight.setParent(node); }

   //Update heights and sizes.
    node.updateHeight(node);
    child.updateHeight(child);
    node.updateSize(node);
    child.updateSize(child);


	  }   
   
	 /**
	     * private void leftRotate(IAVLNode node, IAVLNode child)
	     * 
	     * 
	  	 * 
		 * This method Applies left rotation as follows:
		 * leftRotate(a, b)
		 * 
		 *            a					 b
		 *           / \			    / \
		 *          c   b      =>      a   e
		 *             / \			  / \
		 *            d   e          c   d
		 * 
		 * Time complexity: O(1)
		 */

   private void rotateLeft(IAVLNode node, IAVLNode child){

    IAVLNode leftChild = child.getLeft();
    
    // if node was the root then child should replace it
    if (this.root == node){
      this.root = child;
      child.setParent(null);
    }
    
    // if node is NOT the root then child should be connected to node's parent
    else {
      if(isLeft(node)) // node is a left child
        node.getParent().setLeft(child);
      else
        node.getParent().setRight(child);
    }
    
    // connect nodes
    child.setParent(node.getParent());
    child.setLeft(node);
    node.setParent(child);
    node.setRight(leftChild);
    leftChild.setParent(node); 
    
    //Update heights and sizes.
    node.updateHeight(node);
    child.updateHeight(child);
    node.updateSize(node);
    child.updateSize(child);
    


   }
   
   /**
	 * private boolean isRight(IAVLNode node)
	 * 
	 * precondition: node is NOT the root
	 * 
	 * return value :
	 * true  : if node is a right child
	 * false : if node is a left child
	 * 
	 * Time complexity: O(1)
	 */
   
   private boolean isRight(IAVLNode node){
    return node.getParent().getRight() == node;
   }
   
   /**
	 * private boolean isLeft(IAVLNode node)
	 * 
	 * precondition: node is NOT the root
	 * 
	 * return value :
	 * true  : if node is a left child
	 * false : if node is a right child
	 * 
	 * Time complexity: O(1)
	 */

   private boolean isLeft(IAVLNode node){
    return node.getParent().getLeft() == node;
   }
   
   
   
   /**
    * public int delete(int k)
    *
    * deletes an item with key k from the binary tree, if it is there;
    * the tree must remain valid (keep its invariants).
    * returns the number of rebalancing operations, or 0 if no rebalancing operations were needed.
    * returns -1 if an item with key k was not found in the tree.
    * 
    * NOTE: Rebalancing operations include: 
    * 1. Promotions 2. Demotions 3.Rotations, one per rotation.
    * 
    * Time Complexity: O(log(n))
    */

    public int delete(int k)
    {

     if (this.empty()){
       return -1;
     }

     IAVLNode currNode = this.root;
   

     while (currNode.isRealNode() && currNode.getKey() != k)
       {
         if (currNode.getKey() < k)
           {
             currNode = currNode.getRight();
             
           }

         else 
          {
             currNode = currNode.getLeft();
          }
       }
     
     
     /// k does not appear in the tree
     if(!currNode.isRealNode())
     {
       return -1;
     }
      
     
     /// currNode points to the node to be deleted
  
     /// currNode is the root
     /// currNode is root and is leaf
     if (currNode == root && currNode.isLeaf()) {
    	 root = null;
    	 this.size = 0;
    	 updateMinMax();
    	 return 0; 
     }
     
     
     ///Deleting a node with TWO children
     ///Swap node with it's successor
     if (currNode.hasTwoChildren()) {
    	 IAVLNode successor = currNode.findSuccessor();
    	 swapNodes(currNode, successor);
    	 currNode = successor;
     }
     
     
     ///CurrNode is root
     if (currNode == root) {
    	 root = currNode.getChild();
    	 root.setParent(null);
    	 this.size = 0;
    	 updateMinMax();
    	 return 0;
     }
     
     
     ///CurrNode is a leaf OR has ONE child
     IAVLNode grandParent = currNode.getParent();
     IAVLNode child = currNode.getChild();
     if (isRight(currNode)) { /// currNode is a right child of it's parent
    	 child.setParent(grandParent);
    	 grandParent.setRight(child);
    	 
     }
     /// currNode is a left child of it's parent
     else {
    	 child.setParent(grandParent);
    	 grandParent.setLeft(child);
     }
     
     ///Apply balancing Algorithm;
     ///Return number of rotations and demotions
     
    int x = balanceDel(grandParent, child);
    
 	if (root == null){
 		this.size = 0;
 	}
 	else {
 		this.size -= 1;
 	}

    updateMinMax();
    return x;
     
     
     
   }
    
    /**
     * public int balanceDel(IAVLNode parent, IAVLNode child)
     * 
     * After deleting the node, does required rebalancing operations in order
     * to maintain a proper AVL tree.
     * parent: Parent of deleted node.
     * Child: Child of deleted node.
     * returns number of rebalancing operations: 
     * Rotations, demotions, promotions.
     *
     * 
     * Time complexity: O(log(n))
     */
    
    public int balanceDel(IAVLNode parent, IAVLNode child)
    {
    	
    	if (parent == null) {
    		return 0;
    	}
    	int pHeight = parent.getHeight(); ///parent height
    	int cHeight = child.getHeight();  ///child height
    	int pcHeightDiff = pHeight - cHeight;   ///height difference
    	
    	if (isLeft(child)) 
    	{
    		int ocHeight = parent.getRight().getHeight(); ///Other child height
    		int pocHeightDiff = pHeight - ocHeight; 
    		if (pcHeightDiff == 2 && pocHeightDiff == 1) {///Terminal case WAVL slide 37
    			updateHeightSize(parent);
    			return 0;
    		}
    		else if (pcHeightDiff == 2 && pocHeightDiff == 2) { /// CASE 1 - Demote parent slide 39
    			parent.setHeight(pHeight - 1);
    			parent.updateSize(parent);
    			return 1 + balanceDel(parent.getParent(), parent);
    		}
    		else if (pcHeightDiff == 3 && pocHeightDiff == 1)
    		{
    			
    			int lgcHeight = parent.getRight().getLeft().getHeight(); /// leftGrandChild height
    			int rgcHeight = parent.getRight().getRight().getHeight(); /// rightGrandChild height
    			
    			if (ocHeight - lgcHeight == 1 && ocHeight - rgcHeight  == 1) 
    			  {
    			      /* CASE 2  - TEMINAL CASE
    		 	      Left rotation, which does right child promotion and parent demotion */
    			      rotateLeft(parent, parent.getRight());
    			      updateHeightSize(parent.getRight());
    				  return 1;   ///
    			  }
    			else if (ocHeight - lgcHeight == 2 && ocHeight - rgcHeight == 1) 
    			  {	/// Case 3
    			 	/// Rotate left and demote parent.
    				///Left rotation demotes parents twice.
    				rotateLeft(parent, parent.getRight());
    				return 1 + balanceDel(parent.getParent().getParent(), parent.getParent());
    			  }
    			
    			else if (ocHeight - lgcHeight == 1 && ocHeight - rgcHeight == 2)  /// Case 4 -last case 
    			  {
    				rotateRight(parent.getRight(), parent.getRight().getLeft());
    				rotateLeft(parent, parent.getRight());
    				return 2 + balanceDel(parent.getParent().getParent(), parent.getParent());
    			  }
    			
    		}
    	}
    	 
    	
    		else if (isRight(child))   ///Symmetrical 
    		{
    		int ocHeight1 = parent.getLeft().getHeight(); ///Other child height
    		int pocHeightDiff1 = pHeight - ocHeight1; 
    		if (pcHeightDiff == 2 && pocHeightDiff1 == 1) {///Terminal case WAVL slide 37
    			updateHeightSize(parent);
    			return 0;
    		}
    		else if (pcHeightDiff == 2 && pocHeightDiff1 == 2) { /// CASE 1 - Demote parent slide 39
    			parent.setHeight(pHeight - 1);
    			parent.updateSize(parent);
    			return 1 + balanceDel(parent.getParent(), parent);
    		}
    		else if (pcHeightDiff == 3 && pocHeightDiff1 == 1)
    		{
    			
    			int rgcHeight = parent.getLeft().getRight().getHeight(); /// rightGrandChild height
    			int lgcHeight = parent.getLeft().getLeft().getHeight(); /// leftGrandChild height
    			
    			if (ocHeight1 - rgcHeight == 1 && ocHeight1 - lgcHeight  == 1) 
    			  {
    			      /* CASE 2  - TEMINAL CASE
    		 	      Right rotation, right child promotion and parent demotion */
    			      rotateRight(parent, parent.getLeft());
    			      updateHeightSize(parent);
    				  return 1;
    			  }
    			else if (ocHeight1 - rgcHeight == 2 && ocHeight1 - lgcHeight == 1) 
    			  {	/// Case 3
    			 	/// Rotate right and demote parent twice
    				rotateRight(parent, parent.getLeft());
    				return 1 + balanceDel(parent.getParent(), parent);
    			  }
    			
    			else if  (ocHeight1 - rgcHeight == 1 && ocHeight1 - lgcHeight == 2)  /// Case 4 -last case 
    			  {
    				rotateLeft(parent.getLeft(), parent.getLeft().getRight());
    				rotateRight(parent, parent.getLeft());
    				return 2 + balanceDel(parent.getParent().getParent(), parent.getParent());
    			  }
    			
    		}
    	
    	 }
    	
    	return 0;   //// should nor reach here.
    	}
    	
    
    /**
     * 
     * private void swapNodes(IAVLNode node1, IAVLNode node2)
     * 
     * This method swaps node1 & node2
     * node1.key <=> node2.key
     * node1.value <=> node2.value
     * 
     * Time complexity: O(1)
     */
    
    private void swapNodes(IAVLNode node1, IAVLNode node2) {
    	
    	int tmpKey = node1.getKey();
    	String tmpValue = node2.getValue();
    	
    	node1.setKey(node2.getKey());
    	node1.setValue(node2.getValue());
    	
    	node2.setKey(tmpKey);
    	node2.setValue(tmpValue);
    }

    
    /**
     * public int getRoot()
     *
     * Returns the root AVL node, or null if the tree is empty
     *
     * precondition: none
     * postcondition: none
     * 
     * Time Complexity: O(1)
     */
    
   
   public IAVLNode getRoot()
   {
	   return root;
   }

	/**
	* private void updateMinMax()
	* updates minimum and maximum nodes in tree. 
	* If empty returns null
	*
	*Time Complexity: O(log(n))
	*/

	private void updateMinMax(){

	if (this.empty())
	   {
		   min = null;
		   max = null;
	   }
	
	else {
	
	   IAVLNode currNode = this.root;
	   while(currNode.getLeft().isRealNode()) // while(currNode.isRealNode()) if virtual isn't global
	   {
		   currNode = currNode.getLeft();
	   }
	   min =currNode;
		
	  IAVLNode currNode1 = this.root;
	   while(currNode1.getRight().isRealNode()) 
	   {
		   currNode1 = currNode1.getRight();
	   }
	   max = currNode1;
	   
	}
	   


	}

   /**
    * public String min()
    *
    * Returns the info of the item with the smallest key in the tree,
    * or null if the tree is empty
    * 
    * Time complexity: O(1)
    */
   public String min()
   {	
	if (min == null) 
	{
	  return null;
	}
	  
	return min.getValue();
   }

   /**
    * public String max()
    *
    * Returns the info of the item with the largest key in the tree,
    * or null if the tree is empty
    * 
    * Time complexity O(1)
    */
   public String max()
   {
	if (max == null) 
	{
	  return null;
	}
	  
	return max.getValue();
   }
        

   /**
	 * public int[] keysToArray()
	 *
	 * Returns a sorted array which contains all keys in the tree, or an empty array
	 * if the tree is empty.
	 * 
	 * Time complexity: O(n)
	 */
  public int[] keysToArray()
  {
	  if (empty())
	  	{
		  return new int[0];
	  	}
	  int[] arr = new int[this.size];
      keysToArrayRec(arr, this.root, 0);
      
      return arr;
  }
  
  
  /**
	 * private void keysToArrayRec(int[] arr, IAVLNode node, int i)
	 * 
	 * This recursive in-order traversal writes the keys of the sub-tree
	 * whose root is node to arr starting from index i.
	 * 
	 * Time complexity: O(n)
	 */
  private void keysToArrayRec(int[] arr, IAVLNode node, int i)
  {
	  if (node.getHeight() == -1) 
	  {
		  return;
	  }
	  
	  keysToArrayRec(arr, node.getLeft(), i);
	  arr[node.getLeft().getSize() + i] = node.getKey();
	  keysToArrayRec(arr, node.getRight(), node.getLeft().getSize() + i + 1);
	  
  }

  /**
	 * public String[] infoToArray()
	 *
	 * Returns an array which contains all info in the tree, sorted by their
	 * respective keys, or an empty array if the tree is empty.
	 * 
	 * Time complexity: O(n)
	 */
  public String[] infoToArray()
  {		
	  	if (empty())
	  		{
	  			return new String[0];
	  		}
        String[] arr = new String[this.size];
        infoToArrayRec(arr, this.root, 0);
        return arr;
  }
  
  /**
	 * private void infoToArrayRec(int[] arr, IAVLNode node, int i)
	 * 
	 * This recursive in-order traversal writes the keys of the sub-tree
	 * whose root is node to arr starting from index i.
	 * 
	 * Time complexity: O(n)
	 */
  
  private void infoToArrayRec(String[] arr, IAVLNode node, int i)
  {
	  if (node.getHeight() == -1) 
	  {
		  return;
	  }
	  infoToArrayRec(arr, node.getLeft(), i);
	  arr[node.getLeft().getSize() + i] = node.getValue();
	  infoToArrayRec(arr, node.getRight(), node.getLeft().getSize() + i + 1);
  }

   /**
    * public int size()
    *
    * Returns the number of nodes in the tree.
    *
    * precondition: none
    * postcondition: none
    * 
    * Time complexity: O(1)
    */
   public int size()
   {
	   return this.size; 
   }
   
     /**
    * public string split(int x)
    *
    * splits the tree into 2 trees according to the key x. 
    * Returns an array [t1, t2] with two AVL trees. keys(t1) < x < keys(t2).
	  * precondition: search(x) != null
    * postcondition: none
    * 
    * Join runs in worse case O(log(n))
    * In worse case there are log(n) splits therefor
    * 
    * Time complexity: O(log(n)*log(n))
    */   
   public AVLTree[] split(int x)
   {
	
	   
	/// Find the splitter node, extract it's left and right subtrees.
    IAVLNode splitter = searchNode(x);		
    AVLTree bigger = new AVLTree();
    bigger.root = splitter.getRight();
    bigger.root.setParent(null);
    AVLTree smaller = new AVLTree();
    smaller.root = splitter.getLeft();
    smaller.root.setParent(null);
    
    

    while (splitter.getParent() != null){

      if (splitter.getParent().getKey() < splitter.getKey())
       {

        splitter = splitter.getParent();
        IAVLNode lonelySplitter = new AVLNode(splitter.getKey(), splitter.getValue()); ///Create a root node from splitter for join operation.
        
        AVLTree joinSmaller = new AVLTree();
        joinSmaller.root = splitter.getLeft();
        joinSmaller.root.setParent(null);
        smaller.join(lonelySplitter, joinSmaller);


       
        
       }

      else if  (splitter.getParent().getKey() > splitter.getKey())
       {
        splitter = splitter.getParent();
        IAVLNode lonelySplitter = new AVLNode(splitter.getKey(), splitter.getValue()); ///Create a root node from splitter for join operation.
        AVLTree joinBigger = new AVLTree();
        joinBigger.root = splitter.getRight();
        joinBigger.root.setParent(null);
        bigger.join(lonelySplitter, joinBigger);
   

        
        
       }


    }
    
   

    AVLTree [] result = new AVLTree[2];
    if (smaller.getRoot().getHeight() == -1) {   /// Splitting minimum or maximum results in one empty tree.
    	smaller = new AVLTree();
    }
    if (bigger.getRoot().getHeight() == -1) {
    	bigger = new AVLTree();
    }
    
    result[0] = smaller;
    result[1] = bigger;
    
	   return result; 
   }
   
   
   /**
    * public join(IAVLNode x, AVLTree t)
    *
    * joins t and x with the tree. 	
    * Returns the complexity of the operation (rank difference between the tree and t)
	  * precondition: keys(x,t) < keys() or keys(x,t) > keys()
    * postcondition: none
    * 
    * Time Complexity: O(log(n)) more specifically O( |height(this) - height(t)| + 1)
    * 
    */
   
   
   public int join(IAVLNode x, AVLTree t)
   {
	if (this.empty() && t.empty()) /// If both given AVLTrees are empty.
	{	
		AVLTree leaf = new AVLTree();
		leaf.root = x;
		this.size += 1;
		return 0;
	}
	
	/// if one of the trees is empty perform regular insertion and return rebalancing operations.
	else if (this.empty() && !t.empty()) ///
	{
		t.insert(x.getKey(), x.getValue());  
		root = t.root;
		return t.getRoot().getHeight(); /// returns height of none empty tree. 
		
	}
	
	else if (!this.empty() && t.empty())
	{
		this.insert(x.getKey(), x.getValue()); /// returns number of rebalancing operations.
		return this.getRoot().getHeight(); 
		
	}
	
	///None of the Trees are empty. Perform Joining Algorithm.
	
	else 
	{
		if (this.root.getKey() > t.root.getKey()) /// CASE 1 
		{	
			/// In case of VIRTUAL node, perfom regular insertion.
			if (t.root.getKey() == VIRTUAL) {
				return this.insert(x.getKey(), x.getValue());
				
			}
			/// Three sub-cases depending on the height of the trees.
			else if (this.root.getHeight() == t.root.getHeight()) ///Trees of same height.
				{	
					IAVLNode root1 = this.getRoot();
					IAVLNode root2 = t.getRoot();
					x.setRight(root1);
					x.setLeft(root2);
					root1.setParent(x);
					root2.setParent(x);
					x.updateHeight(x); ///Update height and size of new root.
					x.updateSize(x);
					root = x;
					return 0;
					
				}
			
			
			else if (this.root.getHeight() > t.root.getHeight()) 
			  {
				int heightDiff = this.root.getHeight() - t.root.getHeight();
				IAVLNode currNode = this.root; 
				for (int i = 0; i < heightDiff; i++)
				   {
					  if (currNode.getLeft().isRealNode())  /// Check if we can go left.
					  	{  currNode = currNode.getLeft(); ///Get to the right node with rank <= height t Tree.
					  		
					  	}
					  else  ///Reached the parent of a VIRTUAL NODE
					  	{
						  preformAdjust(x, currNode, t.root, currNode.getLeft(), true);
						  x.updateHeight(x);
						  x.updateSize(x);
						  preformBalance(x, x.getLeft(), x.getRight());
						  updateHeightSize(x);
						  
						  
							
						  return heightDiff; /// Returns rank difference.
						  
					  	}
					 
				   }
				
				preformAdjust(x, currNode.getParent(), t.root, currNode, true);
				x.updateHeight(x);
				x.updateSize(x);
				preformBalance(x, x.getLeft(), x.getRight());
				updateHeightSize(x);
				
				
				return heightDiff; /// Return number of rebalancing operations
			  }	
			else if (this.root.getHeight() < t.root.getHeight()) /// higher tree with the smaller keys.
			  { 
				int heightDiff = t.root.getHeight() - this.root.getHeight();
				IAVLNode currNode = t.root;
				for (int i = 0; i < heightDiff; i++)
				   {
					  if (currNode.getRight().isRealNode())
					  	{
						  currNode = currNode.getRight(); ///Other side, get to the correct node with rank <= height of this Tree.
					  	}
					  else 
					  	{
						 preformAdjust(x, currNode, currNode.getRight(), this.root, false); 
						
						 this.root = t.root;  ///Join happened on t tree.
						 x.updateHeight(x);
						 x.updateSize(x);
						 preformBalance(x, x.getLeft(), x.getRight());
						 updateHeightSize(x);
						 
						 
						 return heightDiff; /// Returns rank difference
						 
					  	}
				   }
				
				preformAdjust(x, currNode.getParent(), currNode, this.root, false);
				this.root = t.root;
				x.updateHeight(x);
				x.updateSize(x);
				preformBalance(x, x.getLeft(), x.getRight());
				updateHeightSize(x);
				
				return heightDiff; /// Returns rank difference
			  }
		}
		
		
		
		else if (this.root.getKey() < t.root.getKey()) /// CASE 2 - SYMMETRICAL
		{	
			
			if (this.root.getKey() == VIRTUAL) {
				int balance = t.insert(x.getKey(), x.getValue());
				root = t.root;
				return balance;
			}
			/// Three cases like above
			/// height of t Tree equals this tree
			
			if (this.root.getHeight() == t.root.getHeight()) ///Trees of same height.
			{	
				IAVLNode root1 = this.getRoot();
				IAVLNode root2 = t.getRoot();
				x.setRight(root2);
				x.setLeft(root1);
				root1.setParent(x);
				root2.setParent(x);
				x.updateHeight(x);///Update x height to be correct, not default zero.
				x.updateSize(x);
				root = x;
				return 0; ///Returns rank difference
				
			}
			
			/// height of t Tree is bigger than this tree
			else if (this.root.getHeight() < t.root.getHeight())
			  {
				int heightDiff = t.root.getHeight() - this.root.getHeight();
				IAVLNode currNode = t.root;
				for (int i = 0; i < heightDiff; i++)
				   {
					  
					if (currNode.getLeft().isRealNode()) {
					  currNode = currNode.getLeft(); ///Get to the right node with rank <= height of this Tree.
					}
					else {  ///Case where the left node is virtual. 
						
						preformAdjust(x, currNode, this.root, currNode.getLeft(), true);
						this.root = t.root;
						x.updateHeight(x);
						x.updateSize(x);
				
						preformBalance(x, x.getLeft(), x.getRight());
						updateHeightSize(x);
						
						
						return heightDiff; /// Returns rank difference
						
					}
				   }
				
				preformAdjust(x, currNode.getParent(), this.root, currNode, true);
				this.root = t.root;
				x.updateHeight(x);
				x.updateSize(x);
		
				preformBalance(x, x.getLeft(), x.getRight());
				updateHeightSize(x);
				
				return heightDiff; /// Returns rank difference
				
			  }
			else if (this.root.getHeight() > t.root.getHeight()) /// higher tree with the smaller keys.
			  { 
				int heightDiff = this.root.getHeight() - t.root.getHeight();
				IAVLNode currNode = this.root;
				for (int i = 0; i < heightDiff; i++)
				   {
					  if (currNode.getRight().isRealNode()) {
					  currNode = currNode.getRight(); ///Other side, get to the correct node with rank <= height of this Tree.
					  }
					  else 
					  	{
						  preformAdjust(x, currNode, currNode.getRight(), t.root, false);
						  updateHeightSize(x);
						  x.updateHeight(x);
						  x.updateSize(x);
						  preformBalance(x, x.getLeft(), x.getRight());
						  updateHeightSize(x);
						  return heightDiff; /// Returns rank difference
					  	}
				   }
				
				preformAdjust(x, currNode.getParent(), currNode, t.root, false);
				x.updateHeight(x);
				x.updateSize(x);
				preformBalance(x, x.getLeft(), x.getRight());
				updateHeightSize(x);
				
				return heightDiff; /// Returns rank difference
			  }
			
		}
		
	}
	
	return 0;
    
	
	
   }
   
   /**
    * public void preformAdjust(IAVLNode node, IAVLNode pNode, IAVLNode lNode, IAVLNode rNode, Boolean isOnRight) 
    *
    * Connects:
    * 1-node on the right with rNode
    * 2-node on the left with rNode
    * 3-node's parent becomes pNode
    * 4-pNode is connected with node depending on the isOnRight boolean
    * 
    * Time Complexity: O(1) 
    * 
    */

   public void preformAdjust(IAVLNode node, IAVLNode pNode, IAVLNode lNode, IAVLNode rNode, Boolean isOnRight) 
    {
	    if (isOnRight) /// Tells us where the higher tree is (tree with bigger keys is always on the right).
	    {	
	    	rNode.setParent(node);  
	    	node.setParent(pNode);
	    	pNode.setLeft(node);   /// Higher tree is on right, there for connection with smaller key of node is on the left
	    	node.setRight(rNode);
	    	node.setLeft(lNode);
	    	lNode.setParent(node);	
	    }
	    
	    else /// Higher tree is the one with the smaller keys (On the left).
	    {	
	    	lNode.setParent(node);
	    	
	    	node.setParent(pNode);
	    	pNode.setRight(node);  ///  Higher tree is on left, there for connection with bigger key of node is on the left
	    	node.setRight(rNode);
	    	node.setLeft(lNode);
	    	rNode.setParent(node);
	    }
	    
	    node.updateHeight(node);
	    node.updateSize(node);
	    pNode.updateHeight(pNode);
	    pNode.updateSize(pNode);
	    
	    
    }
   
   /**
	 * public IAVLNode searchNode(int k)
	 * 
	 * looks for node with key == k
	 * precondition node with given key is in tree.
	 * 
	 * Time complexity: O(logn)
	 * 
	 */
   public IAVLNode searchNode(int k)
   		{
	   	return searchRecNode(this.root, k);
   		}
   
   /**  private IAVLNode searchRecNode(IAVLNode node ,int key )
    * This recursive function returns the node with matching key.
	 * If tree is empty, returns null.
	 * 
	 * Time complexity: O(logn)
	 */

   private IAVLNode searchRecNode(IAVLNode node ,int key )
   {

   		if (this.root == null)
   		 {	
   			return null;
   		 }

   		if (key == node.getKey())
   		 {
   			return node;
   		 }

   		if (key < node.getKey())
   		 {
   			return searchRecNode(node.getLeft(), key);
   		 }

     return searchRecNode(node.getRight(), key);
   }
   
   /** public void preformBalance(IAVLNode parent, IAVLNode lchild, IAVLNode rchild)
    * 
    * This function is called in join method. Determines which balancing procedure is required
    *  after joining the two AVL trees, calls appropriate balancing functions.
    *  Continues verifying validity of sub AVL trees until reaching the root.
    *  
    * 
	* 
	* 
	* Time complexity: O(logn)
	*/
   
   public void preformBalance(IAVLNode parent, IAVLNode lchild, IAVLNode rchild){
	   
	   if (parent == null) {   /// if parent is null do nothing, void function returns nothing.
		   
	   }

	   else if (parent != null) {
		if (lchild.isRealNode()) {
			lchild.updateHeight(lchild);}
		if (rchild.isRealNode()) {
			rchild.updateHeight(rchild);}
		
	   parent.updateHeight(parent);
		   
	   int i = determineBalance(parent);
	   
	   if (i == 0) 
	   {
		 if(parent.getParent() != null)
		   {
			 preformBalance(parent.getParent(), parent.getParent().getLeft(), parent.getParent().getRight() ); ///Go up and continue checking
		   }
		   else { /// in this case don't continue balancing. Reaches root. Void function does nothing.
			   
		}
		    
	   }
	   else if (i == 1)
	   {
		   balanceInsert(parent, lchild);
	   }
	   else if (i == 2)
	   {
		   balanceInsert(parent, rchild);
	   }
	   else if (i == 3)
	   {
		   balanceInsert(parent, lchild);
	   }
	   else if (i == 4)
	   {
		   balanceInsert(parent, rchild);
	   }
	   else if (i == 5)
	   {
		   balanceDel(parent, lchild);
	   }
	   else if (i == 6)
	   {
		   balanceDel(parent, lchild);
		   
	   }
	   else if (i ==7) {
		   balanceDel(parent, rchild);
		   
	   }
	   else { /// Special balancing case, required different approach.
		   if(parent.getHeight() - rchild.getHeight() == 4) 
		   	{
			   this.rotateRight(parent,lchild);
			   preformBalance(lchild.getParent(), lchild.getParent().getLeft(), lchild.getParent().getRight() ); ///Go up the tree
		   	}
		   else {
			   this.rotateLeft(parent,rchild);
			   preformBalance(rchild.getParent(), rchild.getParent().getLeft(), rchild.getParent().getRight() );
		   } 
		   
	   }
	   }
   }
	   
   /** 	public int determineBalance(IAVLNode node)
    * 
    * This function is called determines which rebalancing case is required. D
    * Is called by preformBalance function and returns and indicator integer.
    * 
	 * 
	 * 
	 * Time complexity: O(1)
	 */
   
   
   public int determineBalance(IAVLNode node) {
	  
	   int lHeightDiff = node.getHeight() - node.getLeft().getHeight();
	   int rHeightDiff = node.getHeight() - node.getRight().getHeight();
	   
	   if (lHeightDiff == 1 && rHeightDiff == 1 ) /// Balanced after insert
	   {
		   return 0;
	   }
	   else if((lHeightDiff + rHeightDiff) == 3)
		   	{
		   			return 0;	
		   	}
	   
	   else if((lHeightDiff + rHeightDiff) == 1) ///Unbalanced, use balanceInsert
	   {	if (lHeightDiff == 0) { ///left side
		   		return 1;
		   		}
	  		else {				///right side
	  			return 2;
	  		}
		   
	   }
	   else if((lHeightDiff + rHeightDiff) == 2) ///Unbalanced, use balanceInsert
	   {
		   if (lHeightDiff == 0) {	 ///left side
		   		return 3;
		   		}
	  		else {
	  			return 4; /// right side
	  		}
	   }
	   else if((lHeightDiff + rHeightDiff) == 4) ///Unbalanced, use balanceDel
	   {
		   if (lHeightDiff == 2 || rHeightDiff == 2) {  /// a two two situation
		   		return 5;
		   		}
	  		else {
	  			if (lHeightDiff == 3) { ///left 
			   		return 6;
			   		}
		  		else {
		  			return 7; /// right
		  		}
	  		}
	   }
	   else if( (lHeightDiff + rHeightDiff) == 5) ////Special case
			{
				return 8;
			}
	   return 0;
	  
	   
   }



	/**
	   * public interface IAVLNode
	   * ! Do not delete or modify this - otherwise all tests will fail !
	   */
	public interface IAVLNode
	{	
		public int getKey(); //returns node's key (for virtuval node return -1)
		public String getValue(); //returns node's value [info] (for virtuval node return null)
		public void setLeft(IAVLNode node); //sets left child
		public IAVLNode getLeft(); //returns left child (if there is no left child return null)
		public void setRight(IAVLNode node); //sets right child
		public IAVLNode getRight(); //returns right child (if there is no right child return null)
		public void setParent(IAVLNode node); //sets parent
		public IAVLNode getParent(); //returns the parent (if there is no parent return null)
		public boolean isRealNode(); // Returns True if this is a non-virtual AVL node
    public void setHeight(int height); // sets the height of the node
    public int getHeight(); // Returns the height of the node (-1 for virtual nodes)
    	
    //Added by us
    public void updateHeight(IAVLNode node);
    public int getSize();
    public void updateSize(IAVLNode node);
    public void setSize(int size);
	public IAVLNode findSuccessor();
	public boolean isLeaf();
	public boolean hasTwoChildren();
	public void setKey(int key);
	public void setValue(String value);
	public IAVLNode getChild();
	
	}

   /**
   * public class AVLNode
   *
   * If you wish to implement classes other than AVLTree
   * (for example AVLNode), do it in this file, not in 
   * another file.
   * This class can and must be modified.
   * (It must implement IAVLNode)
   */
  public class AVLNode implements IAVLNode
  {

  		private String value;
  		private int key;
  		private IAVLNode left;
  		private IAVLNode right;
  		private IAVLNode parent;
  		private int size;
  		private int height;


  		public AVLNode(int k, String i, IAVLNode p)
		{
			this.value = i;
  			this.key = k;
  			this.parent = p;
  			this.size = 1;
  			this.left = new AVLNode();
  			this.right = new AVLNode();
  			this.left.setParent(this);
  			this.right.setParent(this);
		}
  		
  		public AVLNode(int k, String i) //for root
  		{
  			this.key = k;
  			this.value = i;
  			this.parent = null;
  			this.height = 0;
  			this.size = 1;
  			this.left = new AVLNode();
  			this.right = new AVLNode();
  			this.left.setParent(this);
  			this.right.setParent(this);
  		}
  		
  		public AVLNode() //for VIRTUAL
  		{	
  			this.value = null;
  			this.parent = null;
  			this.key = VIRTUAL;
  			this.height = -1;
  		}
		
		public int getKey()
		{
			return this.key; 
		}
		
		public String getValue()
		{
			return this.value; 
		}
		public void setLeft(IAVLNode node)
		{
			this.left = node; 
		}
		
		public IAVLNode getLeft()
		{
			return this.left; 
		}
		
		public void setRight(IAVLNode node)
		{
			this.right = node; 
			
		}
		
		public IAVLNode getRight()
		{
			return this.right; 
		}
		
		public void setParent(IAVLNode node)
		{
			this.parent = node; 
		}
		
		public IAVLNode getParent()
		{
			return parent; 
		}
		
		
		
		
		// Returns True if this is a non-virtual AVL node
		public boolean isRealNode()
		{
			return this.key != VIRTUAL; 
		}
		
    	public void setHeight(int height) ///get back to this
    	{
      		this.height = height; 
    	}
    	
    	public int getHeight()
    	{
      		return this.height;
    	}

      	/// Added by me
    	/// didn't use it yet be kept anyway :)

      	public void updateHeight(IAVLNode node)
      	{

      		int rHeight = right.getHeight();
      		int lHeight = left.getHeight();

      		node.setHeight(Math.max(rHeight, lHeight) + 1);
      	}

      	public void setSize(int size)
      	{
      		this.size = size;
      	}
      	public int getSize()
      	{
      		return this.size;
      	}
  
      	
      	
      	public void updateSize(IAVLNode node){

      		int rSize = right.getSize();
      		int lSize = left.getSize();

      		node.setSize(rSize + lSize + 1);

      	}
      	
      	/// NEW ADDITIONS FOR DELETE 7/12
      	
      	public boolean isRoot() {
      		return this.getParent() == null;
      	}
      	
      	public boolean isLeaf() {
      		return !right.isRealNode() && !left.isRealNode();
      	}
      	
      	public boolean hasTwoChildren() {
      		return right.isRealNode() && left.isRealNode();
      	}
      	
      	public IAVLNode getChild() {
      		
      		if (right.isRealNode()) {
      			return right;
      		}
      		return left;
      	}
      	  	
      	public void setKey(int key) {
      		
      		this.key = key;
      	}
      		
        public void setValue(String value) {
        	
        	this.value = value;
        }
      	
      	public IAVLNode findSuccessor()
      	{
      		
      		if (this.isRoot() && this.isLeaf())
      		{
      			return null;
      		}
      		
      		
      		IAVLNode pointerA = right;
      		IAVLNode pointerB = this.getParent();
      		
      		///Successor is in the right Subtree
      		if (pointerA.isRealNode())
      		 { while(pointerA.getLeft().isRealNode())
      		    {
      			 pointerA = pointerA.getLeft();
      		    }
      		 return pointerA;
      		 }
      		
      		///Successor is parent
      		else if (pointerB == null)
      			{	
      			 return null;
      			}
      		else if (pointerB != null)
      		{
      			
      			if (pointerB.getLeft() == this)
      				{ 
      					return pointerB;
      				}
      		///Successor is up the tree  
      		else
      			{	
      		 
      		   while (pointerB.getParent() != null && pointerB.getParent().getLeft() != pointerB)
      		   		{
      			    pointerB = pointerB.getParent();
      		   		}
      			if (pointerB.getParent() == null)
      				{
      				return null;
      				}
      		   return pointerB.getParent(); 
	      		 }
	      		 
      		}
      	return null;
      	
      	}
  }
}
 
  
  
      	
  


      	

      	
  

  

