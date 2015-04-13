(ns sheetpad.util
  (:require-macros [reagent.ratom :refer [reaction]])
  (:require [reagent.core :as reagent :refer [atom]]
            [re-frame.core :refer [register-handler
                                   path
                                   register-sub
                                   dispatch
                                   subscribe]]))

(defn print-db []
  (dispatch [:print-db]))

(defn vec-remove
  [coll pos]
  (vec (concat (subvec coll 0 pos) (subvec coll (inc pos)))))

(def delete-keycode 46)

(defn key-event-handler [handle-fn keycode]
  (fn [e]
    (when (= (.-keyCode e)
             keycode)
      (.preventDefault e)
      (handle-fn e))))
