(ns sheetpad.core
  (:require-macros [reagent.ratom :refer [reaction]])
  (:require [reagent.core :as reagent :refer [atom]]
            [re-frame.core :refer [register-handler
                                   path
                                   register-sub
                                   dispatch
                                   subscribe]]))

(enable-console-print!)

;; Helpers
;-------------------------------------------------------------

(defn print-db []
  (dispatch [:print-db]))

(defn vec-remove
  [coll pos]
  (vec (concat (subvec coll 0 pos) (subvec coll (inc pos)))))

(defn new-editable-element [value]
  {:value value
   :editing false})

(defn new-item [name value]
  {:name (new-editable-element name)
   :value (new-editable-element value)})

(defonce initial-state
  {:sheetpad {:items []}})

(def delete-keycode 46)

(defn key-event-handler [handle-fn keycode]
  (fn [e]
    (when (= (.-keyCode e)
             keycode)
      (handle-fn e))))

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
    (conj items (new-item "Unnamed Item" (rand 10)))))

(register-handler
  :delete-item-handler
  (path [:sheetpad :items])
  (fn [items [handler-name value]]
    (vec-remove items value)))

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

(defn display-edit-control [text]
  [:input {:value text
           :read-only "true"}])

(defn render-item [item-id {n :name v :value}]
  [:div.item
   {:on-key-down (key-event-handler
                   #(dispatch [:delete-item-handler item-id])
                   delete-keycode)}
   (display-edit-control (:value n))
   ": "
   (display-edit-control (:value v))
   [:span [:button
           {:on-click #(dispatch [:delete-item-handler item-id])}
           "x"]]])

(defn items []
  (let [items-sub (subscribe [:items-sub])]
    (fn []
      (into [:div.items]
            (map-indexed render-item
                         @items-sub)))))

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
