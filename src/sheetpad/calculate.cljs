(ns sheetpad.calculate
  (:require [instaparse.core :as insta]))

(def parse
  (insta/parser
    "cell = non-formula | formula
     non-formula = #'[^=].*' | '' | num | string
     formula = <'='> <space?> ((num | string | symbol | item) <space?>)*
     num = #'(\\d|\\.)+'
     string = #'\"[^\"]*\"'
     symbol = '+' | '-' | '*' | '/' | #'[a-zA-Z]\\w*'
     item = <'['> #'[^\\]]+' <']'>
     space = #'\\s+'"))

(defn calculate [value]
  (print-str (-> value
                 parse)))
