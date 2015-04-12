(ns sheetpad.core
  (:require-macros [reagent.ratom :refer [reaction]])
  (:require [reagent.core :as reagent :refer [atom]]
            [re-frame.core :refer [register-handler
                                   path
                                   register-sub
                                   dispatch
                                   subscribe]]))

(enable-console-print!)

(defn print-db []
  (dispatch [:print-db]))

(defonce initial-state
  {:sheetpad {:items []}})

(defn new-editable-element [value]
  {:value value
   :editing false})

(defn new-item [name value]
  {:name (new-editable-element name)
   :value (new-editable-element value)})

;; Handlers
;-------------------------------------------------------------

(register-handler
  :print-db
  (fn
    [db _]
    (print db)
    db))

(register-handler
  :initialize
  (fn
    [db _]
    (merge db initial-state)))

(dispatch [:initialize])

(register-handler
  :add-item-handler
  (path [:sheetpad :items])
  (fn [items [handler-name value]]
    (conj items (new-item "Unnamed Item" "No Value"))))

;; Subscriptions
;-------------------------------------------------------------
(register-sub
  :items-sub
  (fn
    [db _]
    (reaction (-> @db :sheetpad :items))))

;; HTML generation
;-------------------------------------------------------------

(defn display-edit-control [text]
  [:span {:on-click #(println 1)} text])

(defn render-item [{n :name v :value}]
  [:div (display-edit-control (:value n)) ": " (display-edit-control (:value v))])

(defn items []
  (let [items-sub (subscribe [:items-sub])]
    (fn []
      (into [:div.items]
            (map render-item
                 @items-sub)))))

(defn add-item []
  [:div.add-item
   [:button
    {:on-click #(dispatch [:add-item-handler])}
    "Add"]])

(defn app []
  [:div
   [items]
   [add-item]])

(defn ^:export run []
  (reagent/render [app]
                  (js/document.getElementById "app")))
