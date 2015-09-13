(ns sheetpad.handlers
  (:require [re-frame.core :refer [register-handler
                                   path]]
            [sheetpad.util :as util]
            [sheetpad.calculate :as calc]
            [sheetpad.sheets :as sheets]))

(defn parse-item [item]
  (assoc item :parsed-value (calc/parse (item :raw-value))))

(defn calculate-item [item items]
  (assoc item :calculated-value (calc/calculate
                                  (item :parsed-value)
                                  items)))

(defn parse-and-calculate-item [item items]
  (-> item
      parse-item
      (calculate-item items)))

(def new-item
  (parse-and-calculate-item
    {:name "-"
     :raw-value ""}
    []))

(defn update-item [items item value]
  (-> item
      (assoc :raw-value value)
      (parse-and-calculate-item items)))

(defn parse-all-items [items]
  (mapv parse-item
        items))

(defn calc-all-items [items]
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
      calc-all-items))

(def initial-state
  {:sheetpad {:items [new-item]
              :sheets sheets/default-sheets}})

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
        calc-all-items)))

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

(register-handler
  :set-items
  (path [:sheetpad :items])
  (fn [items [_ new-items]]
    (-> new-items
        parse-all-items
        calc-all-items)))
