(ns sheetpad.core
  (:require-macros [reagent.ratom :refer [reaction]])
  (:require [reagent.core :as reagent :refer [atom]]
            [re-frame.core :refer [register-sub
                                   dispatch
                                   subscribe]]
            [sheetpad.util :as util]
            [sheetpad.handlers :as handlers]))

(enable-console-print!)

(dispatch [:initialize])

;; Subscriptions
;-------------------------------------------------------------
(register-sub
  :sheetpad-sub
  (fn
    [db _]
    (reaction (-> @db :sheetpad))))

(register-sub
  :items-sub
  (fn
    [db _]
    (reaction (-> @db :sheetpad :items))))

;; HTML generation
;-------------------------------------------------------------

(defn display-edit-control [item item-id item-attribute]
  (let [value (-> item item-attribute :value)]
    [:input {:value value
             :on-change #(dispatch [:set-value item-id item-attribute (-> % .-target .-value)])}]))

(defn item [item-id item]
  [:div.item
   {:on-key-down (util/key-event-handler
                   #(dispatch [:delete-item-handler item-id])
                   util/delete-keycode)}
   (display-edit-control item item-id :name)
   ": "
   (display-edit-control item item-id :value)
   [:span [:button
           {:on-click #(dispatch [:delete-item-handler item-id])}
           "x"]]])

(defn items []
  (let [items-sub (subscribe [:items-sub])]
    (fn []
      (into [:div.items]
            (map-indexed item @items-sub)))))

(defn add-item []
  [:div.add-item
   [:button
    {:on-click #(dispatch [:add-item-handler])}
    "Add"]])

(defn db-view []
  [:div.db-view
   [:div (print-str @(subscribe [:sheetpad-sub]))]])

(defn app []
  [:div
   [items]
   [add-item]
   [db-view]])

(defn ^:export run []
  (reagent/render [app]
                  (js/document.getElementById "app")))
