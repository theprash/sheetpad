(ns sheetpad.calculate-tests
  (:require [cemerick.cljs.test :refer-macros [is are deftest testing use-fixtures done]]
            [sheetpad.calculate :as calc]
            [sheetpad.handlers :as handlers]))

(defn parse-and-calculate
  ([value]
   (parse-and-calculate value nil))
  ([value items]
   (-> value
       calc/parse
       (calc/calculate items))))

(deftest test-empty
  (is (= ""
         (parse-and-calculate ""))))

(deftest test-number
  (is (= 1
         (parse-and-calculate "1")
         (parse-and-calculate " 1 ")
         (parse-and-calculate "+1")
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

(deftest test-item-reference
  (let [items [(handlers/single-item "a" 3)
               (handlers/single-item "b" 2)]]
    (is (= 3
           (parse-and-calculate "=[a]" items)))
    (is (= 5
           (parse-and-calculate "=[a]+[b]" items)))))

(deftest test-arithmetic-precedence
  (is (= 5
         (parse-and-calculate "=1*2+3")
         (parse-and-calculate "=3+1*2")
         (parse-and-calculate "=10-10/2")
         (parse-and-calculate "=-10/2 + 10"))))

(deftest test-division-error
  (is (re-find #"^#Error"
               (parse-and-calculate "=1 - 1/0"))))
