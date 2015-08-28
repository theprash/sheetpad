(ns sheetpad.render
  (:require [reagent.core :as reagent]
            [re-frame.core :refer [dispatch
                                   subscribe]]
            [sheetpad.util :as util]))

(defn name-editor [item item-id]
  (let [name (-> item :name)]
    [:span
     [:input {:raw-value name
              :on-change #(dispatch
                            (let [new-name (-> % .-target .-value)]
                              [:set-name item-id new-name]))}]]))

(defn value-editor [item item-id]
  (let [value (-> item :raw-value)]
    [:span
     [:input {:raw-value value
              :on-change #(dispatch
                            (let [new-value (-> % .-target .-value)]
                              [:set-value item-id new-value]))}]]))

(defn item [item-id item]
  [:div.item
   {:on-key-down (util/key-event-handler
                   #(dispatch [:delete-item-handler item-id])
                   util/delete-keycode)}
   (name-editor item item-id)
   (value-editor item item-id)
   (when-let [calculated-value (-> item :calculated-value print-str)]
     [:span calculated-value])
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

(defn run []
  (reagent/render [app]
                  (js/document.getElementById "app")))
