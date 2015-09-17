(ns sheetpad.core
  (:require [ring.adapter.jetty :as jetty]
            [ring.util.response :as response]
            [compojure.core :refer :all]
            [compojure.route :as route]
            [sheetpad.sheets :as sheets]))

(defn index-response []
  (-> (response/resource-response "index.html" {:root "public"})
      (response/content-type "text/html")))

(defn get-sheet-names-edn [] (-> ((sheets/sheets :names)) str))

(def read-body (comp clojure.edn/read-string slurp :body))

(defroutes app
  (GET "/" _ (index-response))
  (GET "/sheets/:name" [name] (-> name ((sheets/sheets :by-name)) str))
  (GET "/sheets" _ (get-sheet-names-edn))
  (POST "/save-sheet" req
        (do ((sheets/sheets :add) (read-body req))
            (get-sheet-names-edn)))
  (POST "/delete-sheet" req
        (do ((sheets/sheets :delete) (read-body req))
            (get-sheet-names-edn)))
  (route/resources "/")
  (route/not-found "<h1>Page not found</h1>"))

(defn -main []
  (jetty/run-jetty app {:port 3000}))
