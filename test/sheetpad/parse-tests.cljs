(ns sheetpad.parse-tests
  (:require [cemerick.cljs.test :refer-macros [is are deftest testing use-fixtures done]]
            [sheetpad.calculate :refer [parse]]))

(deftest test-empty
  (is (= nil
         (parse ""))))

(deftest test-number
  (is (= [:num "123"]
         (parse "123")
         (parse "+123")
         (parse " 123 ")))
  (is (= [:num "-123.0"]
         (parse "-123.0")
         (parse " -123.0 ")))
  (is (= [:num ".1"] (parse ".1")))
  (is (= [:num "-1."] (parse "-1."))))

(deftest test-text
  (is (= [:text "abc"]
         (parse "abc"))))

(deftest test-formula-add
  (is (= [:add [:num "1"] [:num "1"]]
         (parse "=1+1")
         (parse "= 1 + 1 "))))

(deftest test-formula-multiply
  (is (= [:mul [:num "1"] [:num "1"]]
         (parse "=1*1")))
  (is (= [:mul [:num "3"] [:num "4"]]
         (parse "=3 * 4 "))))

(deftest test-formula-item
  (is (= [:item "a"]
         (parse "=[a]")
         (parse "= [a] "))))

(deftest test-formula-text
  (is (= [:text "abc"]
         (parse "=\"abc\"")
         (parse "='abc'")))
  (is (= [:text ""]
         (parse "=\"\"")
         (parse "=''"))))

(deftest test-formula-group
  (is (= [:group [:num "1"]]
         (parse "=(1)")
         (parse "= ( 1 ) ")))
  (is (= [:group [:text ""]]
         (parse "=('')")))
  (is (= [:div [:num "1"]
          [:group
           [:mul
            [:group [:add [:num "2"] [:num "3"]]]
            [:num "4"]]]]
         (parse "= 1 / ((2 + 3) * 4)"))))

(deftest test-formula-invalid
  (is (= [:invalid "$`"]
         (parse "=$`"))))
