(ns maze.core
  (:require [clojure.set :refer [intersection]]))


(defn edge
  [a b]
  (sort [a b]))


(defn grid
  [w h]
  (for [x (range w)
        y (range h)]
    [x y]))


(defn neighbors
  [[x y]]
  #{[(inc x) y]
    [(dec x) y]
    [x (inc y)]
    [x (dec y)]})


(defn neighbors-nw
  [[x y]]
  [[x (dec y)]
   [(dec x) y]])


(defn random-tree
  "`nil` corresponds to `deque.pop` (LIFO) in Peter's implementation.
   To get `deque.popleft` (FIFO) behaviour, pass PersistentQueue/EMPTY"
  ([nodes]
   (random-tree nodes nil))
  ([nodes frontier]
   (let [root (rand-nth nodes)]
     (loop [tree #{}
            nodes (disj (set nodes) root)
            frontier (conj frontier root)]
       (if (empty? nodes)
         tree
         (let [node (peek frontier)
               frontier (pop frontier)
               nbrs (intersection (neighbors node) nodes)]
           (if (empty? nbrs)
             (recur tree nodes frontier)
             (let [nbr (rand-nth (seq nbrs))]
               (recur (conj tree (edge node nbr))
                      (disj nodes nbr)
                      (conj frontier node nbr))))))))))
