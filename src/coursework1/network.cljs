(ns coursework1.network
  (:require
            [cljs-http.client :as http]
            [cljs.core.async :refer [<!]]
            [clojure.string :as string]
            [reagent.core :as reagent]


  )
  (:require-macros [cljs.core.async.macros :refer [go]]))


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

(defn getResultsFromStackoverflow [kind dates tag handleResult]
  (let [tmpFirst (first dates)]
    (go
      ;;Define and initialize 3 parameters to loop with.
      (loop [firstElem tmpFirst nextDates (rest dates) allResults (vector)]
        (let [secondElem (first nextDates)
              newRest (rest nextDates)
              result (<! (sendRequest kind firstElem secondElem tag))]
          (let [listOfResults (conj allResults result)]
          (if (empty? newRest)
            (handleResult listOfResults dates)
            (recur secondElem newRest listOfResults)
            )
          )
        )
      )
    )
  )
)







