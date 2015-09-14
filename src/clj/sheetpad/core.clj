(ns sheetpad.core
  (:require [ring.adapter.jetty :as jetty]
            [ring.util.response :as response]
            [compojure.core :refer :all]
            [compojure.route :as route]
            [sheetpad.sheets :as sheets]))

(defn index-response []
  (-> (response/resource-response "index.html" {:root "public"})
      (response/content-type "text/html")))

(defroutes app
  (GET "/" _ (index-response))
  (GET "/sheets/:name" [name] (->> name ((sheets/sheets :by-name)) str))
  (GET "/sheets" _ (->> ((sheets/sheets :names)) str))
  (route/resources "/")
  (route/not-found "<h1>Page not found</h1>"))

(defn -main []
  (jetty/run-jetty app {:port 3000}))
