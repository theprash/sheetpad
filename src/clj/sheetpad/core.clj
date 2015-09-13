(ns sheetpad.core
  (:require [ring.adapter.jetty :as jetty]
            [ring.util.response :as response]
            [compojure.core :refer :all]
            [compojure.route :as route]))

(defn index-response []
  (-> (response/resource-response "index.html" {:root "public"})
      (response/content-type "text/html")))

(defroutes app
  (route/resources "/")
  (GET "/" _ (index-response))
  (route/not-found "<h1>Page not found</h1>"))

(defn -main []
  (jetty/run-jetty app {:port 3000}))
