(ns ^:figwheel-always coursework1.core-test
  (:require
   [cljs.test :refer-macros [deftest testing is]]
   [coursework1.test-formatter :as formatter]
   [figwheel.client :as fw]
   [coursework1.test-helpers :as th]
   [coursework1.date :refer [daysBetweenDates]]
   ))

(enable-console-print!)


(deftest test-daysBetweenDates
  (is (= (daysBetweenDates 1474588800  1474416000) 2)))


(defn run-tests []
  (.clear js/console)
  (cljs.test/run-all-tests #"coursework1.*-test"))
(run-tests)

;; Testresults can be viewed under localhost:3449/test.html in the console
(fw/start {
           :websocket-url "ws://localhost:3449/figwheel-ws"
           ;; :autoload false
           :build-id "test"
           })
