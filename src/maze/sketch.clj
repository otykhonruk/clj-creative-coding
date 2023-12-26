(ns maze.sketch
  (:require [quil.core :refer :all]
            [maze.core :as m]))


(def seed (atom (System/nanoTime)))

(def cell-size 10)
(def n-cells 80)
(def size (* cell-size n-cells))

(defn setup []
  (random-seed @seed)
  (color-mode :hsb 360 100 100)
  (no-loop))


(defn draw []
  (background 45 10 100)
  (stroke-weight 2)
  (let [grid (m/grid n-cells n-cells)
        edges (m/random-tree grid)]
    (m/draw-maze grid cell-size edges)))


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
    :q (exit)
    nil))


(defsketch maze
  :title "Maze generation and visualization"
  :settings #(pixel-density (display-density))
  :size [800 800]
  :setup setup
  :draw draw
  :features [:keep-on-top]
  :key-released process-key)
