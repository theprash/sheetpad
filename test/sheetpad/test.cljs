(ns sheetpad.test
  (:require [cemerick.cljs.test :refer-macros [is are deftest testing use-fixtures done]]
            [sheetpad.handlers :as handlers]
            [sheetpad.render :as render]
            [sheetpad.calculate :as calc]))

(deftest test-parse-empty
  (is (= [:cell [:non-formula]]
         (calc/parse ""))))

(deftest test-parse-number
  (is (= [:cell [:non-formula [:num "123"]]]
         (calc/parse "123"))))

(deftest test-parse-formula-add
  (is (= [:cell [:formula [:num "1"] [:symbol "+"] [:num "1"]]]
         (calc/parse "=1+1")
         (calc/parse "= 1 + 1 "))))

(deftest test-parse-formula-multiply
  (is (= [:cell [:formula [:num "1"] [:symbol "*"] [:num "1"]]]
         (calc/parse "=1*1")))
  (is (= [:cell [:formula [:num "3"] [:symbol "*"] [:num "4"]]]
         (calc/parse "=3 * 4"))))

(deftest test-parse-formula-item
  (is (= [:cell [:formula [:item "a"]]]
         (calc/parse "=[a]"))))
