/*
 * Name: Kechagioglou Dimitris 
 * Student ID: E20070
 * Description: The HashHeap class implements a structure that combines
 * a hash table and a heap for efficient management of (id, priority) pairs.
 */

class Node {
    public int id;
    public int priority;

    // Contructor to initialize a node with an id and priority
    public Node(int id, int priority) {
        this.id = id;
        this.priority = priority;
    }
}


/*
 *Creates a structure which stores up to mx elements
 *The method selects as size of the hastTable maxsize
 *a prime number that is greater than 2*mx
*/
    
public class HashHeap {

   //hashTable storing nodes based on id
    private Node[] hashTable;

    //Prime numbers (i mod 2 !=0)
    private static final int[] primes = {101, 211, 431, 863, 1741, 3469, 6949, 14033, 28057, 56111}; 

    // Heap is storing nodes based on priority
    private Node[] heap;

    // Maximum size of hashtable and heap
    private int maxSize; 

    // Number of elements in the heap
    private int heapSize; 

    // Number of elements in the hashTable
    private int tableSize;


    // Constructor to initialize HashHeap with a given maximum number of elements
    public HashHeap(int mx) {

        // Determine the smallest prime number greater than 2*mx for the hashtbable
        for (int prime : primes) {

            // Prime is 101 in our first case
            if (prime > 2 * mx) { 

                // Maxsize is 101
                maxSize = prime; 

                // Exit the loops
                break; 
            }
        }

        // Our heap and hashtable have the same size --> 101
        hashTable = new Node[maxSize];
        heap = new Node[maxSize];
        tableSize = 0;
        heapSize = 0;
    }

    // Hash function to address every id to a position in the hashtable
    private int hash(int id) {
        return id % maxSize;
    }

    private int probe(int hash, int step) {
        return (hash + step * step) % maxSize;
    }



    /*
     * Insertion of an element in the struct
     * If insertion is successful, returns true
     * Or else it returns false
     */
    public boolean insert(Node n) {
        if (contains(n.id)) return false;

        int hash = hash(n.id);
        int step = 0;
        while (hashTable[hash] != null) {
            hash = probe(hash, ++step);
        }
        hashTable[hash] = n;
        tableSize++;
        heapInsert(n);
        return true;
    }


    /*
     * Takes away the element this with unique ID
     * Returns the node which stores the element
     * Returns null, if struct is this is empty 
     * or if the element doesnt exist  
     */

    public Node removeId(int id) {
        int hash = hash(id);
        int step = 0;
        while (hashTable[hash] != null) {
            if (hashTable[hash].id == id) {
                Node removed = hashTable[hash];
                hashTable[hash] = null;
                tableSize--;
                heapRemove(removed);
                return removed;
            }
            hash = probe(hash, ++step);
        }
        return null;
    }


    /*
     *Takes away from this struct the element with 
     * the max priority
     * Returns the node which stores this element
     * Returns null if the struct is empty
     */

    public Node remove() {
        if (heapSize == 0) return null;
        Node max = heap[0];
        heap[0] = heap[--heapSize];
        heapifyDown(0);
        removeId(max.id);
        return max;
    }



    /*
     * Returns true ,if the element with id is in the struct
     * In other case it returns false
     * 
     */

    public boolean contains(int id) {
        int hash = hash(id);
        int step = 0;
        while (hashTable[hash] != null) {
            if (hashTable[hash].id == id) return true;
            hash = probe(hash, ++step);
        }
        return false;
    }


/*
 * Returns a new instance of the HashHeap class, containing the union of the elements
 * from the current structure this and the given structure.
 * Each element present in both structures this and h, possibly with different priorities,
 * will be included in the resulting structure with a priority equal to the sum of the two priorities.
 */


    public HashHeap union(HashHeap h) {
        HashHeap result = new HashHeap(this.tableSize + h.tableSize);
        for (Node n : this.heap) {
            if (n != null) result.insert(new Node(n.id, n.priority));
        }
        for (Node n : h.heap) {
            if (n != null) {
                if (result.contains(n.id)) {
                    Node existing = result.removeId(n.id);
                    result.insert(new Node(n.id, existing.priority + n.priority));
                } else {
                    result.insert(new Node(n.id, n.priority));
                }
            }
        }
        return result;
    }



/*
 * Returns a new instance of the HashHeap class, containing elements with an id that:
 * (a) Exists in the current structure this but not in the given structure h, or
 * (b) Exists in both structures this and h: the priority of the element in the resulting structure
 * equals the difference between the priorities of the element in this and h.
 */


    public HashHeap diff(HashHeap h) {
        HashHeap result = new HashHeap(this.tableSize);
        for (Node n : this.heap) {
            if (n != null) {
                if (h.contains(n.id)) {
                    Node other = h.getNodeById(n.id);
                    int priorityDiff = n.priority - other.priority;
                    if (priorityDiff > 0) {
                        result.insert(new Node(n.id, priorityDiff));
                    }
                } else {
                    result.insert(new Node(n.id, n.priority));
                }
            }
        }
        return result;
    }


/*
 * Returns a new instance of the HashHeap class containing the k elements from the current structure (this)
 * with the highest priority values. If the current structure contains fewer than k elements,
 * the resulting structure includes all of these elements.
 */

    public HashHeap kbest(int k) {
        HashHeap result = new HashHeap(k);
        for (int i = 0; i < k && i < heapSize; i++) {
            result.insert(heap[i]);
        }
        return result;
    }

    private void heapInsert(Node n) {
        heap[heapSize] = n;
        heapifyUp(heapSize++);
    }

    private void heapRemove(Node n) {
        for (int i = 0; i < heapSize; i++) {
            if (heap[i].id == n.id) {
                heap[i] = heap[--heapSize];
                heapifyDown(i);
                break;
            }
        }
    }

    private void heapifyUp(int index) {
        while (index > 0) {
            int parent = (index - 1) / 2;
            if (heap[index].priority <= heap[parent].priority) break;
            swap(index, parent);
            index = parent;
        }
    }

    private void heapifyDown(int index) {
        while (index < heapSize) {
            int left = 2 * index + 1;
            int right = 2 * index + 2;
            int largest = index;

            if (left < heapSize && heap[left].priority > heap[largest].priority) largest = left;
            if (right < heapSize && heap[right].priority > heap[largest].priority) largest = right;

            if (largest == index) break;
            swap(index, largest);
            index = largest;
        }
    }

    private void swap(int i, int j) {
        Node temp = heap[i];
        heap[i] = heap[j];
        heap[j] = temp;
    }

    private Node getNodeById(int id) {
        int hash = hash(id);
        int step = 0;
        while (hashTable[hash] != null) {
            if (hashTable[hash].id == id) return hashTable[hash];
            hash = probe(hash, ++step);
        }
        return null;
    }

    public static void main(String[] args) {

        // Create a new HasHeap instance
        HashHeap hashHeap = new HashHeap(10);

        // Insert nodes into the HashHeap
        hashHeap.insert(new Node(1, 50));
        hashHeap.insert(new Node(2, 30));
        hashHeap.insert(new Node(3, 70));
        hashHeap.insert(new Node(4, 20));

        // It should be true
        System.out.println("Contains ID 3: " + hashHeap.contains(3)); 

        // It should be false
        System.out.println("Contains ID 5: " + hashHeap.contains(5));

        // Remove element with ID
        Node removedNode = hashHeap.removeId(2);
        System.out.println("Removed Node ID 2: " + (removedNode != null ? removedNode.priority : "null"));

        // Αφαίρεση στοιχείου με μέγιστη προτεραιότητα
        Node maxNode = hashHeap.remove();
        System.out.println("Removed Max Priority Node: " + (maxNode != null ? maxNode.priority : "null"));

        // Προσθήκη περισσότερων στοιχείων και ένωση
        HashHeap anotherHeap = new HashHeap(10);
        anotherHeap.insert(new Node(5, 90));
        anotherHeap.insert(new Node(6, 10));

        HashHeap unionHeap = hashHeap.union(anotherHeap);
        System.out.println("Union contains ID 5: " + unionHeap.contains(5)); // Αναμένεται true
        System.out.println("Union contains ID 2: " + unionHeap.contains(2)); // Αναμένεται false

        // Αφαίρεση των k καλύτερων
        HashHeap kBestHeap = unionHeap.kbest(2);
        System.out.println("kBest contains ID 5: " + kBestHeap.contains(5)); // Αναμένεται true
        System.out.println("kBest contains ID 3: " + kBestHeap.contains(3)); // Αναμένεται true
    }
}