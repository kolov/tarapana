(ns tarapana.core
  (:use compojure.core
          [ring.util.response :only (file-response resource-response status)]
        ring.util.servlet
        [ring.middleware params session file file-info]
        )
  (:require [compojure.route :as r]
            [clj-http.client :as c]
            [compojure.handler :as handler]
            [ring.adapter.jetty :as ring]
            [clojure.string :as s]
            ))

(defroutes app-routes
 
  (GET "stairs" [] (resource-response "public/stairs.html"))
  (GET "/stairs/*" [] (resource-response "public/stairs.html"))
  (r/resources "/")
  (r/not-found "<h1>Page not found</h1> <br/> Try <a href=\"/search.html\">search.html</a>")
  )

;(defn app-reload [] (-> app-routes (handler/site)
;                      (wrap-reload #'app-routes '(net.kolov.jaclo.server))
;                      ))

(defservice app-routes)

(def app (handler/site app-routes))
;(defonce server  (ring/run-jetty #'app {:port 8888 :join? false}))
; to start: (use 'ring.util.serve)
; (serve app)

(defn -main [port]
  (ring/run-jetty app {:port (Integer. port)}))
