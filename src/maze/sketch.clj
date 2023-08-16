(ns maze.sketch
  (:require [quil.core :refer :all]
            [maze.core :as m]))


(def seed (atom (System/nanoTime)))


(defn setup []
  (random-seed @seed)
  (no-loop))


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
  :size [700 700]
  :setup setup
  :draw m/draw
  :features [:keep-on-top]
  :key-released process-key)
