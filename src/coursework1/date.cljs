(ns coursework1.date
  (:require [clojure.browser.repl :as repl]
            [cljs-http.client :as http]
            [cljs.core.async :refer [<!]]
            [reagent.core :as reagent]
            [cljs-pikaday.reagent :as pikaday]
            [clojure.string :as string]
            [coursework1.network :as network]

  )
  (:require-macros [cljs.core.async.macros :refer [go]]
                   [reagent.ratom :refer [reaction]]))



(defn date? [x]
  (= (type x) js/Date))

;; ;;add 30 days in seconds 2592000
;; (defn addMonth [d]
;;   (+ d 2592000)
;;  )



(defn dateInUnix [dateInput]
    (if (date? dateInput)
      (/ (.getTime dateInput) 1000)
        "unselected"))


(defn monthsBetweenDates [dates firstDate endDate]
  (if (nil? dates)
     (monthsBetweenDates (vector firstDate) firstDate endDate)
     (let [nextDate (+ (peek dates) 2592000)]
       (if (>= nextDate endDate)
             (conj dates (int endDate))
             (monthsBetweenDates (conj dates nextDate) firstDate endDate)
       )
     )
  )
)


(defn daysBetweenDates [x y]
  (when (every? date? [x y])
    (let [ms-per-day (* 1000 60 60 25)
          x-ms (.getTime x)
          y-ms (.getTime y)]
      (.round js/Math (.abs js/Math (/ (- x-ms y-ms) ms-per-day))))))
