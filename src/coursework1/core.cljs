(ns ^:figwheel-always coursework1.core
  (:require [clojure.browser.repl :as repl]
            [cljs-http.client :as http]
            [cljs.core.async :refer [<!]]
            [reagent.core :as reagent]
            [cljs-pikaday.reagent :as pikaday]
            [clojure.string :as string]
            [coursework1.network :as network]
            [coursework1.date :as date]
            [coursework1.chart :as charting]

            [cljsjs.chartist]

  )
  (:require-macros [cljs.core.async.macros :refer [go]]
                   [reagent.ratom :refer [reaction]]))

;; (defonce conn
;;   (repl/connect "http://localhost:9000/repl"))


;;(defonce app-state (atom {:text "Hello world!"}))
(enable-console-print!)

;;-----------------------
;;var

(enable-console-print!)


(defn do-something-x-y
  [x y]
  (+ x y))



(def today (js/Date.))
(defonce start (reagent/atom today))
(defonce end (reagent/atom today))

(defonce tag1 (reagent/atom ""))
(defonce tag2 (reagent/atom ""))
(defonce tag3 (reagent/atom ""))

(defonce posts (reagent/atom "posts"))
(defonce answers (reagent/atom "answers"))
(defonce questions (reagent/atom "questions"))

(def months (reagent/atom 0))
(def totalDaysSelected (reaction (date/daysBetweenDates @start @end)))

(def inputFieldsNotEmpty
  (or
       (not= @start "undselected")
       (not= @end "undselected")) )

(defn handleResult [listOfResults dates]
  (charting/renderChart listOfResults dates)
  )

(defn drawChart [kindOfPost]
  (if inputFieldsNotEmpty
        (let [monthsInTimeSpan (date/monthsBetweenDates nil (date/dateInUnix @start) (date/dateInUnix @end))]
          (charting/clearChart)

          (if (not= @tag1 "")
            (network/getResultsFromStackoverflow kindOfPost monthsInTimeSpan @tag1 handleResult))

          (if (not= @tag2 "")
            (network/getResultsFromStackoverflow kindOfPost monthsInTimeSpan @tag2 handleResult))

          (if (not= @tag3 "")
            (network/getResultsFromStackoverflow kindOfPost monthsInTimeSpan @tag3 handleResult))
          )
  )
)




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
       [:p "Enter TAG : " [inputField tag1]]
       [:p "Enter TAG : " [inputField tag2]]
       [:p "Enter TAG : " [inputField tag3]]
    ]
    [:div
     [:p [:button {:on-click #(drawChart @questions)} "Show Chart!"]
      ]
    ]

  ]
)

;;-----------------------
;;run

(defn run []
  (reagent/render [home-page](.getElementById js/document "app"))
)
