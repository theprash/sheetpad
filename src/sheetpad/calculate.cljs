(ns sheetpad.calculate
  (:require [instaparse.core :as insta]))

(enable-console-print!)

(def parser
  (insta/parser
    "cell = non-formula | formula
     non-formula = #'[^=].*' | ''
     formula = '=' <space?> ((symbol | num | string) <space?>)*
     num = #'(\\d|\\.)+'
     string = #'\"[^\"]*\"'
     symbol = #'[^0-9\\.]' #'\\S*'
     space = #'\\s+'"))

(defn calculate [value] (print-str (parser value)))
