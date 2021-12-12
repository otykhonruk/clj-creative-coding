(ns noc.ch01-vectors.bouncing-ball
  (:require [clojure.core.matrix :as m]
            [quil.core :as q]
            [quil.middleware :refer [fun-mode]]))

(def width 640)
(def height 360)


(defn setup []
  {:pos [100 100]
   :speed [1 3.3]})


(defn upd [state]
  (let [{pos :pos speed :speed} state
        pos' (m/add pos speed)
        [x y] pos'
        [dx dy] speed]
    {:pos pos'
     :speed [(if (or (< x 0) (> x width)) (* -1 dx) dx)
             (if (or (< y 0) (> y height)) (* -1 dy) dy)]})) 

(defn draw [state]
  (let [{[x y] :pos} state]
    (q/background 255)
    (q/stroke 0)
    (q/fill 175)
    (q/ellipse x y 100 100)))


(q/defsketch bouncing-ball
  :title "Bouncing ball"
  :size [width height]
  :setup setup
  :update upd
  :draw draw
  :features [:keep-on-top]
  :middleware [fun-mode])

