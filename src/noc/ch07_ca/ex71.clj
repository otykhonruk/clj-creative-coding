(ns noc.ch07-ca.ex71
  (:require [quil.core :as q]
            [quil.middleware :as m]
            [noc.ch07-ca.core :as ca]))

(defn setup
  []
  (q/color-mode :rgb 1)
  (q/background 0)
  {:ca (ca/ca 90 (q/width)) :gen 0})

(defn update'
  [state]
  (let [{:keys [ca gen]} state]
    (if (= (q/height) gen)
      {:ca (ca/ca (rand-int 256) (q/width))
       :gen 0}
      {:ca (rest ca)
       :gen (inc gen)})))

(defn draw
  [state]
  (let [{:keys [ca gen]} state]
    (when (= (q/height) gen) (q/background 0))
    (doseq [[x v] (map-indexed vector (first ca))]
      (q/set-pixel x gen (q/color v v v)))))

(q/defsketch ex71
  :title "Cellular automata"
  :size [1024 768]
  :setup setup
  :draw draw
  :update update'
  :features [:keep-on-top]
  :middleware [m/fun-mode])
