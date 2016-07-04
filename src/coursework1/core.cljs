(ns coursework1.core
  (:require [clojure.browser.repl :as repl]
            [cljs-http.client :as http]
            [cljs.core.async :refer [<!]]
            [reagent.core :as reagent]
            [cljs-pikaday.reagent :as pikaday]
            [clojure.string :as string]
            [coursework1.network :as network]
            [coursework1.date :as date]
            [cljsjs.chartist]

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
(def totalDaysSelected (reaction (date/daysBetweenDates @start @end)))



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
    [:p ""]
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
     [:p "Selected startdate in UnixTime " (date/dateInUnix @start)]
     [:p "Selected enddate in UnixTime " (date/dateInUnix @end) ]
     [:p "Days selected: " @totalDaysSelected]
     [:p "Total Values for months " @months]
     [:p [:button {:on-click #(date/getTotalMonthValues @start @end)} "Press!"]]
    ]
  ]
)


;;-----------------------
;;Charts



;;-----------------------
;;run

(defn run []
  (reagent/render [simple-component](.getElementById js/document "total"))
  (reagent/render [home-page](.getElementById js/document "app"))

)
