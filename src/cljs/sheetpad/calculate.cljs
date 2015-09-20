(ns sheetpad.calculate
  (:require [instaparse.core :as insta]))

(def parser
  (insta/parser
    "cell = non-formula | formula
    <non-formula> = #'[^=].*' | text | num
    <formula> = <'='> formula-value
    <formula-value> = <space> (invalid | add-sub | num | quoted | item | group) <space>
    group = <'('> formula-value <')'>
    num = <space> #'(\\+|-)?(\\d+\\.\\d+|\\d+\\.|\\.\\d+|\\d+)' <space>
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

(defn item-value [item-name items]
  (->> items
       (filter #(= item-name (% :name)))
       first
       :calculated-value))

(defn calculate-result [[tag & [a b :as body]] items]
  (let [calc #(calculate-result % items)
        fn-result (fn [f & args]
                     (let [results (map calc args)
                           failure (->> results
                                        (filter coll?)
                                        (filter (comp #{:error :invalid} first))
                                        first)]
                       (or failure
                           (apply f results))))]
    (case tag
      :num (js/parseFloat a)
      :text a
      :add (fn-result + a b)
      :sub (fn-result - a b)
      :mul (fn-result * a b)
      :div (let [b' (calc b)]
             (if (zero? b')
               (calc [:error "Divided by zero."])
               (fn-result / a b)))
      :group (calculate-result a items)
      :item (item-value a items)
      :error [:error a]
      [:invalid])))

(defn calculate [parsed items]
  (let [result (calculate-result parsed items)]
    (if (coll? result)
      (let [[tag msg] result]
        (case tag
          :error (str "#Error - " msg)
          "#Invalid formula"))
      result)))
