(ns maze.core
  (:import [clojure.lang PersistentQueue])
  (:require [clojure.set :refer [intersection]]
            [quil.core :refer :all]))
        

;; Literal translation from Python to Clojure from Peter Norvig's
;; "Making and Solving Mazes". Only the "making" currently implemented.
;; https://github.com/norvig/pytudes/blob/main/ipynb/Maze.ipynb


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
  ([nodes] (random-tree nodes nil)) ; list
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


;; (random-tree (grid 8 8))

(defn draw-cell
  [x y cellsize]
  ;; Draw N and W walls.
  (let [ox (* x cellsize) 
        oy (* y cellsize)
        x (+ ox cellsize)
        y (+ oy cellsize)]
    (line ox oy x oy)
    (line ox oy ox y)))


(defn draw-maze
  [grid cellsize edges]
  (doseq [xy grid]
    (let [[n w] (neighbors-nw xy)
          [x y] xy]
      (let [ox (* x cellsize)
            oy (* y cellsize)]
        (when-not (edges [n xy])
          (line ox oy (+ ox cellsize) oy))
        (when-not (edges [w xy])
          (line ox oy ox (+ oy cellsize)))))))

(defn draw-grid
  [grid cellsize]
  (doseq [[x y] grid]
    (draw-cell x y cellsize)))

(defn draw []
  (no-loop)
  (background 255)
  (stroke-weight 2)
  (let [grid (grid 70 70)
        ;; corresponds to `deque.pop` in Peter's implementation.
        ;; to get `deque.popleft` behaviour, pass PersistentQueue/EMPTY
        edges (random-tree grid)]
    (draw-maze grid 10 edges)))
