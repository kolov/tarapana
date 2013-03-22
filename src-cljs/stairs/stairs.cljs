(ns tarapana.stairs
  (:require 
            [goog.dom :as dom]
            [clojure.browser.repl :as repl]
            [clojure.string :as str]
            [clojure.browser.repl :as repl]
            [domina :as d]
[tarapana.flow :as f])
  (:require-macros [tarapana.csutil :as csutil])
  (:use ;[jayq.core :only [$ css inner]]
        [jayq.util :only [map->js]]
        [domina :only [append! destroy-children!]]
        [domina.xpath :only [xpath]]
        [domina.css :only [sel]]
        )
)



(csutil/defelement canvas "theCanvas")
(csutil/defelement generateButton "generate")
(csutil/defelement showButton "show")
(csutil/defelement floors "floors")

(def ctx (.getContext canvas "2d"))

(.-strokeStyle   "#000000")
(.-fillStyle  "#FFFF00")
(do (.beginPath ctx) (.arc ctx 100,1,0, (* 2 Math/PI),true) (.closePath ctx) (.stroke ctx) (.fill ctx))

(defn ^:export initPage []   (
                  do
                  (repl/connect "http://localhost:9000/repl")
                 ;  (append! (sel "#stairs-input") "<div>Repl started</div>")
                ))

(defn floor [level n]
  (str "<div class=\"row\">"
       "<div class=\"span2\">Stromen niveau " (inc level) "</div>"
       (reduce str (for[ i (range n)] ( str "<div class=\"span1\"><input class=\"small-input\" id=\"stream" level "-" i "\" value=\"100\"></div>")))
       "</div>" 
       "<div class=\"row\">"
    "<div class=\"span2\"Capacitei>Capaciteit trap " (inc level)  " </div>" 
    "<div class=\"span2\">"
      "<input class=\"small-input\" id=\"stair-filled" level "\" value=\"0\"/>/"
      "<input class=\"small-input\" id=\"stair" level "\" value=\"15\"/>"
     "</div>"
    "</div>"
    ))
 





(defn get-input[id] (if-let [d (d/by-id id)] (-> d .-value js/parseInt)))
(defn get-stream[level ix] (get-input (str "stream" level "-" ix)))
(defn get-stair-capacity[level] (get-input (str "stair" level)))
(defn get-stair-filled[level] (get-input (str "stair-filled" level)))
(defn make-stair1[n] 
{ :capacity (get-stair-capacity n)
  :filled (get-stair-filled n) 
  :streams (loop [i 0 r []] (let[v (get-stream n i)] 
   (if v (recur (inc i) (conj r {:filled v})) r)))})

(defn make-stair[n]
 (let[stair (make-stair1 n)]
   (if (get-stair-capacity (inc n)) (assoc stair :stair (make-stair (inc n))) stair)))
 
(defn print-stair1[s level] 
  (str  " L" level ": ["(:filled s) "/" (:capacity s) "] Wt: ["
    (->> (map :filled (:streams s)) (interpose ",") (reduce str))
    "]"))

(defn print-stair[s t] 
   (str "T=" t
        (loop [s s l 0 t t r (print-stair1 s l )]
     (let [upstair (:stair s)]
       (if upstair (recur upstair (inc l) t (str r "|" (print-stair1 upstair (inc l)))) r)))))

(defn print-stages[ss] 
 (loop[ ss ss r "" t 0]
   (let [s (first ss)] (if s (recur (next ss)
     (str r "<div id=\"stage" t "\" class=\"result-row\"\">" (print-stair s t) "</div>") (inc t)) r))))

(def theStair (atom nil) )
(def stages (atom nil) )
(defn doShow []  (js/alert @theStair))
(set! (.-onclick (d/by-id "show")) doShow)

(defn calculate-stages[] (let [result (loop [i 30 s @theStair r [] ]
     (if (> i 0) (recur (dec i) (f/next-stair s) (conj r s)) r) )]
(swap! stages (constantly result))))

(defn doStart[]  (do 
 (swap! theStair #(make-stair 0))
 (swap! stages calculate-stages)
  (append! (d/by-id "stairs-output") (str "<div>" (print-stages @stages) "</div>"))))

(set! (.-onclick (d/by-id "start")) doStart)

(defn draw-stair[el floors streams]
(do
  (destroy-children! el)
  (append! el (reduce str (map #(floor % streams) (reverse (range floors))) ))
  (d/set-styles! (d/by-id "buttons") {:visibility "visible"})
  ) )

(set! (.-onclick generateButton) #(draw-stair (d/by-id "stairs-input") (get-input "floors") (get-input "streams"))) 
 