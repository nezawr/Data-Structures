/**
 * FibonacciHeap
 *
 * An implementation of fibonacci heap over integers.
 *
 *  Authors :
 *                  Nezar Aburas
 *         Username: nezaraburas, ID: 209010644
 *
 *                  Michael Naaman
 *          Username: naaman, ID: 209231893
 *
 *
 */
import java.util.*; 

public class FibonacciHeap
{	
	
	private HeapNode mMin = null; //Pointer to the minimum element in the heap.
	
	private HeapNode mStart = null; //Pointer to the first tree in heap.
	
	private int mSize = 0; // Size of the heap.
	
	private int marked; // Number of marked nodes.
	
	private int totalLinks; // Number of linking operations since the start of the program.
	
	private int totalCuts; // Number of cut operations since the start of the program.
	
	private static FibonacciHeap heap = new FibonacciHeap();
	
	
   /**
    * public boolean isEmpty()
    *
    * precondition: none
    * 
    * The method returns true if and only if the heap
    * is empty.
    *   
    */
	
	///OK
    public boolean isEmpty()
    {
    	return mMin == null; 
    }
		
   /**
    * public HeapNode insert(int key)
    *
    * Creates a node (of type HeapNode) which contains the given key, and inserts it into the heap. 
    */
    
    //OK
    public HeapNode insert(int key)
    {    
    	/* Create the entry object, which is a circularly-linked list of length
         * one.
         */
    	HeapNode result = new HeapNode(key);
    	
    	HeapNode currentmMin = mMin;
    	
    	/* Merge this singleton list with the tree list. */
    	mMin = mergeLists(result, mStart);
    	
    	/* Update start of heap */
    	
    	mStart = result;
    	 
    	/* Update pointer to minimal node */
    	if (currentmMin != null) {
    		
    		mMin = (currentmMin.getKey() < mMin.getKey()? currentmMin : mMin);
    	
    	}
    	
    	/* Increase the size of the heap. */
    	mSize ++;
    	
    	/* Return the reference to the new element. */
    	return result; 
    }
    
    
    private void insert2(HeapNode source)
    {
    	HeapNode result = new HeapNode(source);
    	mMin = mergeLists(result, mMin);
    	mStart = result;
    	mSize ++;
    }

   /**
    * public void deleteMin()
    *
    * Delete the node containing the minimum key.
    *
    */
    
    
    // OK
    public void deleteMin()
    {
    	if (isEmpty()) {
    		return;
    	}
    	
    	/* Keep pointer to mMin for future ahead */
    	HeapNode minElem = mMin;
    	
    	/* Delete size of heap */
    	--mSize;
    	
    	/* Getting rid of the minimal nodes requires us to consider two cases:
    	 * Case one:
    	 * The minimal node is the only node in the root list. Update Root list to null.
    	 * Case Two:
    	 * Otherwise, if it's not the only one, connect the minimal node previous and next nodes together to remove it from list.
    	 * then arbitrarily reassign the min.
    	 */
    	
    	if (mMin.mNext.equals(mMin)) { //Case one
    		mMin = null;
    	}
    	else {		//Case Two
    		
    		/* If minimal node is also the first one */
    		
    		if (mMin.equals(mStart)) {
    			mStart = mMin.mNext;
    		}
    		mMin.mPrev.mNext = mMin.mNext;
    		mMin.mNext.mPrev = mMin.mPrev;
    		mMin = mMin.mNext; // Arbitrary element of the root list.
    	}
    	
    	/*	Go over minimal's elements children, update their parent pointer to null
    	 *  since they will join the root list.
    	 * 
    	 */
    	
    	if (minElem.mChild != null) { // Has children.
    		
    		/* Keep track of first visited node */
    		HeapNode curr = minElem.mChild;
    		
    		do {
    			curr.mParent = null;
    			
    			/* Walk to next node, stop when we reach where we started. */
    
    			curr = curr.mNext;
    			
    		} while(curr != minElem.mChild);
    	}
    	
    	/* Splice min element's children with root list, randomly assign the mMin */
    	
    	mMin = mergeLists(minElem.mChild, mMin);
    	
    	/* If no elements are left, job is finished */
    	if (mMin == null) {
    		return;
    	}
    	
    	/* Perform consolidation algorithm, results in root list of tree with at most one tree of given a certain degree*/
    	consolidate();
    	
     	return; 
     	
    }
     
    
    // OK
    private void consolidate() {
    	
    	int maxPossible = upperBoundCal(this.mSize);
    	
    	HeapNode[] A = new HeapNode[maxPossible + 1];
    	
    	ArrayList<HeapNode> rootArray = getRootArray(mMin);
    	for (HeapNode node : rootArray) {
    		HeapNode x = node;
    		int degree = x.mDegree;
    		while (A[degree] != null) {
    			HeapNode y = A[degree];  //another node with the same degree as x.
    			x = fibHeapLink(x, y);
    			heap.totalLinks += 1;
    			A[degree] = null;
    			degree += 1;		
    		}
    		A[degree] = x;
    	}
    	
    	mMin = null;
    	mStart = null;
    	
    	 for (int i = 0; i < maxPossible + 1; i ++) { ///Find a none empty element in array
    		 
    		 /* Update mMin and mStart */
    		if (A[i] != null ) {
    			mMin = A[i];
    			mStart = A[i];
    			break;
    			
    			}
    		}
    	
    	 for (int j = 0; j < maxPossible + 1; j ++) { /// Find the minimal element
    		if (A[j] != null)
    			if ((A[j].getKey() < mMin.getKey())) 
    				mMin = A[j];
    				
    	} 


    	
    	return; 
    	
    	 }
    
     
    
    /// OK
    public HeapNode fibHeapLink(HeapNode x, HeapNode y) {
    	
    	if (x.getKey() > y.getKey())
    	{
    		HeapNode temp = x;
    		x = y;
    		y = temp;
    	}
    	//// y's key is always larger than x's
    	/* Remove y from root list of heap.
    	 * Make y a child of x, incrementing x.degree
    	 * y mark = false
    	 * */
    	
    	/* update mStart */
    	if (mStart.equals(y)) {
    		mStart = x;
    	}
    	
    	removeRootList(y);
    	
    	if (x.mChild != null) {
    		mergeLists(y, x.mChild);
    	}
    	if (x.mChild == null) {
    		x.mChild = y;
    		y.mNext = y.mPrev = y;
    	}
    	y.mParent = x;
    	x.mDegree += 1;
    	return x;
    }
    
    
    ///OK
    private void removeRootList(HeapNode node) {
    	
    	node.mPrev.mNext = node.mNext;
		node.mNext.mPrev = node.mPrev;
		node.mNext = node;
		node.mPrev = node;
    }
    
    
   
    ///OK
    public int upperBoundCal(int size) {
    	
    	double x = (1 + Math.sqrt(5))/2;   ///Algorithm book page 523.
    	double result = Math.log(size) / Math.log(x);
    	
    	return (int) Math.floor(result);
    }
    
    
    ///OK
    public ArrayList<HeapNode> getRootArray(HeapNode minNode) {

    	ArrayList<HeapNode> result = new ArrayList<HeapNode>();
    	HeapNode curr = minNode;
    	do {
    		result.add(curr);
    		curr = curr.mNext;
    		
    	} while (!curr.equals(minNode));
    	
    	return result;
    }
    

   /**
    * public HeapNode findMin()
    *
    * Return the node of the heap whose key is minimal. 
    *
    */
    
    /// OK
    public HeapNode findMin()
    {
    	return mMin;
    } 
    
   /**
    * public void meld (FibonacciHeap heap2)
    *
    * Meld the heap with heap2
    *
    */
    
    ///OK
    public void meld (FibonacciHeap heap2)
    {
    	  /// Create a new FibonacciHeap to hold the result.
    	  FibonacciHeap result = new FibonacciHeap();
    	  
    	  /* Merge the two Fibonacci heap root lists together
    	  *  mergeLists computes the min of the two lists, therefore
    	  *  we can store the result in mMin of the new heap.
    	  */
    	  
    	  if (heap2.mMin == null) {
    		  return;
    	  }
    	  
    	  
    	  HeapNode potentialmMin = mergeLists(this.mStart, heap2.mStart);
    	  mMin = (this.mMin.getKey() < heap2.mMin.getKey()? this.mMin : heap2.mMin);
    	  mMin = (mMin.getKey() < potentialmMin.getKey()? mMin : potentialmMin);
    	  
    	  ///result.mStart = this.mStart;
    	  /* The size of the new heap is the sum of the sizes of the input heaps. */
    	  this.mSize = this.mSize + heap2.mSize;
    	  
    	  
    	  /* update this to be result */
    	  
    	  ///this.mMin = result.mMin;
    	  //this.mStart= result.mStart;
    	  //this.mSize = result.mSize;
    	  this.marked = this.marked + heap2.marked;
    	  this.totalCuts = this.totalCuts + heap2.totalCuts;
    	  this.totalLinks = this.totalLinks + heap2.totalLinks;
    	  
    	  
    	  return; 		
    }
    


///OK
    private static HeapNode mergeLists(HeapNode one, HeapNode two) {
    	
    	/* There are four cases depending on whether the lists are null or not.
         * We consider each separately.
         */
    	
    	if (one == null && two == null) {
    		return null;
    	}
    	else if (one != null && two == null) {
    		return one;
    	}
    	else if (one == null && two != null) {
    		return two;
    	}
    	else {  // Both non-null; actually do the splice.
    		
    		HeapNode oneNext = one.mPrev;
    		one.mPrev = two.mPrev;
    		one.mPrev.mNext = one;
    		two.mPrev = oneNext;
    		two.mPrev.mNext = two;
    		
    		/* Return a pointer to whichever's smaller. */
    		return one.getKey() < two.getKey()? one : two;
    	}
    	
    }
    

   /**
    * public int size()
    *
    * Return the number of elements in the heap
    *   
    */
    
    /// OK
    public int size()
    {
    	return mSize; 
    }
    	
    /**
    * public int[] countersRep()
    *
    * Return a counters array, where the value of the i-th entry is the number of trees of order i in the heap. 
    * 
    */
    
    
    public int[] countersRep()
    {
    	if (mMin == null) 
    	{
    		return null;
    	}
    	
    	TreeMap<Integer, Integer> degreeMap = new TreeMap<Integer, Integer>();
    	int size = fillMap(degreeMap);
        return mapToArray(degreeMap, size + 1);
    }
    
    private int fillMap(TreeMap<Integer, Integer> map)
    {
    	int maxDegree = 0;
    	HeapNode curr = mMin;
    	do
    	{
    		if(map.containsKey(curr.mDegree))
    		{
    			map.put(curr.mDegree, map.get(curr.mDegree) +1);
    			maxDegree = Math.max(maxDegree, curr.mDegree);
    			curr = curr.mNext;
    		}
    		else
    		{
    			map.put(curr.mDegree, 1);
    			maxDegree = Math.max(maxDegree, curr.mDegree);
    			curr = curr.mNext;
    		}
    	} while(curr != mMin);
    	
    	return maxDegree;
    }
    
    private int[] mapToArray(TreeMap<Integer, Integer> map, int size)
    {
    	int[] result = new int[size];
    	for(int degree : map.keySet())
    	{
    		result[degree] = map.get(degree);
    	}
    	return result;
    }
	
   /**
    * public void delete(HeapNode x)
    *
    * Deletes the node x from the heap. 
    *
    */
    public void delete(HeapNode x) 
    {
    	int delta = (x.key - mMin.key) +1;
    	decreaseKey(x, delta); // x is the new minimum
    	this.deleteMin();
    }

    /**
     * public void decreaseKey(HeapNode x, int delta)
     *
     * The function decreases the key of the node x by delta. The structure of the heap should be updated
     * to reflect this change (for example, the cascading cuts procedure should be applied if needed).
     */
     public void decreaseKey(HeapNode x, int delta)
     {    
    	 assert delta >= 0 : "Invalid negetive number";
    	 
    	 if (delta == 0) //no changes are made
    	 {
    		 return;
    	 }
      	x.key -= delta;
      	HeapNode y = x.mParent;
      	if (y != null && x.getKey() < y.getKey()) {
      		cut(x,y);
      		heap.totalCuts += 1;
      		cascadingCut(y);
      	}
      	if (x.getKey() < mMin.getKey()) {
      		mMin = x;
      	}
      	
      	return; 
     }
     
     
     private void cut(HeapNode x, HeapNode y) {
     	
     	removeChildList(x);
     	HeapNode currentmMin = mMin;
     	mMin = mergeLists(x, mStart);
     	mStart = x;
     	mMin = (currentmMin.getKey() < mMin.getKey()? currentmMin : mMin);
     	y.mDegree -= 1;
     	   	
     	if (x.mIsMarked == true) {
     		marked -= 1;
     		x.mIsMarked = false;
     	}
     	
     }
     
     private void cascadingCut(HeapNode y) {
     	
     	HeapNode z = y.mParent;
     	if ( z != null ) 
     	{
     		if (y.mIsMarked == false) 
     		{
     			marked += 1;
     			y.mIsMarked = true;
     		}
     		else 
     		{
     			cut(y, z);
     			heap.totalCuts += 1;
     			cascadingCut(z);
     		}
     	}
     
     }
     
     
     private void removeChildList(HeapNode node) 
     {	
    	 if(node.mNext.equals(node))
    	 {
    		 node.mParent.mChild = null;
    		 node.mParent = null;
    		 return;
    	 }
    	 
    	 node.mPrev.mNext = node.mNext;
    	 node.mNext.mPrev = node.mPrev;
    	 node.mParent.mChild = node.mNext;
    	 node.mParent = null;
    	 node.mNext = node;
    	 node.mPrev = node;
     }
     

    /**
     * public int potential() 
     *
     * This function returns the current potential of the heap, which is:
     * Potential = #trees + 2*#marked
     * The potential equals to the number of trees in the heap plus twice the number of marked nodes in the heap. 
     */
     public int potential() 
     {    
    	ArrayList<HeapNode> rootArray = getRootArray(mMin);
     	int length = rootArray.size(); /// # of trees.
     	
     	return length + 2 * marked; 
     }
     
     
    
    /**
     * public static int totalLinks() 
     *
     * This static function returns the total number of link operations made during the run-time of the program.
     * A link operation is the operation which gets as input two trees of the same rank, and generates a tree of 
     * rank bigger by one, by hanging the tree which has larger value in its root on the tree which has smaller value 
     * in its root.
     */
     public static int totalLinks()
     {    
    	
     	return heap.totalLinks;
     }

    /**
     * public static int totalCuts() 
     *
     * This static function returns the total number of cut operations made during the run-time of the program.
     * A cut operation is the operation which disconnects a subtree from its parent (during decreaseKey/delete methods). 
     */
     public static int totalCuts()
     {    
     	return heap.totalCuts;
     }

      /**
     * public static int[] kMin(FibonacciHeap H, int k) 
     *
     * This static function returns the k minimal elements in a binomial tree H.
     * The function should run in O(k(logk + deg(H)). 
     */
     public static int[] kMin(FibonacciHeap H, int k)
     {
         if (k == 0)
         {
             return null;
         }
         
         int[] arr = new int[k];
         FibonacciHeap range = new FibonacciHeap();
         range.insert2(H.mMin); 
         range.addMin(k, 0, arr);
         return arr;
     }
     
     private void addMin(int k, int index, int[] arr)
     {
         if(index == k)
         {
             return;
         }
         arr[index] = this.mMin.key;
         //System.out.println("k :" +k+ "||| idx: " +index+ "||| arr: " +Arrays.toString(arr));
         if(this.mMin.mSource.mChild != null)
         {
             this.addMinChilds(this.mMin.mSource);
         }
         this.deleteMin();
         this.addMin(k, index+1, arr);
     }
     
     private void addMinChilds(HeapNode parent)
     {
         this.insert2(parent.mChild);
         HeapNode curr = parent.mChild.mNext;
         while(curr != parent.mChild)
         {
             this.insert2(curr);
             curr = curr.mNext;
         }
     }
     
     
     public int getNumberOfTrees() {
    	 
    	if (mMin == null) {
    		return 0;
    	}
    	ArrayList<HeapNode> rootArray = getRootArray(mMin);
      	int length = rootArray.size(); /// # of trees.
      	return length;
    	 
     }
     
     public HeapNode getFirst() {
    	 
    	 return mStart;
     }
     
    /**
     * public class HeapNode
     * 
     * If you wish to implement classes other than FibonacciHeap
     * (for example HeapNode), do it in this file, not in 
     * another file 
     *  
     */
     public class HeapNode{

 	
    public int key;  // Element being stored here
    
    ///Added 
    private int mDegree;  // Number of children
    private boolean mIsMarked;  // Whether this node is marked
    
    private HeapNode mNext;  // Next and previous elements in the list
    private HeapNode mPrev;
    
    private HeapNode mParent; // Parent in the tree, if any.
    
    private HeapNode mChild; // Child node, if any.
    
    private HeapNode mSource; // Source in Binomial tree, used only in kMin
    
    
    public HeapNode(int key)
    {
        this.key = key;
        this.mDegree = 0;
        this.mIsMarked = false;
        mNext = mPrev = this;
    }
    
    public HeapNode(HeapNode source)
    {
        this.mSource = source;
        this.key = source.key;
        this.mDegree = 0;
        this.mIsMarked = false;
        mNext = mPrev = this;
    }

    public int getKey() {
        return this.key;
       }
    
    public HeapNode getNext() {
        return mNext;
    }
    
    public HeapNode getPrev() {
        return mPrev;
    }
    
    public void setKey(int x) {
        this.key = x;
    }
    
    public HeapNode getChild()
    {
        return this.mChild;
    }
    
    public HeapNode getParent()
    {
        return this.mParent;
    }
    
    public void setPrev(HeapNode x) {
    	
    	this.mPrev = x;
    	x.mNext = this;
    	
    }
    
public void setNext(HeapNode x) {
    	
    	this.mNext = x;
    	x.mPrev = this;
    
    }
public int getRank() {
	return this.mDegree;
}

     }
 }
