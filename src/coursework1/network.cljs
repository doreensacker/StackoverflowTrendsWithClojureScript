(ns coursework1.network
  (:require
            [cljs-http.client :as http]
            [cljs.core.async :refer [<!]]
            [clojure.string :as string]
            [reagent.core :as reagent]
            [cljsjs.chartist]

  )
  (:require-macros [cljs.core.async.macros :refer [go]]))

(def resultsAtom (reagent/atom []))
(def datesAtom (reagent/atom []))


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
  (let [chart (reagent/atom nil)]
    (reagent/create-class
      {
       ;;:component-will-update #(.detach @chart)
       :component-did-mount #(reset! chart (show-chart resultForChart monthsInChart))
       :component-did-update #(let [newChartData {:labels (mapv dateFromUnix @datesAtom)
                                                  :series [@resultsAtom]}]
                                (.update @chart (clj->js newChartData))
                                )
       :display-name        "chart-component"
       :reagent-render      (fn [resultForChart monthsInChart]
                              [:div {:class "ct-chart ct-perfect-fourth"}])})))

(defn renderChart [results months]
  (reagent/render [chart-component results months](.getElementById js/document "myChart"))
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

(defn getResultsFromStackoverflow [kind dates tag]
  (let [tmpFirst (first dates)]
    (go
      ;;Define and initialize 3 parameters to loop with.
      (loop [firstElem tmpFirst nextDates (rest dates) allResults (vector)]
        (let [secondElem (first nextDates)
              newRest (rest nextDates)
              result (<! (sendRequest kind firstElem secondElem tag))]
          (let [listOfResults (conj allResults result)]
          (if (empty? newRest)
            ( (reset! resultsAtom listOfResults)
              (reset! datesAtom dates)
              (renderChart listOfResults dates)
              )
            (recur secondElem newRest listOfResults)
            )
          )
        )
      )
    )
  )
)







