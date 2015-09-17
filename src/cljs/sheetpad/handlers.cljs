(ns sheetpad.handlers
  (:require [re-frame.core :refer [register-handler
                                   path
                                   dispatch]]
            [sheetpad.util :as util]
            [sheetpad.calculate :as calc]
            [cljs-http.client :as http]
            [cljs.core.async :refer [<!]])
  (:require-macros [cljs.core.async.macros :refer [go]]))

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
  {:sheetpad {:items [new-item]}
   :sheets []
   :save-sheet-name ""})

(register-handler
  :initialize
  (fn
    [db _]
    (dispatch [:get-sheet-names])
    (merge db initial-state)))

(register-handler
  :update-sheet-names
  (path [:sheets])
  (fn [_ [_ sheet-names]]
    sheet-names))

(register-handler
  :get-sheet-names
  (fn [db _]
    (go
      (let [response (<! (http/get "/sheets"))
            sheet-names (-> response :body cljs.reader/read-string)]
        (dispatch [:update-sheet-names sheet-names])))
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
    (go
      (let [response (<! (http/get (str "/sheets/" sheet-name)))
            items (-> response :body cljs.reader/read-string :items)]
        (dispatch [:set-items items])))
    db))

(register-handler
  :set-save-sheet-name
  (path [:save-sheet-name])
  (fn [_ [_ new-name]]
    new-name))

(register-handler
  :save-sheet
  (fn [db _]
    (go
      (let [response (<! (http/post "/save-sheet"
                                    {:edn-params {:sheet-name (db :save-sheet-name)
                                                  :items (-> db :sheetpad :items)}}))
            sheet-names (-> response :body cljs.reader/read-string)]
        (dispatch [:update-sheet-names sheet-names])))
    db))

(register-handler
  :delete-sheet
  (fn [db [_ sheet-name]]
    (go
      (let [response (<! (http/post "/delete-sheet"
                                    {:edn-params {:sheet-name sheet-name}}))
            sheet-names (-> response :body cljs.reader/read-string)]
        (dispatch [:update-sheet-names sheet-names])))
    db))
