(ns noc.ch08-fractals.sketch
  (:require [clojure.string :refer [join]])
  (:require [quil.core :refer :all])
  (:require [quil.middleware :refer [fun-mode]]))


(def size 800)


(def tree
  {:axiom "F"
   :rules {\F "FF+[+F-F-F]-[-F+F+F]"}
   :angle (radians 22.5)
   :len   8})


(def plant
  {:axiom "X"
   :rules {\F "FF"
           \X "F-[[X]+X]+F[+FX]-X"}
   :angle (radians 22.5)
   :len   8})


(def grid
  {:axiom "F+F+F+F"
   :rules {\F "FF+F-F+F+FF"}
   :angle (radians 90)
   :len 8})


(def hilbert
  {:axiom "X"
   :rules {\X "-YF+XFX+FY-"
           \Y "+XF-YFY-FX+"}
   :angle (radians 90)
   :len 20})
;;   (translate 0 (height))


(def dragon
  {:axiom "FX"
   :rules {\X "X+YF+"
           \Y "-FX-Y"}
   :angle (radians 90)
   :len 5
   })


(def hex-gosper
  {:axiom "XF"
   :rules {\X "X+YF++YF-FX--FXFX-YF+"
           \Y "-FX+YFYF++YF+FX--FX-Y"}
   :angle (radians 60)
   :len 6})


(defn step [rules iter]
  (join (map #(rules % %) iter)))


(defn lsystem [{:keys [rules axiom] :as sys}]
  "Lazy sequence of L-system generations. Current generation stored under the `:gen` key"
  (iterate
   #(update % :gen (partial step rules))
   (assoc sys :gen axiom))) ;; initial generation


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


(defn setup []
  (frame-rate 1)
  (color-mode :hsb 360 100 100)
  (background 45 5 100)
  (lsystem tree))


(defn update-state [[_ & rest]]
  rest)


(defn draw [[it & rest]]
  (background 45 5 100)
  ;; grounded
  (translate (/ (width) 3) (height))
  (rotate (- HALF-PI))
  
  ;; centered
  ;; (translate (/ (width) 2) (/ (height) 2))

  (turtle it)
  (when (> (frame-count) 5)
    (no-loop)))


(defsketch lsystem-sketch
  :title "L-System"
  :size [size size]
  :settings #(pixel-density (display-density))
  :setup setup
  :draw draw
  :update update-state
  :features [:keep-on-top]
  :middleware [fun-mode])
