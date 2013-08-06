(ns clojureworld.rest
  (:require [liberator.core :refer [resource defresource]]
            [ring.adapter.jetty :refer [run-jetty]]
            [compojure.core :refer [defroutes ANY routes]]
            [liberator.dev :as dev]
            [ring.middleware.multipart-params :only [wrap-multipart-params]]
            [ring.util.response :only [header]]
            [compojure.handler :only [api]]
            [ring.middleware.json :refer [wrap-json-response]]))

(defresource hello-world
  :handle-ok {:msg "Hello" :recipient "World!!!!!!?"}
  :etag "fixed-etag"
  :available-media-types ["application/json"])

(defresource start
  :handle-ok {:msg "Start here!"}
  :etag "fixed-etag"
  :available-media-types ["application/json"])

(defn assemble-routes []
  (->
    (routes
      (ANY "/" [] start)
      (ANY "/hello-world" [] hello-world))))

(defn wrap-expires [handler expiry]
  (fn [request]
    (let [response (handler request)]
      (assoc-in response [:headers "Expires"] expiry))))

(def app
  (-> (assemble-routes)
    (wrap-json-response)
    (wrap-expires "2014-01-01")))

(defn start []
  (run-jetty #'app {:port 5000 :join? false}))

