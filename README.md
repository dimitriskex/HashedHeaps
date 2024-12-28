# HashedHeaps: A Hybrid Data Structure that combines Hash Table and Heap 

## Description:
The `HashHeap` is a data structure designed for efficient management of `(id, priority)` pairs.
It combines a hash table for fast lookup operations and a heap for priority-based operations. This structure supports operations like insertion, deletion, union, and retrieving top `k` elements efficiently.

## Features
**Insert**: Insertion of elements in the struct
**RemoveId**: Removes the element this with unique ID
**Remove**: Removes from `this` struct the element with the highest priority
**Contains**: Returns true ,if the element with id is in the struct, in other case returns false
**Union**: Combines two `HashHeap` structures with priority adjustments
**Diff**: Compute the difference of two `HashHeap` structures.
**Top k Elements**: Get the top `k` elements based on priority

