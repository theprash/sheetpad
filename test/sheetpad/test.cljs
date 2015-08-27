(ns sheetpad.test
  (:require [cemerick.cljs.test :refer-macros [is are deftest testing use-fixtures done]]
            [sheetpad.handlers :as handlers]
            [sheetpad.render :as render]
            [sheetpad.calculate :as calc]))

(def tests
  [{:desc "Parse 1 + 1"
    :test {:equal [[:cell [:formula [:num "1"] [:symbol "+"] [:num "1"]]]
                   (calc/parser "=1+1")]}}
   {:desc "Parse 1 * 1"
    :test {:equal [[:cell [:formula [:num "1"] [:symbol "*"] [:num "1"]]]
                   (calc/parser "=1 * 1")]}}])

(deftest test-home
  (is (= 1 1)))
