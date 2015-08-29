(ns sheetpad.core
  (:require-macros [reagent.ratom :refer [reaction]])
  (:require [reagent.core :as reagent :refer [atom]]
            [re-frame.core :refer [register-sub
                                   dispatch-sync]]
            [sheetpad.handlers :as handlers]
            [sheetpad.render :as render]
            [sheetpad.calculate :as calc]))

(enable-console-print!)

(dispatch-sync [:initialize])

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

(def ^:export run render/run)
