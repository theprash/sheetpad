(ns sheetpad.calculate
  (:require [instaparse.core :as insta]))

(def parser
  (insta/parser
    "cell = non-formula | formula
    <non-formula> = #'[^=].*' | text | num | ''
    <formula> = <'='> formula-value
    <formula-value> = invalid | binary | <space?> ((num | quoted | item | group) <space?>)?
    group = <'('> formula-value <')'>
    num = <space?> #'(\\d|\\.)+' <space?>
    <quoted> = <quote> text <quote>
    quote = '\"' | '\\''
    text = #'[^\"\\']*'
    binary = formula-value op formula-value
    op = '+' | '-' | '*' | '/'
    item = <'['> #'[^\\]]+' <']'>
    space = #'\\s+'
    invalid = #'.*'"))

(defn parse [string]
  (let [remove-cell-tag second]
    (-> string
        parser
        remove-cell-tag)))

(defn calculate [[tag & body]]
  (case tag
    :num (-> body first js/parseFloat)
    :text (first body)
    nil))

(defn parse-and-calculate [value]
  (-> value
      parse
      calculate))
