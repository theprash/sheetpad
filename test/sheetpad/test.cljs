(ns sheetpad.test
  (:require [cemerick.cljs.test :refer-macros [is are deftest testing use-fixtures done]]
            [sheetpad.handlers :as handlers]
            [sheetpad.render :as render]
            [sheetpad.calculate :as calc]))

(deftest test-parse-empty
  (is (= []
         (calc/parse ""))))

(deftest test-parse-number
  (is (= [[:num "123"]]
         (calc/parse "123"))))

(deftest test-parse-formula-add
  (is (= [[:num "1"] [:symbol "+"] [:num "1"]]
         (calc/parse "=1+1")
         (calc/parse "= 1 + 1 "))))

(deftest test-parse-formula-multiply
  (is (= [[:num "1"] [:symbol "*"] [:num "1"]]
         (calc/parse "=1*1")))
  (is (= [[:num "3"] [:symbol "*"] [:num "4"]]
         (calc/parse "=3 * 4"))))

(deftest test-parse-formula-item
  (is (= [[:item "a"]]
         (calc/parse "=[a]"))))
