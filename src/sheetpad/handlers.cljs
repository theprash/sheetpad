(ns sheetpad.handlers
  (:require [re-frame.core :refer [register-handler
                                   path]]
            [sheetpad.util :as util]
            [sheetpad.calculate :as calc]))


(def new-item
  (let [value "-"]
    {:name "[unnamed]"
     :value value
     :calculated-value (calc/calculate value)}))

(defn calculate-value [items value]
  (calc/calculate value))

(defonce initial-state
  {:sheetpad {:items []}})

(register-handler
  :initialize
  (fn
    [db _]
    (merge db initial-state)))

(register-handler
  :add-item-handler
  (path [:sheetpad :items])
  (fn [items _]
    (conj items new-item)))

(register-handler
  :delete-item-handler
  (path [:sheetpad :items])
  (fn [items [_ value]]
    (util/vec-remove items value)))

(register-handler
  :set-name
  (path [:sheetpad :items])
  (fn [items [_ item-id name]]
    (assoc-in items [item-id :name] name)))

(register-handler
  :set-value
  (path [:sheetpad :items])
  (fn [items [_ item-id value]]
    (-> items
        (assoc-in [item-id :value] value)
        (assoc-in [item-id :calculated-value] (calculate-value items value)))))
