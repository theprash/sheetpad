(ns sheetpad.handlers
  (:require [re-frame.core :refer [register-handler
                                   path]]
            [sheetpad.util :as util]
            [sheetpad.calculate :as calc]))

(def new-item
  {:name "-"
   :raw-value ""
   :parsed-value nil
   :calculated-value nil})

(defn calculate-item [item items]
  (let [calculated (calc/calculate (item :parsed-value) items)]
    (merge item {:calculated-value calculated})))

(defn parse-and-calculate-item [item items]
  (let [parsed (calc/parse (item :raw-value))
        calculated (calc/calculate parsed items)]
    (merge item {:parsed-value parsed
                 :calculated-value calculated})))

(defn update-item [items item value]
  (-> item
      (assoc :raw-value value)
      (parse-and-calculate-item items)))

(defn recalc-all-items [items]
  (let [item-count (count items)]
    (loop [index 0
           items items]
      (if (= index item-count)
        items
        (recur
          (inc index)
          (update-in items [index] #(calculate-item % items)))))))

(defn set-value-handler [item-id value items]
  (-> items
      (update-in [item-id] #(update-item items % value))
      recalc-all-items))

(defn app-state [items]
  {:sheetpad {:items items}})

(def initial-state
  (app-state [new-item]))

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
    (-> items
        (util/vec-remove value)
        recalc-all-items)))

(register-handler
  :set-name
  (path [:sheetpad :items])
  (fn [items [_ item-id name]]
    (assoc-in items [item-id :name] name)))

(register-handler
  :set-value
  (path [:sheetpad :items])
  (fn [items [_ item-id value]]
    (set-value-handler item-id value items)))
