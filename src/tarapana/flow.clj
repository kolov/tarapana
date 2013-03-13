(ns tarapana.flow
)

(defn indexed [coll] (map vector (iterate inc 0) coll))

(def stair
  {
   :capacity 10, :filled 0, :gone 0
   :stair { :capacity 20 :filled 6 :gone 0 :streams [ {:filled 100} {:filled 8}]}
   :streams [{:filled 33}]})

(defn distribute-streams [[[total n] result] [ix filled]]
  (let [will-take (min (/ total n) filled)
        r [ [ (- total will-take) (dec n)] (conj result [ix will-take])]
       ]
    r))

                                        ; Calculates allowed stream for a stair and streams
                                        ; try:
                                        ; (admitted 30 {:filled 15} [{:filled 5} {:filled 50}])

(defn admittance[capacity want-stair streams]
  (let [
        want-streams (reduce + streams)
        may-stair (max (/ capacity 2) (- capacity want-streams))
        will-stair (min may-stair want-stair)
        will-streams (min  want-streams (- capacity will-stair))
        ordered-filled (sort-by #(second %) (indexed streams)) 
        r  (reduce distribute-streams [[will-streams (count streams)] []] ordered-filled)
        r2 (second r)
        r-ordered (sort-by first r2)
        stream-values (map second r-ordered)
; xx (print (str "Calculated " (doall stream-values)  " for capacity" capacity "; streams: " streams "\n"))
        ]
    [will-stair   stream-values]))

(defn update-streams[ stair admittance]
  (let [upstreams (:streams stair)
        streams-admittance (second admittance)
                                        ; xx (print (str "update-stream: " upstreams " admittance=" admittance))
        ]
    (if upstreams
      (merge stair {:streams
             (map #(update-in % [:filled] - %2) upstreams streams-admittance)})
      stair)))

(declare step)

(defn update-upstair[ stair admittance]
  (merge stair {:stair (step (:stair stair) (first admittance))}))

                                                   
(defn step[stair canleave]
  (let [{filled :filled upstair :stair upstreams :streams capacity :capacity} stair
        gone (min filled canleave)
        can-enter (+ gone (- capacity filled))
        admittance (admittance can-enter
                             (if upstair (:filled upstair) 0)
                             (map :filled upstreams))
        will-enter (reduce + (flatten admittance))
        stair1  (merge stair {:gone gone :filled (+ will-enter (- filled gone))
                           ;   :just-entered will-enter :addmittance admittance
                              })
        ]
    (update-streams (if upstair (update-upstair stair1 admittance) stair1 ) admittance)))

(defn go[stair] (step stair (:capacity stair)))

(defn steps[stair n]
  (loop [x n s stair] (when (> x 0) (do (print s) (print "\n") (recur (dec x) (go s))))))
 
         