(ns maze.sketch
  (:import [clojure.lang PersistentQueue])
  (:require [quil.core :refer :all]
            [maze.core :as m]))


(def seed (atom (System/nanoTime)))

(def cell-size 14)
(def n-cells 50)
(def size (* cell-size n-cells))

(def fifo (atom nil))


(defn setup []
  (random-seed @seed)
  (no-loop))


;; draw-cell/draw-grid are for testing purposes

(defn draw-cell
  "Draw N and W walls."
  [x y cellsize]
  (let [ox (* x cellsize) 
        oy (* y cellsize)
        x (+ ox cellsize)
        y (+ oy cellsize)]
    (line ox oy x oy)
    (line ox oy ox y)))


(defn draw-grid
  [grid cellsize]
  (doseq [[x y] grid]
    (draw-cell x y cellsize)))


(defn draw-maze
  [grid cellsize edges]
  (doseq [xy grid]
    (let [[n w] (m/neighbors-nw xy)
          [x y] xy
          ox (* x cellsize)
          oy (* y cellsize)]
      (when-not (edges [n xy])
        (line ox oy (+ ox cellsize) oy))
      (when-not (edges [w xy])
        (line ox oy ox (+ oy cellsize))))))


(defn draw []
  (background 250 245 235)
  (stroke-weight 2)
  (let [grid (m/grid n-cells n-cells)
        edges (m/random-tree grid @fifo)]
    (draw-maze grid cell-size edges)))


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
         (swap! fifo #(if %1 nil PersistentQueue/EMPTY))
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
