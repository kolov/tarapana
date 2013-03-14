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
        [domina :only [append!]]
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

(defn floor [level]
  (str "<div class=\"row\">"
       "<div class=\"span2\"><input class=\"small-input\" id=\"stream" level "-0\" value=\"100\"> --&gt;</div>"
       "<div class=\"span2\" align=\"center\">&nbsp;|&nbsp;</div>"
       "<div class=\"span2\"> &lt;-- <input class=\"small-input\" id=\"stream" level "-1\" value=\"100\"> </div>"
       "</div>" 
       "<div class=\"row\">"
    "<div class=\"span2\">&nbsp;</div>"
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
 


(def theStair (atom nil) )
(def stages (atom nil) )
(defn doShow []  (js/alert @stages))
(set! (.-onclick (d/by-id "show")) doShow)

(defn doStart[] 
  (js/alert (loop [i 30 s @theStair r [] ]
     (if (> i 0) (recur (dec i) (f/next-stair s) (conj r s)) r) )))

(set! (.-onclick (d/by-id "start")) doStart)

(defn draw-stair[el n]
(do
  (append! el (reduce str (map floor (reverse (range n))) ))
  (d/set-styles! (d/by-id "buttons") {:visibility "visible"})
  (swap! theStair #(make-stair 0)))
(js/alert @theStair))

(set! (.-onclick generateButton) #(draw-stair (d/by-id "stairs-input") (get-input "floors"))) 
 