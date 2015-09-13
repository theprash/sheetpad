(ns sheetpad.subs
  (:require-macros [reagent.ratom :refer [reaction]])
  (:require [re-frame.core :refer [register-sub]]))

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
