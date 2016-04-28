(ns hello-world.core
  (:require [clojure.browser.repl :as repl]
            [cljs-http.client :as http]
            [cljs.core.async :refer [<!]])
  (:require-macros [cljs.core.async.macros :refer [go]]))

(defonce conn
  (repl/connect "http://localhost:9000/repl"))

(enable-console-print!)
(println "Hello world!")

(defn total [url]
  (go (let [response (<! (http/get url {:with-credentials? false}))]
    (:total (:body response)))))

(enable-console-print!)
(go
  (println (<! (total "https://api.stackexchange.com/2.2/answers?fromdate=1456790400&todate=1459382400&tagged=clojure&site=stackoverflow&filter=!bRyCgbjcxkJlK8"))))

;;(go
  ;;defn multi [(listOfUrl)]
    ;  map (println(<!(total listOfUrl))))
