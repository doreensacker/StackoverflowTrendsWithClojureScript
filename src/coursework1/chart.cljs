(ns coursework1.chart
  (:require

            [clojure.string :as string]
            [reagent.core :as reagent]
            [cljsjs.chartist]

  ))

(def resultsAtom (reagent/atom []))
(def datesAtom (reagent/atom []))

;;Converts unixtimestamp to date
(defn dateFromUnix [unix-time]
  (let [date (js/Date. (* unix-time 1000))
        day (.getDate date)
        month (+ 1 (.getMonth date))
        year (.getFullYear date)
        ]
    (string/join (vector day "/" month "/" year))
    )
  )

(defn labels [[x & restDates]]
    (let [next (first restDates)]
        (if (empty? restDates)
            []
            (concat [(str x " - " next)] (labels restDates))
        )))

;;Generates the chart.
(defn show-chart
  [resultsInChart monthsForResults]
  (let [chart-data {:labels (labels (mapv dateFromUnix monthsForResults))
                    :series [resultsInChart]}
        options {
                 :height "400px"
                 :chartPadding 10
                 :lineSmooth (js/Chartist.Interpolation.simple {:divisor 2})
                  }]
    (js/Chartist.Line ".ct-chart" (clj->js chart-data) (clj->js options))
   )
  )

;;Build all components to display the chart.
(defn chart-component
  [resultForChart monthsInChart]
  (let [chart (reagent/atom nil)]
    (reagent/create-class
      {
       :component-did-mount #(reset! chart (show-chart resultForChart monthsInChart))
       :component-did-update #(let [newChartData {:labels (labels (mapv dateFromUnix @datesAtom))
                                                  :series @resultsAtom}]
                                (.update @chart (clj->js newChartData))
                                )
       :display-name        "chart-component"
       :reagent-render      (fn [resultForChart monthsInChart]
                              [:div {:class "ct-chart ct-perfect-fourth"}])})))


(defn legendForChart
  [tagList]
  [:div
       [:p
           [:span.legend {:style{:color "#d70206"}} (str (nth tagList 0))]
           [:span.legend {:style{:color "#0544d3"}} (str (nth tagList 1))]
           [:span.legend {:style{:color "#55ec33"}} (str (nth tagList 2))] ]
    ]
  )

;;Render the chart in dom.
(defn renderChart [results months tag sortedTags]
    (let [new (conj @resultsAtom {:name tag :data results})
          sorted (sort #(compare (:name %1) (:name %2)) new)]
  (reset! resultsAtom sorted)
  (reset! datesAtom months)
  (reagent/render [chart-component results months](.getElementById js/document "myChart"))
  (reagent/render [legendForChart sortedTags](.getElementById js/document "legend"))
  ))

(defn clearChart []
  (reset! resultsAtom [])
  (reset! datesAtom []))
