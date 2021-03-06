(ns sheetpad.core
  (:require-macros [reagent.ratom :refer [reaction]])
  (:require [re-frame.core :refer [dispatch-sync]]
            [sheetpad.handlers]
            [sheetpad.subs]
            [sheetpad.render :as render]))

(enable-console-print!)

(dispatch-sync [:initialize])

(def ^:export run render/run)
