(ns sheetpad.parse-tests
  (:require [cemerick.cljs.test :refer-macros [is are deftest testing use-fixtures done]]
            [sheetpad.calculate :as calc]))

(deftest test-empty
  (is (= nil
         (calc/parse ""))))

(deftest test-number
  (is (= [:num "123"]
         (calc/parse "123")
         (calc/parse " 123 "))))

(deftest test-text
  (is (= [:text "abc"]
         (calc/parse "abc"))))

(deftest test-formula-add
  (is (= [:binary [:num "1"] [:op "+"] [:num "1"]]
         (calc/parse "=1+1")
         (calc/parse "= 1 + 1 "))))

(deftest test-formula-multiply
  (is (= [:binary [:num "1"] [:op "*"] [:num "1"]]
         (calc/parse "=1*1")))
  (is (= [:binary [:num "3"] [:op "*"] [:num "4"]]
         (calc/parse "=3 * 4 "))))

(deftest test-formula-item
  (is (= [:item "a"]
         (calc/parse "=[a]"))))

(deftest test-formula-text
  (is (= [:text "abc"]
         (calc/parse "=\"abc\"")
         (calc/parse "='abc'")))
  (is (= [:text ""]
         (calc/parse "=\"\"")
         (calc/parse "=''"))))

(deftest test-formula-group
  (is (= [:group [:num "1"]]
         (calc/parse "=(1)")
         (calc/parse "= ( 1 ) ")))
  (is (= [:group [:text ""]]
         (calc/parse "=('')")))
  (is (= [:binary [:num "1"]
          [:op "/"]
          [:group
           [:binary
            [:group [:binary [:num "2"] [:op "+"] [:num "3"]]]
            [:op "*"]
            [:num "4"]]]]
         (calc/parse "= 1 / ((2 + 3) * 4)"))))

(deftest test-formula-invalid
  (is (= [:invalid "$`"]
         (calc/parse "=$`"))))
