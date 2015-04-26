(ns sheetpad.calculate
  (:require [instaparse.core :as insta]))

(enable-console-print!)

(def parser
  (insta/parser
    "cell = non-formula | formula
     formula = '=' space? (symbol | num | string)? space?
     num = #'(\\d|\\.)+'
     string = #'\".*\"'
     non-formula = #'.*'
     symbol = #'(\\w|-)+'
     space = #'\\s+'"))

(defn calculate [value] (print-str (parser value)))
