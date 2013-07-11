(ns clojureworld.core
  (:require [liberator.core :refer [resource defresource]]
            [ring.adapter.jetty :refer [run-jetty]]
            [compojure.core :refer [defroutes ANY routes]]
            [liberator.dev :as dev]
            [ring.middleware.multipart-params :only [wrap-multipart-params]]
            [ring.util.response :only [header]]
            [compojure.handler :only [api]]))

(defresource hello-world
  :handle-ok "Hello World!"
  :etag "fixed-etag"
  :available-media-types ["text/plain"])

(defresource root
  :handle-ok "Start here for the begisnning!"
  :etag "fixed-etag"
  :available-media-types ["text/plain"])

(defn assemble-routes []
  (->
    (routes
      (ANY "/" [] root)
      (ANY "/hello-world" [] hello-world)
    )
  )
)

(def handler
  (-> (assemble-routes)))

(run-jetty #'handler {:port 3000})
