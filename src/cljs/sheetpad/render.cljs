(ns sheetpad.render
  (:require [reagent.core :as reagent]
            [re-frame.core :refer [dispatch
                                   dispatch-sync
                                   subscribe]]
            [cljs.pprint]))

(defn name-editor [item item-id]
  (let [name (-> item :name)]
    [:span
     [:input {:value name
              :on-change #(let [new-name (-> % .-target .-value)]
                            (dispatch-sync [:set-name item-id new-name]))}]]))

(defn value-editor [item item-id]
  (let [value (-> item :raw-value)]
    [:span
     [:input {:value value
              :on-change #(let [new-value (-> % .-target .-value)]
                            (dispatch-sync [:set-value item-id new-value]))}]]))

(defn calculated-value-display [value]
  [:input {:value value :read-only true}])

(defn make-item [item-id item]
  (cond
    (:raw-value item) [:div.item
                       (name-editor item item-id)
                       (value-editor item item-id)
                       (calculated-value-display (-> item :calculated-value))
                       [:span [:button
                               {:on-click #(dispatch [:delete-item item-id])}
                               "x"]]]
    (:columns item) (into [:div.item]
                          (map #(into [:div] (map-indexed make-item %))
                               (:columns item)))))

(defn items []
  (let [items-sub (subscribe [:items-sub])]
    (fn []
      (into [:div.items]
            (map-indexed make-item @items-sub)))))

(defn add-single-item []
  [:div.add-item
   [:button
    {:on-click #(dispatch [:add-single-item])}
    "Add"]])

(defn add-table-item []
  [:div.add-item
   [:button
    {:on-click #(dispatch [:add-table-item])}
    "Add Table"]])

(defn load-sheets []
  [:div "Load sheet:"
   (map (fn [sheet-name]
          [:div
           {:key sheet-name}
           [:a
            {:href "#"
             :on-click #(dispatch [:get-and-set-items sheet-name])}
            sheet-name]
           [:button {:on-click #(dispatch [:delete-sheet sheet-name])} "x"]])
        @(subscribe [:sheets-sub]))])

(defn save-sheet []
  [:div
   [:input {:value @(subscribe [:save-sheet-name-sub])
            :on-change #(let [new-value (-> % .-target .-value)]
                          (dispatch-sync [:set-save-sheet-name new-value]))}]
   [:button {:on-click #(dispatch [:save-sheet])} "Save sheet"]])

(defn items-view []
  [:div.items-view
   (with-out-str (cljs.pprint/pprint @(subscribe [:items-sub])))])

(defn app []
  [:div
   [items]
   [add-single-item]
   [add-table-item]
   [load-sheets]
   [save-sheet]
   [items-view]])

(defn run []
  (reagent/render [app]
                  (js/document.getElementById "app")))
