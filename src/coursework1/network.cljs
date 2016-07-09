(ns coursework1.network
  (:require
            [cljs-http.client :as http]
            [cljs.core.async :refer [<!]]
            [clojure.string :as string]
            [reagent.core :as reagent]

  )
  (:require-macros [cljs.core.async.macros :refer [go]]))

(defn makeManyCalls [kind dates tag]
  (let [tmpFirst (first dates)]
    (go
      (loop [firstElem tmpFirst nextDates (rest dates) allResults (vector)]
        (let [secondElem (first nextDates)
              newRest (rest nextDates)
              result (<! (sendRequest kind firstElem secondElem tag))]
          (let [listOfResults (conj allResults result)]
          (if (empty? newRest)
            (renderChart listOfResults dates)
            (recur secondElem newRest listOfResults)
            )
          )
        )
      )
    )
  )
)

(defn callback[results]
   (js/console.log (string/join ["Results" results]))

  )

(defn getFromUrl [url]
   (go (let [response (<! (http/get url {:with-credentials? false}))]
     (:total (:body response)))))

(defn sendRequest [kind from to tag]
  (let [url (string/join ["https://api.stackexchange.com/2.2/"
                                                 kind "?"
                                                 "fromdate=" from
                                                 "&todate=" to
                                                 "&tagged=" tag
                                                 "&site=stackoverflow&filter=!bRyCgbjcxkJlK8"
                                                 "&key=sFL00JQUoK8d5n9GtHiGzg(("
                          ])]
    (getFromUrl url)))


;;----------
;;chart

(defn dateFromUnix [unix-time]
  (let [date (js/Date. (* unix-time 1000))
        day (.getDate date)
        month (+ 1 (.getMonth date))
        year (.getFullYear date)
        ]
    (string/join (vector day "/" month "/" year))
    )
  )

(defn firstChart[results monthsForResults]
  (let [chart-data {:labels (mapv dateFromUnix monthsForResults)
                    :series  [results]}
        options {:width  "700px"
                 :height "380px"}]
    (js/Chartist.Line. ".ct-chart" (clj->js chart-data) (clj->js options))))


(defn showChart [resultValues monthValues]
  (let [some "state goes here"]
    (reagent/create-class
      {:component-did-mount #(firstChart resultValues monthValues )
       :display-name        "chart-component"
       :reagent-render      (fn []
                              [:div {:class "ct-chart ct-perfect-fourth"}])})))

(defn renderChart [ress mon]
  (reagent/render [showChart ress mon](.getElementById js/document "myChart")))

;;-------
;;Test

;; (enable-console-print!)
;;      (go
;;        (println (<! (total "https://api.stackexchange.com/2.2/answers?fromdate=1456790400&todate=1459382400&tagged=clojure&site=stackoverflow&filter=!bRyCgbjcxkJlK8"
;;                       ))))

;; (go
;;   (reset! numberOfTotal (<! (total (string/join ["https://api.stackexchange.com/2.2/"
;;                                                  "answers?"
;;                                                  "fromdate=" "1456790400"
;;                                                  "&todate=" "1459382400"
;;                                                  "&tagged=" "clojure"
;;                                                  "&site=stackoverflow&filter=!bRyCgbjcxkJlK8"
;;         ])))))
