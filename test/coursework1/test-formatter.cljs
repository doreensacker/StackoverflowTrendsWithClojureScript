(ns coursework1.test-formatter
  (:require
   [cljs.test :as test :include-macros true :refer [report]]))

;; This file includes some formatting functions to give
;; some improved test output display, including a
;; color-changing browser favicon that shows green
;; for passing tests and red for failing.

;; create 16x16 html5 canvas to serve as favicon image
(defn color-favicon-data-url [color]
  (let [cvs (.createElement js/document "canvas")]
    (set! (.-width cvs) 16)
    (set! (.-height cvs) 16)
    (let [ctx (.getContext cvs "2d")]
      (set! (.-fillStyle ctx) color)
      (.fillRect ctx 0 0 16 16))
    (.toDataURL cvs)))

(defn change-favicon-to-color [color]
  (let [icon (.getElementById js/document "favicon")]
    (set! (.-href icon) (color-favicon-data-url color))))

(defmethod report [::test/default :summary] [m]
  (println "\nRan" (:test m) "tests containing"
           (+ (:pass m) (:fail m) (:error m)) "assertions.")
  (println (:fail m) "failures," (:error m) "errors.")
  (if (< 0 (+ (:fail m) (:error m)))
    (change-favicon-to-color "#d00")
    (change-favicon-to-color "#0d0")))
