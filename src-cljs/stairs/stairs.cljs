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
        [domina :only [append!]]
        [domina.xpath :only [xpath]]
        [domina.css :only [sel]]
        )
  )


(csutil/defelement canvas "theCanvas")
(csutil/defelement generateButton "generate")
(csutil/defelement floors "floors")

(def ctx (.getContext canvas "2d"))

(.-strokeStyle   "#000000")
(.-fillStyle  "#FFFF00")
(do (.beginPath ctx) (.arc ctx 100,1,0, (* 2 Math/PI),true) (.closePath ctx) (.stroke ctx) (.fill ctx))

(defn ^:export initPage []   (
                  do
                  (repl/connect "http://localhost:9000/repl")
                   (append! (sel "#stairs-input") "<div>Hello world!</div>")
                ))

; (set! (.-onload js/window) onload)
(set! (.-onclick generateButton) #(js/alert "click"))

