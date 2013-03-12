(ns tarapana.flow
)

(defn indexed [coll] (map vector (iterate inc 0) coll))

(def stair
  {
   :capacity 10, :filled 8, :gone 0
   :stair { :capacity 10 :filled 6 :gone 0 :streams [ {:filled 100}]}
   :streams [{:filled 200}]})

(defn distribute-streams [[[total n] result] [ix filled]]
  (let [will-take (min (/ total n) filled)
        r [ [ (- total will-take) (dec n)] (conj result [ix will-take])]
       ]
    r))

(defn admitted[capacity stair streams]
  (let [want-stair (if stair (:filled stair) 0)
        want-streams (reduce + (map :filled streams))
        may-stair (max (/ capacity 2) (- capacity want-streams))
        will-stair (max may-stair want-stair)
        will-streams (min  want-streams (- capacity will-stair))
        ordered-streams (sort-by #(:filled (second %)) (indexed streams))
        ordered-filled (map #( vector (first %) (:filled (second %)))  ordered-streams)
        r  (reduce distribute-streams [[will-streams (count streams)] []] ordered-filled)
        r2 (second r)
        r-ordered (sort-by first r2)
        stream-values (map second r-ordered)]
    [will-stair   stream-values   ]))

(defn step1[stair canleave]
  (let [{filled :filled upstair :stair streams :streams} stair
        gone (min filled canleave)
        canenter (- filled canleave)
        wantenter (+ (:filled upstair) (reduce + (map :filled streams)))
        willenter (min wantenter canenter)]
    (merge stair {:gone gone :filled (+ willenter (- filled gone))})))

(defn step[stair] (let [{canleave :filled upstair :stair} stair ]
  (step1  (merge stair { :stair (if upstair (step1 upstair canleave) nil)} ) canleave)))
         