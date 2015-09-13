(ns sheetpad.core
  (:require [ring.adapter.jetty :as jetty]
            [ring.middleware.resource :as resource]
            [ring.middleware.content-type :as content-type]
            [ring.util.response :as response]))

(defn handler [request]
  (when (request :uri)
    (-> (response/resource-response "index.html" {:root "public"})
        (response/content-type "text/html"))))

(def app
  (-> handler
      (resource/wrap-resource "public")
      (content-type/wrap-content-type)))

(defn -main []
  (jetty/run-jetty app {:port 3000}))
