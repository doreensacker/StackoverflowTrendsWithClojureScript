(ns coursework1.network
  (:require
            [cljs-http.client :as http]
            [cljs.core.async :refer [<!]]
            [clojure.string :as string]
            [reagent.core :as reagent]


  )
  (:require-macros [cljs.core.async.macros :refer [go]]))


;;Send the request to Stackoverflow to get the total number of questions
;;for a specific tag in a specific timespan
(defn sendRequest [url]
   (go (let [response (<! (http/get url {:with-credentials? false}))]
     (:total (:body response)))))

(defn generateRequest [from to tag]
  (let [url (string/join ["https://api.stackexchange.com/2.2/questions?fromdate=" from
                                                             "&todate=" to
                                                             "&tagged=" tag
                                                             "&site=stackoverflow&filter=!bRyCgbjcxkJlK8"
                                                             "&key=sFL00JQUoK8d5n9GtHiGzg(("
                          ])]
    (sendRequest url)))

;;Function requests the total number of questions from Stackoverflow
(defn getResultsFromStackoverflow [dates tag handleResult]
  ;;definde temporary variable
  (let [tmpFirst (first dates)]
    (go
      ;;Define and initialize 3 parameters to loop with.
      (loop [firstElem tmpFirst nextDates (rest dates) allResults (vector)]
        (let [secondElem (first nextDates)
              newRest (rest nextDates)
              result (<! (generateRequest firstElem secondElem tag))]
          ;;Save esults in listOfResults
          (let [listOfResults (conj allResults result)]
          ;;When all requests are made, use callbackfunction handleResults.
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







