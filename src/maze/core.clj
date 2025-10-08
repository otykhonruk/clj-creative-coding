(ns maze.core
  (:import [clojure.lang PersistentQueue])
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
  "`nil` frontier corresponds to `deque.pop` (LIFO) behaviour in Peter's implementation.
   To get `deque.popleft` (FIFO) behaviour, pass `PersistentQueue/EMPTY`"
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


(defn breadth-first-search [width height edges]
  (let [start [0 0]
        goal [(dec width) (dec height)]]
    (loop [frontier (conj PersistentQueue/EMPTY start)
           paths {start [start]}]
      (when-let [s (peek frontier)]
        (if (= s goal)
          (paths s)
          (let [nodes
                (for [s2 (neighbors s)
                      :when (and (not (contains? paths s2))
                                 (contains? edges (edge s s2)))]
                  s2)
                frontier' (into (pop frontier) nodes)
                paths' (into paths
                             (for [s2 nodes] [s2 (conj (paths s []) s2)]))]
            (recur frontier' paths')))))))
