(defproject coursework "1.0.0-SNAPSHOT"
  :description "visualisation of stackoverflow"
  :plugins [[lein-cljsbuild "1.1.3"]
            ;;[lein-figwheel "0.5.2"]
            ]
  :dependencies [
                 ;;[org.clojure/clojure "1.7.0"]
                 [org.clojure/clojurescript "1.8.51"]
                 [cljs-http "0.1.40"]
                 ;;[cljsjs/bootstrap "3.3.6-0"]
                 [cljs-pikaday "0.1.2"]
                 ;;[cljsjs/pikaday "1.4.0-1"]
                 ;;[cljsjs/react "15.0.1-1"]
                 [reagent "0.6.0-alpha"]
                 [cljsjs/chartist "0.9.4-4"]
                 ;;[figwheel "0.5.2"]
                ]

  :main 'coursework1.core
  :output-to "out/main.js"
  )

;;   :hooks [leiningen.cljsbuild]

;;   :profiles {:dev {:cljsbuild
;;                    {:builds {:client
;;                              {:figwheel {:on-jsload "coursework1.core/run"}
;;                               :compiler {:main "coursework1.core"
;;                                          :optimizations :none}}}}}

;;              :prod {:cljsbuild
;;                     {:builds {:client
;;                               {:compiler {:optimizations :advanced
;;                                           :elide-asserts true
;;                                           :pretty-print false}}}}}}

;;   :figwheel {:repl false}

;;   :cljsbuild {:builds {:client
;;                        {:source-paths ["src"]
;;                         :compiler {:output-dir "out"
;;                                    :output-to "out/main.js"}}}}
