(defproject clojureworld "0.1.0-SNAPSHOT"
  :description "A comlete world, created in clojure, accessible through a REST API!"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [
			[org.clojure/clojure "1.5.1"]
			[liberator "0.9.0"]
			[compojure "1.1.3"]
			[ring/ring-jetty-adapter "1.1.0"]
      [ring/ring-json "0.2.0"]
      [org.clojure/core.incubator "0.1.3"]])
