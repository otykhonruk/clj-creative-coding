(ns maze.sketch
  (:require [quil.core :refer :all]
            [maze.core :as m]))


(defn setup []
  (no-loop))


(defsketch maze
  :title "Maze generation and visualization"
  :settings #(pixel-density (display-density))
  :size [700 700]
  :setup setup
  :draw m/draw
  :features [:keep-on-top])


(defn refresh []
  (use :reload 'maze.core)
  (.loop maze))

;; (refresh)
