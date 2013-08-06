(ns clojureworld.core-test
  (:require [clojure.test :refer :all ]
            [clojureworld.core :refer :all ]))

(deftest demo

  (testing "Eher eine Demo als ein Test"

    ;; SETUP TEST FIXTURE

    (def fleetschloss (->Location
                        "Fleetschlösschen"
                        "Eine kleine nette Kneipe am Rande der Speicherstadt"
                        (ref (->LocationState #{:bier :bierdeckel :feuerzeug } #{}))))

    (def brook2 (->Location
                  "Holländischer Brook 2"
                  "Ein Büro in der Speicherstadt"
                  (ref (->LocationState #{:kram } #{}))))

    (def karlheinz (->Player
                     "Karl-Heinz"
                     (ref (->PlayerState nil #{:olles_taschentuch }))))

    ;; TEST INITIAL MOVE

    (.move karlheinz brook2)

    (is (=
          (:name (:location @(:state-ref karlheinz)))
          "Holländischer Brook 2"))

    (is (=
          (:players @(:state-ref brook2))
            #{karlheinz}))

    (is (=
          (:players @(:state-ref fleetschloss))
            #{}))

    ;; TEST SUBSEQUENT MOVE

    (.move karlheinz fleetschloss)

    (is (=
          (:name (:location @(:state-ref karlheinz)))
          "Fleetschlösschen"))

    (is (=
          (:players @(:state-ref brook2))
            #{}))

    (is (=
          (:players @(:state-ref fleetschloss))
            #{karlheinz}))


    ;; TEST GRAB

    (.grab karlheinz :bier )

    (is (=
          (:inventory @(:state-ref karlheinz))
            #{:bier :olles_taschentuch }))

    (is (=
          (:things @(:state-ref fleetschloss))
            #{:bierdeckel :feuerzeug }))
    ))
