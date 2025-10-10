(ns natureofcode.ch08-fractals.sketch
  (:require [clojure.string :refer [join]])
  (:require [quil.core :refer :all])
  (:require [quil.middleware :refer [fun-mode]]))


(def default-num-gens 4)
(def default-stroke [0 0 0])    ; black
(def default-origin [0.5 0.5])  ; center


(def systems
  [
   {:name "tree"
    :axiom "F"
    :rules {\F "FF+[+F-F-F]-[-F+F+F]"}
    :angle (radians 22.5)
    :len   8
    :stroke [120 60 40]
    :origin [0.35 1 (- HALF-PI)]}

   {:name "plant"
    :axiom "X"
    :rules {\F "FF"
            \X "F-[[X]+X]+F[+FX]-X"}
    :angle (radians 22.5)
    :len   8
    :stroke [120 60 40]
    :origin [0.35 1 (- HALF-PI)]}

   {:name "grid"
    :axiom "F+F+F+F"
    :rules {\F "FF+F-F+F+FF"}
    :angle (radians 90)
    :len 8}

   {:name "hilbert"
    :axiom "X"
    :rules {\X "-YF+XFX+FY-"
            \Y "+XF-YFY-FX+"}
    :angle (radians 90)
    :len 6
    :origin [0 1]
    :frames 7}

   {:name "dragon"
    :axiom "FX"
    :rules {\X "X+YF+"
            \Y "-FX-Y"}
    :angle (radians 90)
    :len 5
    :stroke [300 50 50]
    :origin [0.6 0.7]
    :frames 15}

   {:name "hex-gosper"
    :axiom "XF"
    :rules {\X "X+YF++YF-FX--FXFX-YF+"
            \Y "-FX+YFYF++YF+FX--FX-Y"}
    :angle (radians 60)
    :len 5
    :stroke [0 50 50]
    :origin [0.8 0.1]}
   ]
  )


(defn step [rules iter]
  (join (map #(rules % %) iter)))


(defn lsystem [{:keys [rules axiom] :as sys}]
  "Lazy sequence of indexed L-system generations. Current generation stored under the `:gen` key"
  (->> (assoc sys :gen axiom)  ;; initial generation
       (iterate #(update % :gen (partial step rules))) ;; infinite sequence of generations
       (map-indexed vector)))


(defn turtle
  "Draws bracketed string-based L-System"
  [{:keys [gen len angle]}]
  (doseq [c gen]
    (case c
      \F (do (line 0 0 len 0) (translate len 0))
      \f (translate 0 len)
      \+ (rotate angle)
      \- (rotate (- angle))
      \[ (push-matrix)
      \] (pop-matrix)
      nil)))


(defn position
  ([w h]
   (translate (* (width) w) (* (height) h)))
  ([w h a]
   (position w h)
   (rotate a)))


(defn setup []
  (color-mode :hsb 360 100 100)
  (background 45 5 100)
  (frame-rate 1)
  (let [[s & xs] (cycle systems)]
    [(lsystem s) xs]))


(defn update-state [[[[i gen] & gens] systems]]
  (if (> i (:frames gen default-num-gens))
    [(lsystem (first systems)) (rest systems)]
    [gens systems]))


(defn draw [[[[_ it] & _] _]]
  (background 45 5 100)
  (stroke (:stroke it default-stroke))
  (apply position (:origin it default-origin))
  (turtle it))


(defsketch lsystem-sketch
  :title "L-System"
  :size [780 780]
  :settings #(pixel-density (display-density))
  :setup setup
  :draw draw
  :update update-state
  :features [:keep-on-top]
  :middleware [fun-mode])
