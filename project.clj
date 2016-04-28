(defproject lein-cljsbuild-example "1.2.3"
  :plugins [[lein-cljsbuild "1.1.3"]]
  :description "FIXME: write description"
  :dependencies [[org.clojure/clojurescript "1.8.51"]
                 [cljs-http "0.1.40"]
                 [cljsjs/bootstrap "3.3.6-0"]
                 [cljsjs/pikaday "1.4.0-1"]]
  :main 'hello-world.core
  :output-to "out/main.js"
)
