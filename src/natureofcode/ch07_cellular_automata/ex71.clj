(ns natureofcode.ch07-cellular-automata.ex71
  (:require [quil.core :refer :all]
            [quil.middleware :refer [fun-mode]]
            [natureofcode.ch07-cellular-automata.core :as ca]))

(defn setup []
  (color-mode :rgb 1)
  (background 0)
  {:ca (ca/ca 90 (width)) :gen 0})


(defn update [state]
  (let [{:keys [ca gen]} state]
    (if (= (height) gen)
      {:ca (ca/ca (rand-int 256) (width))
       :gen 0}
      {:ca (rest ca)
       :gen (inc gen)})))


(defn draw [state]
  (let [{:keys [ca gen]} state]
    (when (= (height) gen) (background 0))
    (doseq [[x v] (map-indexed vector (first ca))]
      (set-pixel x gen (color v)))))


(defsketch ex71
  :title "Cellular automata"
  :size [1024 768]
  :setup setup
  :draw draw
  :update update
  :features [:keep-on-top]
  :middleware [fun-mode])
