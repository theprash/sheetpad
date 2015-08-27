(ns sheetpad.calculate
  (:require [instaparse.core :as insta]))

(def parser
  (insta/parser
    "cell = non-formula | formula
    <non-formula> = #'[^=].*' | text | num | ''
    <formula> = <'='> formula-value
    <formula-value> = invalid | (<space?> ((num | quoted | symbol | item | group) <space?>)*)
    group = <'('> formula-value <')'>
    num = <space?> #'(\\d|\\.)+' <space?>
    <quoted> = <quote> text <quote>
    quote = '\"' | '\\''
    text = #'[^\"\\']*'
    symbol = '+' | '-' | '*' | '/' | #'[a-zA-Z]\\w*'
    item = <'['> #'[^\\]]+' <']'>
    space = #'\\s+'
    invalid = #'.*'"))

(defn parse [string]
  (let [remove-cell-tag (fn [parsed]
                          (if (vector? parsed)
                            (subvec parsed 1)
                            parsed))]
    (-> string
        parser
        remove-cell-tag)))

(defn calculate [value]
  (-> value
      parse))
