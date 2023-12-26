(ns ch08_fractals.koch
  (:require [quil.core :refer :all]
            [quil.middleware :refer [fun-mode]]))

(defn vadd
  [[x1 y1] [x2 y2]]
  [(+ x1 x2)(+ y1 y2)])

(defn vsub
  [[x1 y1] [x2 y2]]
  [(- x1 x2)(- y1 y2)])

(defn vmul
  [[x y] n]
  [(* n x) (* n y)])

(defn vrotate
  [[x y] theta]
  (let [st (sin theta)
        ct (cos theta)]
    [(- (* x ct) (* y st))
     (+ (* x st) (* y ct))]))


(defn polygon
  [n r]
  (let [a (/ TWO-PI n)]
    (for [i (range n)]
      (let [a (* i a)
            x (* r (sin a))
            y (* r (cos a))]
        [x y]))))


(defn new-koch
  [a e]
  (let [v (vmul (vsub e a) 1/3)
        b (vadd a v)
        c (vadd b (vrotate v THIRD-PI))
        d (vadd b v)]
    [a b c d e]))


(defn setup []
  (color-mode :hsb 360 100 100)
  (frame-rate 1)
  (polygon 3 300))


(defn update
  [poly]
  (if (< (count poly) 2000)
    (let [segments (partition 2 1 poly poly)]
      (mapcat #(apply new-koch %1) segments))
    (do
      (no-loop)
      poly)))


(defn draw
  [poly]
  (background 45 10 100)
  (no-fill)
  (stroke-weight 1)
  (translate (/ (width) 2) (/ (height ) 2))
  (begin-shape)
  (doseq [[x y] poly]
    (vertex x y))
  (end-shape :close))


(defsketch koch
  :title "Koch curve"
  :size [800 800]
  :settings #(pixel-density (display-density))
  :setup setup
  :draw draw
  :update update
  :features [:keep-on-top]
  :middleware [fun-mode])
