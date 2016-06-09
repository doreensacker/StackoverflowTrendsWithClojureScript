(ns coursework1.network
  (:require
            [cljs-http.client :as http]
            [cljs.core.async :refer [<!]]
            [clojure.string :as string]
  )
  (:require-macros [cljs.core.async.macros :refer [go]]))

(defn makeManyCalls [dates nextFunc]
  (let [tmpFirst (first dates)]
    (go
      (loop [counter 0 firstElem tmpFirst nextDates (rest dates) allResults (vector)]
        (let [secondElem (first nextDates)
              newRest (rest nextDates)
              result (<! (sendRequest firstElem secondElem "jquery"))]
          (js/console.log (string/join ["Runde " counter ": " result]))
          (let [listOfResults (conj allResults result)]
          (if (empty? newRest)
            (nextFunc listOfResults)
            (recur (inc counter) secondElem newRest listOfResults)
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

(defn sendRequest [from to tag]
  (let [url (string/join ["https://api.stackexchange.com/2.2/"
                                                 "answers?"
                                                 "fromdate=" from
                                                 "&todate=" to
                                                 "&tagged=" tag
                                                 "&site=stackoverflow&filter=!bRyCgbjcxkJlK8"
                                                 "&key=sFL00JQUoK8d5n9GtHiGzg(("
                          ])]
    (getFromUrl url)))

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
