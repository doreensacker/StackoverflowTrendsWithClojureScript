(ns coursework1.date
  (:require [clojure.browser.repl :as repl]
            [cljs-http.client :as http]
            [cljs.core.async :refer [<!]]
            [reagent.core :as reagent]
            [cljs-pikaday.reagent :as pikaday]
            [clojure.string :as string]
            [coursework1.network :as network]
            [clojure.string :as string]

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


(defn monthsBetweenDates [dates firstDate ende]
  (if (nil? dates)
     (monthsBetweenDates (vector firstDate) firstDate ende)
     (let [nextDate (+ (peek dates) 2592000)]
       (if (>= nextDate ende)
             (conj dates ende)
             (monthsBetweenDates (conj dates nextDate) firstDate ende)
       )
     )
  )
)

(defn getTotalMonthValues [kind start end tag]
  (let [startwert (dateInUnix start)
        endwert (dateInUnix end)
        months (monthsBetweenDates nil startwert endwert)]
        (network/makeManyCalls kind months tag)
  )
)

(defn daysBetweenDates [x y]
  (when (every? date? [x y])
    (let [ms-per-day (* 1000 60 60 25)
          x-ms (.getTime x)
          y-ms (.getTime y)]
      (.round js/Math (.abs js/Math (/ (- x-ms y-ms) ms-per-day))))))
