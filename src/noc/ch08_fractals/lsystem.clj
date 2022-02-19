(ns noc.ch08-fractals.sketch
  (:require [clojure.string :refer [join]])
  (:require [quil.core :refer :all]))


(def size 600)


(defn lsystem
  [axiom rules]
  (iterate (fn [a] (join (map #(rules % %) a)))
           axiom))


(defn turtle
  "Draws bracketed string-based L-System"
  [gen len theta]
  (doseq [c gen]
    (case c
      \F (do (line 0 0 len 0) (translate len 0))
      \f (translate 0 len)
      \+ (rotate theta)
      \- (rotate (- theta))
      \[ (push-matrix)
      \] (pop-matrix))))


(defn draw []
  (background 255)
  (translate (/ (width) 2) (height))
  (rotate (- HALF-PI))
  (stroke 0)
  (let [axiom "F"
        rules {\F "FF+[+F-F-F]-[-F+F+F]"}
        sys (lsystem axiom rules)
        gen (nth sys 4)
        theta (radians 22.5)
        len 10]
    (turtle gen len theta)))


(defn setup []
  (no-loop))


(defsketch lsystem-sketch
  :title "L-System"
  :size [size size]
  :settings #(pixel-density (display-density))
  :setup setup
  :draw draw
  :features [:keep-on-top])
