(ns sheetpad.calculate
  (:require [instaparse.core :as insta]))

(def parser
  (insta/parser
    "cell = non-formula | formula
    <non-formula> = #'[^=].*' | '' | num | string
    <formula> = <'='> <space?> ((num | string | symbol | item) <space?>)*
    num = #'(\\d|\\.)+'
    string = #'\"[^\"]*\"'
    symbol = '+' | '-' | '*' | '/' | #'[a-zA-Z]\\w*'
    item = <'['> #'[^\\]]+' <']'>
    space = #'\\s+'"))

(defn parse [string]
  (let [remove-cell-tag (fn [parsed] (subvec parsed 1))]
    (-> string
        parser
        remove-cell-tag)))

(defn calculate [value]
  (-> value
      parse))
