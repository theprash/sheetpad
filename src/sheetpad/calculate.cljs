(ns sheetpad.calculate
  (:require [instaparse.core :as insta]))

(def parser
  (insta/parser
    "cell = non-formula | formula
    <non-formula> = '' | #'[^=].*' | num | string
    <formula> = <'='> <space?> ((num | string | symbol | item) <space?>)*
    num = <space?> #'(\\d|\\.)+' <space?>
    string = #'\"[^\"]*\"'
    symbol = '+' | '-' | '*' | '/' | #'[a-zA-Z]\\w*'
    item = <'['> #'[^\\]]+' <']'>
    space = #'\\s+'"))

(defn parse [string]
  (let [remove-cell-tag
        (fn [parsed] (if (vector? parsed)
                       (subvec parsed 1)
                       parsed))]
    (-> string
        parser
        remove-cell-tag)))

(defn calculate [value]
  (-> value
      parse))
