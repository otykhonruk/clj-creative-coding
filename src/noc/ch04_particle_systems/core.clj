(ns noc.ch04-particle-systems.core
  (:require [quil.core :refer :all]
            [quil.middleware :as m]
            [fastmath.vector :as v]))


(defrecord Particle [pos acc vel lsp])


(def gravity [0 0.1])
(def lifespan 250)  ;; frames

(def make-emitter hash-set)

(defn make-particle [x y]
  "Constructs particle at given position with random velocity"
  (->Particle [x y]
              gravity
              [(random -1 1) (random -2, 0)] ;; velocity
              1))

(defn update-particle [{:keys [acc vel] :as p}]
  (-> p
      (update :vel v/add acc)
      (update :pos v/add vel)
      (update :lsp - (/ 2 lifespan))))


(defn update-emitter [emitter]
  (set
   (for [p emitter
         :when (> (:lsp p) 0)]
     (update-particle p))))
