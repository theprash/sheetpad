(ns sheetpad.handlers
  (:require [re-frame.core :refer [register-handler
                                   path
                                   dispatch]]
            [sheetpad.util :as util]
            [sheetpad.calculate :as calc]
            [ajax.core :as ajax]
            [cljs.reader]))

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
              :sheets []}})

(register-handler
  :initialize
  (fn
    [db _]
    (dispatch [:get-sheet-names])
    (merge db initial-state)))

(register-handler
  :update-sheet-names
  (path [:sheetpad :sheets])
  (fn [_ [_ sheet-names]]
    sheet-names))

(register-handler
  :get-sheet-names
  (fn [db _]
    (ajax/GET "/sheets"
              {:handler (fn [r]
                          (dispatch
                            [:update-sheet-names (cljs.reader/read-string r)]))})
    db))

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

(register-handler
  :get-and-set-items
  (fn [db [_ sheet-name]]
    (ajax/GET (str "/sheets/" sheet-name)
              {:handler (fn [r]
                          (let [items (-> r cljs.reader/read-string :items)]
                            (dispatch [:set-items items])))})
    db))

(register-handler
  :delete-sheet
  (path [:sheetpad :items])
  (fn [db [_ sheet-name]]
    (ajax/POST (str "/delete-sheet/" sheet-name)
               {:handler (fn [r]
                           (dispatch [:get-sheet-names]))})
    db))
