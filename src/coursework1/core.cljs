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

(defonce start (reagent/atom today))
(defonce end (reagent/atom today))
(defonce tag (reagent/atom ""))
(def today (js/Date.))
(def months (reagent/atom 0))
(def totalDaysSelected (reaction (date/daysBetweenDates @start @end)))


(defn inputField [value]
  [:input {:type "text"
           :value @value
           :on-change #(reset! value (-> % .-target .-value))}])



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
       [:p "Enter TAG : " [inputField tag]]
    ]
    [:div
     [:p [:button {:on-click #((if (or (not= tag "" ) (not= start "undselected") (not= end "undselected"))
                                (date/getTotalMonthValues "questions" @start @end @tag)))} "Get Questions!"]
         [:button {:on-click #((if (or (not= tag "" ) (not= start "undselected") (not= end "undselected"))
                                (date/getTotalMonthValues "answers" @start @end @tag)))} "Get Answers!"]
         [:button {:on-click #((if (or (not= tag "" ) (not= start "undselected") (not= end "undselected"))
                                (date/getTotalMonthValues "posts" @start @end @tag)))} "Get Posts!"]
      ]
    ]

  ]
)


;;-----------------------
;;Charts



;;-----------------------
;;run

(defn run []
  (reagent/render [home-page](.getElementById js/document "app"))

)
