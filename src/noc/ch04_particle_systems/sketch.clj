(ns noc.ch04-particle-systems.sketch
  (:require [quil.core :refer :all]
            [quil.middleware :refer [fun-mode]]
            [noc.ch04-particle-systems.core :refer :all]))


(defn setup []
  (color-mode :rgb 255 255 255 1)
  (make-emitter))


(defn update [emitter]
  (->> (make-particle (/ (width) 2) 40)
       (conj emitter)
       (update-emitter)))


(defn draw [emitter]
  (background 250)
  (doseq [{[x y] :pos lsp :lsp} emitter]
    (stroke 0 lsp)
    (fill 0 lsp)
    (ellipse x y 15 15)))


(defsketch emitter
  :title "Basic particle emitter"
  :size [800 600]
  :settings #(pixel-density (display-density))
  :setup setup
  :update update
  :draw draw
  :features [:keep-on-top]
  :middleware [fun-mode])
