(ns ^:figwheel-always coursework1.core-test
  (:require
   [cljs.test :refer-macros [deftest testing is]]
   [coursework1.test-formatter :as formatter]
   [figwheel.client :as fw]
   [coursework1.test-helpers :as th]
   [coursework1.core :refer [do-something-x-y]]
   ))

(enable-console-print!)


(deftest test-do-something-x-y
  (is (= (do-something-x-y 1 2) 3)))


  (deftest test-async
    (async done
      (http/get "http://foo.com/api.edn"
        (fn [res]
          (is (= res :awesome))
          (done)))))

(defn run-tests []
  (.clear js/console)
  (cljs.test/run-all-tests #"coursework1.*-test"))
(run-tests)

;; FW connection is optional in order to simply run tests,
;; but is needed to connect to the FW repl and to allow
;; auto-reloading on file-save
(fw/start {
           :websocket-url "ws://localhost:3449/figwheel-ws"
           ;; :autoload false
           :build-id "test"
           })
