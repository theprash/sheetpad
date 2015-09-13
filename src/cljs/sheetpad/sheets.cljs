(ns sheetpad.sheets)

(def default-sheets
  [{:name "Basic formula"
    :items [{:name "a" :raw-value "1"}
            {:name "b" :raw-value "=[a] * 2"}]}
   {:name "Lots of formulas"
    :items [{:name "a" :raw-value "1"}
            {:name "b" :raw-value "=[a] + 1"}
            {:name "c" :raw-value "=[b] + 1"}
            {:name "d" :raw-value "=[c] + 1"}
            {:name "e" :raw-value "=[d] + 1"}
            {:name "f" :raw-value "=[e] + 1"}
            {:name "g" :raw-value "=[f] + 1"}
            {:name "h" :raw-value "=[g] + 1"}
            {:name "i" :raw-value "=[h] + 1"}
            {:name "j" :raw-value "=[i] + 1"}
            {:name "k" :raw-value "=[j] + 1"}
            {:name "l" :raw-value "=[k] + 1"}
            {:name "m" :raw-value "=[l] + 1"}
            {:name "n" :raw-value "=[m] + 1"}]}])
