(ns tarapana.csutil)

(defmacro defelement[name id] `(def ~name (dom/getElement ~id)))