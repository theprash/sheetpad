(ns sheetpad.calculate-tests
  (:require [cemerick.cljs.test :refer-macros [is are deftest testing use-fixtures done]]
            [sheetpad.calculate :as calc]))

(defn parse-and-calculate
  ([value]
   (parse-and-calculate value nil))
  ([value items]
   (-> value
       calc/parse
       (calc/calculate items))))

(deftest test-number
  (is (= 1
         (parse-and-calculate "1")
         (parse-and-calculate " 1 ")
         (parse-and-calculate "=1")
         (parse-and-calculate "= 1 "))))

(deftest test-text
  (is (= " abc "
         (parse-and-calculate " abc ")
         (parse-and-calculate "=' abc '")
         (parse-and-calculate "=\" abc \""))))

(deftest test-binary-operator
  (is (= 7
         (parse-and-calculate "=2+5")
         (parse-and-calculate "= 3 + 4 ")
         (parse-and-calculate "=10 - 3")
         (parse-and-calculate "=3.5 * 2")
         (parse-and-calculate "=21 / 3"))))

(deftest test-group
  (is (= 4
         (parse-and-calculate "=(4)")
         (parse-and-calculate "= ( 4 ) ")
         (parse-and-calculate "=(1 + 2) + 1")
         (parse-and-calculate "=(5 + 7) / 3"))))
