(ns coursework1.core
  (:require [clojure.browser.repl :as repl]
            [cljs-http.client :as http]
            [cljs.core.async :refer [<!]]
            [reagent.core :as reagent]
            [cljs-pikaday.reagent :as pikaday]
            [cljs-time.internal.core :as internal :refer [leap-year? format]]
            [goog.date.Date]
            [goog.date.DateTime]
            [goog.date.UtcDateTime]
            [goog.i18n.TimeZone])
  (:require-macros [cljs.core.async.macros :refer [go]]
                   [reagent.ratom :refer [reaction]]))

(defonce conn
  (repl/connect "http://localhost:9000/repl"))

(defonce numberOfTotal (reagent/atom 1))

(defonce start (reagent/atom last-week-yesterday))
(defonce end (reagent/atom yesterday))
(def today (js/Date.))

(defn todayToUnix []
  (js/Date.(.getTime today))
)


(enable-console-print!)
(println today)


(defn total [url]
   (go (let [response (<! (http/get url {:with-credentials? false}))]
     (:total (:body response)))))

(enable-console-print!)
     (go
       (println (<! (total "https://api.stackexchange.com/2.2/answers?fromdate=1456790400&todate=1459382400&tagged=clojure&site=stackoverflow&filter=!bRyCgbjcxkJlK8"
                      ))))
;;(go
  ;;defn multi [(listOfUrl)]
    ;  map (println(<!(total listOfUrl))))


(defn simple-component []
  [:div
   [:p "Number of total " @numberOfTotal]
  ])

(go
  (reset! numberOfTotal (<! (total "https://api.stackexchange.com/2.2/answers?fromdate=1456790400&todate=1459382400&tagged=clojure&site=stackoverflow&filter=!bRyCgbjcxkJlK8"
                      ))))


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
     [:p "UnixTimeStamp "  ]]
  ]
)

(defn run []
  (reagent/render [simple-component](.getElementById js/document "call"))
  (reagent/render [home-page](.getElementById js/document "app"))
)










