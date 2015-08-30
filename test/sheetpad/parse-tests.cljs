(ns sheetpad.parse-tests
  (:require [cemerick.cljs.test :refer-macros [is are deftest testing use-fixtures done]]
            [sheetpad.calculate :refer [parse]]))

(deftest test-empty
  (is (= nil
         (parse ""))))

(deftest test-number
  (is (= [:num "123"]
         (parse "123")
         (parse " 123 "))))

(deftest test-text
  (is (= [:text "abc"]
         (parse "abc"))))

(deftest test-formula-add
  (is (= [:binary [:num "1"] [:op "+"] [:num "1"]]
         (parse "=1+1")
         (parse "= 1 + 1 "))))

(deftest test-formula-multiply
  (is (= [:binary [:num "1"] [:op "*"] [:num "1"]]
         (parse "=1*1")))
  (is (= [:binary [:num "3"] [:op "*"] [:num "4"]]
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
  (is (= [:binary [:num "1"]
          [:op "/"]
          [:group
           [:binary
            [:group [:binary [:num "2"] [:op "+"] [:num "3"]]]
            [:op "*"]
            [:num "4"]]]]
         (parse "= 1 / ((2 + 3) * 4)"))))

(deftest test-formula-invalid
  (is (= [:invalid "$`"]
         (parse "=$`"))))
