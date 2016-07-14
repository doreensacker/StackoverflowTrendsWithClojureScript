(ns coursework1.network
  (:require
            [cljs-http.client :as http]
            [cljs.core.async :refer [<!]]
            [clojure.string :as string]
            [reagent.core :as reagent]
            [cljsjs.chartist]

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

;; (defn callback[results]
;;    (js/console.log (string/join ["Results" results]))

;;   )

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


(defn show-chart
  [resultsInChart monthsForResults]
  (let [chart-data {:labels (mapv dateFromUnix monthsForResults)
                    :series [resultsInChart]}
        options {:width  "700px"
                 :height "380px"}]
    (js/Chartist.Line ".ct-chart" (clj->js chart-data) (clj->js options))
   )
  )

(defn chart-component
  [resultForChart monthsInChart]
  (let [chart (reagent/atom nil) ]
    (js/console.log "chart-component")
    (reagent/create-class
      {
       :component-will-mount #(js/console.log "willmount")
       :component-will-update #(let [newChartData {:labels (mapv dateFromUnix monthsInChart)
                                                  :series [resultForChart]}]
                                (js/console.log newChartData)
                                (.update @chart (clj->js chart-data))
                                ;;(show-chart resultForChart monthsInChart)
                                )
                                 ;;.detach @chart
                                 ;;(.detach js/Chartist ".ct-chart")
       :component-did-mount #(reset! chart (show-chart resultForChart monthsInChart))
       :component-did-update #()
       :display-name        "chart-component"
       :reagent-render      (fn [resultForChart monthsInChart]
                              [:div {:class "ct-chart ct-perfect-fourth"}])})))

(defn renderChart [results months]
  (reagent/render [chart-component results months](.getElementById js/document "myChart"))
  )
