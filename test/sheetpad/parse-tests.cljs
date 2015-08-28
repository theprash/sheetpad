(ns sheetpad.parse-tests
  (:require [cemerick.cljs.test :refer-macros [is are deftest testing use-fixtures done]]
            [sheetpad.calculate :as calc]))

(deftest test-parse-empty
  (is (= []
         (calc/parse ""))))

(deftest test-parse-number
  (is (= [[:num "123"]]
         (calc/parse "123")
         (calc/parse " 123 "))))

(deftest test-parse-text
  (is (= [[:text "abc"]]
         (calc/parse "abc"))))

(deftest test-parse-formula-add
  (is (= [[:num "1"] [:binary-op "+"] [:num "1"]]
         (calc/parse "=1+1")
         (calc/parse "= 1 + 1 "))))

(deftest test-parse-formula-multiply
  (is (= [[:num "1"] [:binary-op "*"] [:num "1"]]
         (calc/parse "=1*1")))
  (is (= [[:num "3"] [:binary-op "*"] [:num "4"]]
         (calc/parse "=3 * 4 "))))

(deftest test-parse-formula-item
  (is (= [[:item "a"]]
         (calc/parse "=[a]"))))

(deftest test-parse-formula-text
  (is (= [[:text "abc"]]
         (calc/parse "=\"abc\"")
         (calc/parse "='abc'")))
  (is (= [[:text ""]]
         (calc/parse "=\"\"")
         (calc/parse "=''"))))

(deftest test-parse-formula-group
  (is (= [[:group [:num "1"]]]
         (calc/parse "=(1)")
         (calc/parse "= ( 1 ) ")))
  (is (= [[:group [:text ""]]]
         (calc/parse "=('')")))
  (is (= [[:num "1"]
          [:binary-op "/"]
          [:group
           [:group [:num "2"] [:binary-op "+"] [:num "3"]]
           [:binary-op "*"]
           [:num "4"]]]
         (calc/parse "= 1 / ((2 + 3) * 4)"))))

(deftest test-parse-formula-invalid
  (is (= [[:invalid "$`"]]
         (calc/parse "=$`"))))
