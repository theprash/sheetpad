(ns sheetpad.handlers
  (:require [re-frame.core :refer [register-handler
                                   path]]
            [sheetpad.util :as util]
            [sheetpad.calculate :as calc]))

(def new-item
  {:name "[unnamed]"
   :raw-value "-"
   :parsed-value nil
   :calculated-value nil})

(defn update-item [item value items]
  (let [parsed (calc/parse value)
        calculated (calc/calculate parsed items)]
    (merge item {:raw-value value
                 :parsed-value parsed
                 :calculated-value calculated})))

(defn update-item-id [item-id value items]
  (-> items
      (update-in [item-id] #(update-item % value items))))

(defonce initial-state
  {:sheetpad {:items [new-item]}})

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
    (update-item-id item-id value items)))
