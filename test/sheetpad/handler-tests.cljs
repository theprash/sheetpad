(ns sheetpad.handler-tests
  (:require [cemerick.cljs.test :refer-macros [is are deftest testing use-fixtures done]]
            [sheetpad.handlers :as handlers]))

(defn setup-items [& args]
  (let [item-from-pair (fn [[name value]]
                         (merge handlers/new-item {:name name :raw-value value}))]
    (as-> args <>
      (partition 2 <>)
      (mapv item-from-pair <>)
      (mapv #(handlers/parse-and-calculate-item % <>) <>)
      (handlers/recalc-all-items <>))))

(deftest test-item-references
  (let [items (setup-items "a" "1"
                           "b" "=[a]+1"
                           "c" "=[b]+1")]
    (is (= 3
           (-> items
               last
               :calculated-value)))
    (is (= 4
           (-> items
               (#(handlers/set-value-handler 0 "2" %))
               last
               :calculated-value)))))
