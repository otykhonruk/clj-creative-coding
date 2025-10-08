(ns maze.sketch
  (:import [clojure.lang PersistentQueue])
  (:require [quil.core :refer :all]
            [maze.core :as m]))


(def seed (atom (System/nanoTime)))

(def n-cells 50)
(def cell-size 14)

(def half-cell-size (/ cell-size 2))
(def size (* cell-size n-cells))

(def frontier (atom nil))


(defn setup []
  (random-seed @seed)
  (no-loop))


;; draw-cell/draw-grid are for testing purposes

(defn draw-cell
  "Draw N and W walls."
  [x y]
  (let [ox (* x cell-size) 
        oy (* y cell-size)
        x (+ ox cell-size)
        y (+ oy cell-size)]
    (line ox oy x oy)
    (line ox oy ox y)))


(defn draw-grid
  [grid]
  (doseq [[x y] grid]
    (draw-cell x y cell-size)))


(defn draw-maze
  [grid edges solution]

  ;; maze
  (stroke 5 5 5)
  (doseq [xy grid]
    (let [[n w] (m/neighbors-nw xy)
          [x y] xy
          ox (* x cell-size)
          oy (* y cell-size)]
      (when-not (edges [n xy])
        (line ox oy (+ ox cell-size) oy))
      (when-not (edges [w xy])
        (line ox oy ox (+ oy cell-size)))))

  ;; solution
  (stroke 250 20 5)
  (no-fill)
  (begin-shape)
  (doseq [[x y] solution]
    (vertex
     (+ (* x cell-size) half-cell-size)
     (+ (* y cell-size) half-cell-size)))
  (end-shape))


(defn draw []
  (background 250 245 235)
  (stroke-weight 2)
  (let [grid (m/grid n-cells n-cells)
        edges (m/random-tree grid @frontier)
        solution (m/breadth-first-search n-cells n-cells edges)]
    (draw-maze grid edges solution)))


(defn process-key []
  (case (key-as-keyword)
    :s (let [parts (map #(format "%02d" (%))
                        [month day hour minute seconds])
             tstamp (apply str parts)
             fname (str "maze-" (year) tstamp "-" @seed ".png")]
         (save fname))
    :r (do
         (random-seed (reset! seed (System/nanoTime)))
         (redraw))
    :l (do
         (use :reload 'maze.core)
         (redraw))
    :f (do  ;; toggle LIFO/FIFO order
         (swap! frontier #(if %1 nil PersistentQueue/EMPTY))
         (redraw))
    :q (exit)
    nil))


(defsketch maze
  :title "Maze generation and visualization"
  :settings #(pixel-density (display-density))
  :size [size size]
  :setup setup
  :draw draw
  :features [:keep-on-top]
  :key-released process-key)
