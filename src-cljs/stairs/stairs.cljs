(ns tarapana.stairs
  (:require
            [goog.dom :as dom]
            [clojure.browser.repl :as repl]
            [goog.events :as events]
            [goog.ui.tree.TreeControl :as tree]
            [goog.ui.Component :as component]
            [clojure.string :as str]
            [clojure.browser.repl :as repl])
  (:require-macros [tarapana.csutil :as csutil])
  (:use [jayq.core :only [$ css inner]]
        [jayq.util :only [map->js]]     
        )
  )

 
(csutil/defelement canvas "theCanvas")
(def ctx (.getContext canvas "2d"))

(.-strokeStyle   "#000000")
(.-fillStyle  "#FFFF00")
(do (.beginPath ctx) (.arc ctx 100,100,50,0, (* 2 Math/PI),true) (.closePath ctx) (.stroke ctx) (.fill ctx))

(defn ^:export initPage [] (js/alert "Hello"))

(defn onload [] (repl/connect "http://localhost:9000/repl"))

(set! (.-onload js/window) onload)

