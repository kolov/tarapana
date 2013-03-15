(defproject tarapana "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
   :dependencies [[org.clojure/clojure "1.5.1"]
                 [compojure "1.1.3"]
                 [clj-stacktrace "0.2.4"]
                 [ring/ring-core "1.1.0"]
                 [clj-http "0.3.0"]
                 [jayq "0.1.0-alpha4"]
                 [domina "1.0.2-SNAPSHOT"]
                ; [org.clojure/google-closure-library-third-party "0.0-2029"]
                [org.clojure/google-closure-library "0.0-1376-2"]
                [org.clojure/google-closure-library-third-party "0.0-1376-2"]
                 [ring/ring-jetty-adapter "1.1.0-SNAPSHOT"]]
  :dev-dependencies [[ring/ring-devel "1.1.0-SNAPSHOT"]
                     [lein-ring "0.5.4"]
                     [ring-serve "0.1.1"]
                     ]
  :plugins [[lein-cljsbuild "0.3.0"]
            [lein-swank "1.4.4"]
            [lein-ring "0.8.3"]]
  :source-path "src"
  :main tarapana.core
  :ring {:handler tarapana.core/app}
  ;  :hooks [leiningen.cljsbuild]
  :extra-classpath-dirs ["~/projects/clojurescript/src/clj"
                         "~/projects/clojurescript/src/cljs"]
  :cljsbuild {
      :crossovers [tarapana.flow]
      :crossover-path "crossover-cljs"
      :builds [{:source-path "src-cljs/stairs",
                        :compiler {:pretty-print true,
                                   :output-to "resources/public/cljs/stairs.js",
                                   :optimizations :whitespace}}

                       ]
              :repl-listen-port 9000
              :repl-launch-commands {
                                      "la" ["firefox" "-jsconsole" "http://localhost/my-page"]
                                      }
              }
  )

