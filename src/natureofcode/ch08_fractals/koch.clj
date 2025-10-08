(ns ch08_fractals.koch
  (:require [quil.core :refer :all]
            [quil.middleware :refer [fun-mode]]
            [fastmath.vector :as v]))

(defn polygon
  [n r]
  (let [a (/ TWO-PI n)]
    (for [i (range n)]
      (let [a (* i a)
            x (* r (sin a))
            y (* r (cos a))]
        (v/vec2 x y)))))


(defn new-koch
  "Split edge into four equal segments"
  [a e]
  (let [v (v/mult (v/sub e a) 1/3)
        b (v/add a v)
        c (v/add b (v/rotate v THIRD-PI))
        d (v/add b v)]
    [a b c d e]))


(defn setup []
  (color-mode :hsb 360 100 100)
  (frame-rate 1)
  (polygon 3 300))


(defn update
  [poly]
  (mapcat #(apply new-koch %1)
          (partition 2 1 poly poly)))


(defn draw
  [poly]
  (background 45 10 100)
  (no-fill)
  (stroke-weight 1)
  (translate (/ (width) 2) (/ (height ) 2))
  (begin-shape)
  (doseq [[x y] poly]
    (vertex x y))
  (end-shape :close)
  (when (> (count poly) 2000)
    (no-loop)))


(defsketch koch
  :title "Koch curve"
  :size [720 720]
  :settings #(pixel-density (display-density))
  :setup setup
  :draw draw
  :update update
  :features [:keep-on-top]
  :middleware [fun-mode])
