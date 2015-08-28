(ns sheetpad.calculate-tests
  (:require [cemerick.cljs.test :refer-macros [is are deftest testing use-fixtures done]]
            [sheetpad.calculate :as calc]))

(deftest test-number
  (is (= 1
         (calc/parse-and-calculate "1")
         (calc/parse-and-calculate " 1 ")
         (calc/parse-and-calculate "=1")
         (calc/parse-and-calculate "= 1 "))))

(deftest test-text
  (is (= " abc "
         (calc/parse-and-calculate " abc ")
         (calc/parse-and-calculate "=' abc '")
         (calc/parse-and-calculate "=\" abc \""))))
