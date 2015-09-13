(ns sheetpad.util)

(defn vec-remove
  [coll pos]
  (vec (concat (subvec coll 0 pos) (subvec coll (inc pos)))))

(defn key-event-handler [handle-fn keycode]
  (fn [e]
    (when (= (.-keyCode e)
             keycode)
      (.preventDefault e)
      (handle-fn e))))
