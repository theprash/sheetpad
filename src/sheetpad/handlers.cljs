(ns sheetpad.handlers
  (:require [re-frame.core :refer [register-handler
                                   path]]
            [sheetpad.util :as util]))


(defn new-editable-element [value]
  {:value value})

(defn new-item [name value]
  {:name (new-editable-element name)
   :value (new-editable-element value)})

(defonce initial-state
  {:sheetpad {:items []}})

(register-handler
  :initialize
  (fn
    [db _]
    (merge db initial-state)))

(register-handler
  :add-item-handler
  (path [:sheetpad :items])
  (fn [items _]
    (conj items (new-item "[[unnamed]]" "-"))))

(register-handler
  :delete-item-handler
  (path [:sheetpad :items])
  (fn [items [_ value]]
    (util/vec-remove items value)))

(register-handler
  :set-value
  (path [:sheetpad :items])
  (fn [items [_ item-id item-attribute value]]
    (assoc-in items [item-id item-attribute :value] value)))
