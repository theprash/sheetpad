(ns sheetpad.test
  (:require [cemerick.cljs.test :refer-macros [is are deftest testing use-fixtures done]]
            [sheetpad.handlers :as handlers]
            [sheetpad.render :as render]
            [sheetpad.calculate :as calc]))

(deftest test-add
  (is (= [:cell [:formula [:num "1"] [:symbol "+"] [:num "1"]]]
         (calc/parser "=1+1"))))

(deftest test-multiply
  (is (= [:cell [:formula [:num "1"] [:symbol "*"] [:num "1"]]]
         (calc/parser "=1 * 1"))))
