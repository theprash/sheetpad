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

(deftest test-binary-operator
  (is (= 7
         (calc/parse-and-calculate "=2+5")
         (calc/parse-and-calculate "= 3 + 4 ")
         (calc/parse-and-calculate "=10 - 3")
         (calc/parse-and-calculate "=3.5 * 2")
         (calc/parse-and-calculate "=21 / 3"))))

(deftest test-group
  (is (= 4
         (calc/parse-and-calculate "=(4)")
         (calc/parse-and-calculate "= ( 4 ) ")
         (calc/parse-and-calculate "=(1 + 2) + 1")
         (calc/parse-and-calculate "=(5 + 7) / 3"))))
