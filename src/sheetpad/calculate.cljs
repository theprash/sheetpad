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

(declare calculate)

(defn calculate-binary [a [_ op] b items]
  (let [calc #(calculate % items)]
    (case op
      "+" (+ (calc a) (calc b))
      "-" (- (calc a) (calc b))
      "*" (* (calc a) (calc b))
      "/" (/ (calc a) (calc b))
      nil)))

(defn item-value [item-name items]
  (->> items
       (filter #(= item-name (% :name)))
       first
       :calculated-value))

(defn calculate [[tag & [a b c :as body]] items]
  (case tag
    :num (js/parseFloat a)
    :text a
    :binary (calculate-binary a b c items)
    :group (calculate a items)
    :item (item-value a items)
    nil))
