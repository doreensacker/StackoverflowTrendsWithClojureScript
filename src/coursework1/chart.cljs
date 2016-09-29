(ns coursework1.chart
  (:require

            [clojure.string :as string]
            [reagent.core :as reagent]
            [cljsjs.chartist]

  ))

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
  (reset! resultsAtom results)
  (reset! datesAtom months)
  (reagent/render [chart-component results months](.getElementById js/document "myChart"))
  )






