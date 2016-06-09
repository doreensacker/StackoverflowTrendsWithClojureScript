(ns coursework1.core
  (:require [clojure.browser.repl :as repl]
            [cljs-http.client :as http]
            [cljs.core.async :refer [<!]]
            [reagent.core :as reagent]
            [cljs-pikaday.reagent :as pikaday]
            [clojure.string :as string]
  )
  (:require-macros [cljs.core.async.macros :refer [go]]
                   [reagent.ratom :refer [reaction]]))

(defonce conn
  (repl/connect "http://localhost:9000/repl"))

;;-----------------------
;;var

(defonce numberOfTotal (reagent/atom 1))
(defonce start (reagent/atom last-week-yesterday))
(defonce end (reagent/atom yesterday))
(def today (js/Date.))
(def months (reagent/atom 0))


;;-----------------------
;;date func

(defn date? [x]
  (= (type x) js/Date))

(defn dateInUnix [whichOne]
  (let [dateInput @(whichOne {:start start :end end})]
    (if (date? dateInput)
      (/ (.getTime dateInput) 1000)
        "unselected")))

;;add 30 days in seconds 2592000
(defn addMonth [d]
  (+ d 2592000)
 )


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

(defn startMonths []
  (let [startwert (dateInUnix :start)
        endwert (dateInUnix :end)
        months (monthsBetweenDates nil startwert endwert)]
        (manyCalls months)
        (str months)
  )
)


(defn manyCalls [dates]
  (let [tmpFirst (first dates)]
        results (vector)
    (go
      (loop [counter 0 firstElem tmpFirst nextDates (rest dates)]
        (let [secondElem (first nextDates)
              newRest (rest nextDates)
              result (<! (total' firstElem secondElem "jquery"))]
          (js/console.log (string/join ["Runde " counter ": " result]))
          (conj results result)
          (if (empty? newRest)
            ()
            (recur (inc counter) secondElem newRest)
          )
        )
      )
    )
    (js/console.log (str results))
  )
)






(enable-console-print!)
(println (monthsBetweenDates nil 1459461600 1462140000))

(defn daysBetweenDates [x y]
  (when (every? date? [x y])
    (let [ms-per-day (* 1000 60 60 25)
          x-ms (.getTime x)
          y-ms (.getTime y)]
      (.round js/Math (.abs js/Math (/ (- x-ms y-ms) ms-per-day))))))

(def totalDaysSelected (reaction (daysBetweenDates @start @end)))

;;-----------------------
;;http


(defn total [url]
   (go (let [response (<! (http/get url {:with-credentials? false}))]
     (:total (:body response)))))

(defn total' [from to tag]
  (let [url (string/join ["https://api.stackexchange.com/2.2/"
                                                 "answers?"
                                                 "fromdate=" from
                                                 "&todate=" to
                                                 "&tagged=" tag
                                                 "&site=stackoverflow&filter=!bRyCgbjcxkJlK8"
        ])]
    (go (let [response (<! (http/get url {:with-credentials? false}))]
       (:total (:body response))))))


(enable-console-print!)
     (go
       (println (<! (total "https://api.stackexchange.com/2.2/answers?fromdate=1456790400&todate=1459382400&tagged=clojure&site=stackoverflow&filter=!bRyCgbjcxkJlK8"
                      ))))

(go
  (reset! numberOfTotal (<! (total (string/join ["https://api.stackexchange.com/2.2/"
                                                 "answers?"
                                                 "fromdate=" "1456790400"
                                                 "&todate=" "1459382400"
                                                 "&tagged=" "clojure"
                                                 "&site=stackoverflow&filter=!bRyCgbjcxkJlK8"
        ])))))


;;-----------------------
;;views


(defn simple-component []
  [:div
   [:p "Number of total " @numberOfTotal ]
  ])



(defn home-page []
  [:div[:h2 "Select 2 dates as a range"]
    [:div
       [:label {:for "start"} "Start date: "]
       [pikaday/date-selector
        {:date-atom start
         :max-date-atom end
         :pikaday-attrs {:max-date today}
         :input-attrs {:id "start"}
        }
       ]
    ]
    [:div
      [:label {:for "end"} "End date: "]
      [pikaday/date-selector
        {:date-atom end
         :min-date-atom start
         :pikaday-attrs {:max-date today}
         :input-attrs {:id "end"}
        }]
    ]
    [:div
     [:p "Selected startdate in UnixTime " (dateInUnix :start)]
     [:p "Selected enddate in UnixTime " (dateInUnix :end) ]
     [:p "Days selected: " @totalDaysSelected]
     [:p "Months between Dates " @months]
     [:p [:button {:on-click #(swap! months startMonths)} "Press!"]]
    ]
  ]
)

;;-----------------------
;;run

(defn run []
  (reagent/render [simple-component](.getElementById js/document "total"))
  (reagent/render [home-page](.getElementById js/document "app"))
)









