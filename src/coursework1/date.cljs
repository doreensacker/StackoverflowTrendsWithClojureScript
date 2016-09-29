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



(defn date? "Check if var is a date"[x]
  (= (type x) js/Date))

(defn dateInUnix "Convert var to unixtimestamp" [dateInput]
    (if (date? dateInput)
      (/ (.getTime dateInput) 1000)
        "unselected"))

;;Calculates the beginning unixdate for each month between the two given dates.
;;Returns a list of unixdates, which are the months between the given dates.
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

"Returns the number of days between two given dates."
(defn daysBetweenDates [x y]
  (when (every? date? [x y])
    (let [ms-per-day (* 1000 60 60 25)
          x-ms (.getTime x)
          y-ms (.getTime y)]
      (.round js/Math (.abs js/Math (/ (- x-ms y-ms) ms-per-day))))))
