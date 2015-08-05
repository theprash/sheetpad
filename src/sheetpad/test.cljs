(ns sheetpad.test
  (:require [sheetpad.handlers :as handlers]
            [sheetpad.render :as render]
            [sheetpad.calculate :as calc]))

(def tests
  [{:desc "Parse 1 + 1"
    :test {:equal [[:cell [:formula [:num "1"] [:symbol "+"] [:num "1"]]]
                   (calc/parser "=1+1")]}}
   {:desc "Parse 1 * 1"
    :test {:equal [[:cell [:formula [:num "1"] [:symbol "*"] [:num "1"]]]
                   (calc/parser "=1 * 1")]}}])

(defn passed? [test]
  (apply = (-> test :test :equal)))

(defn run-tests []
  (let [failed (filter (complement passed?) tests)]
    (str (count tests) " tests run\n"
         (count failed) " failed\n"
         (clojure.string/join "\n" (->> failed
                                        (map :desc)
                                        (map #(str "  " %)))))))
