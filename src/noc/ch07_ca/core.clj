(ns noc.ch07-cellular-automata.core)

(defn next-cell
  [rule [a b c]]
  (let [s (-> a
              (bit-shift-left 1) (bit-or b)
              (bit-shift-left 1) (bit-or c))]
    (bit-shift-right (bit-and rule (bit-shift-left 1 s)) s)))

(defn next-row
  [rule row]
  (let [row' (map #(next-cell rule %) (partition 3 1 row))]
    ;; pad with 0's
    (conj (into [0] row') 0)))

(defn ca
  ([rule w]
   (ca rule w (assoc (vec (repeat w 0)) (quot w 2) 1)))
  ([rule w row]
   (iterate #(next-row rule %) row)))
