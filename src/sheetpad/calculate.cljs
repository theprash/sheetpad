(ns sheetpad.calculate
  (:require [instaparse.core :as insta]))

(def parser
  (insta/parser
    "cell = non-formula | formula
    <non-formula> = #'[^=].*' | text | num | ''
    <formula> = <'='> formula-value
    <formula-value> = <space> (invalid | add-sub | num | quoted | item | group) <space>
    group = <'('> formula-value <')'>
    num = <space> #'(\\d|\\.)+' <space>
    <quoted> = <quote> text <quote>
    quote = '\"' | '\\''
    text = #'[^\"\\']*'
    <add-sub> = mul-div | add | sub
    add = add-sub <'+'> mul-div
    sub = add-sub <'-'> mul-div
    <mul-div> = term | mul | div
    mul = mul-div <'*'> term
    div = mul-div <'/'> term
    <term> = formula-value | <'('> add-sub <')'>
    item = <'['> #'[^\\]]+' <']'>
    space = #'\\s*'
    invalid = #'.+'"))

(defn parse [string]
  (let [remove-cell-tag second]
    (-> string
        str
        parser
        remove-cell-tag)))

(declare calculate)

(defn item-value [item-name items]
  (->> items
       (filter #(= item-name (% :name)))
       first
       :calculated-value))

(defn calculate [[tag & [a b c :as body]] items]
  (let [calc #(calculate % items)]
    (case tag
      :num (js/parseFloat a)
      :text a
      :add (+ (calc a) (calc b))
      :sub (- (calc a) (calc b))
      :mul (* (calc a) (calc b))
      :div (/ (calc a) (calc b))
      :group (calculate a items)
      :item (item-value a items)
      nil)))
