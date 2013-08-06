(ns clojureworld.core)

;; LOCATION

(defrecord LocationState [things players])

(defprotocol LocationActions
  (enter [this player] "A player enters the location")
  (leave [this player] "A player leaves the ocation")
  (add-thing [this thing] "Something is dropped here")
  (get-thing [this thing] "Something is taken away from here. Returns the thing."))

(defrecord Location [name description state-ref]
  LocationActions
  (enter [this player]
    (dosync
      (alter state-ref assoc :players (conj (:players (deref state-ref)) player))))
  (leave [this player]
    (dosync
      (alter state-ref assoc :players (disj (:players (deref state-ref)) player))))
  (add-thing [this thing]
    (dosync
      (alter state-ref assoc :things (conj (:things (deref state-ref)) thing))))
  (get-thing [this thing]
    (dosync
      (def thing-from-location (get (:things (deref state-ref)) thing))
      (if-not (nil? thing-from-location)
        (alter state-ref assoc :things
          (disj (:things (deref state-ref)) thing-from-location)))
      thing-from-location)))

;; PLAYER

(defrecord PlayerState [location inventory])

(defprotocol PlayerActions
  (say
    [this words]
    [this words recipient]
    "Says something - just so or to another player")
  (move
    [this location]
    "Moves the player to another location")
  (grab
    [this thing]
    "Take something from the current location")
  )

(defrecord Player [name state-ref]
  PlayerActions
  (say [this words] (str "Du sagst: " words))
  (say [this words player] (str "Du sagst zu " (:name player) ": " words))
  (move [this new-location]
    (dosync
      (def old-location (:location (deref state-ref)))
      (if-not (nil? old-location) (.leave old-location this))
      (.enter new-location this)
      (alter state-ref assoc :location new-location)))
  (grab [this thing]
    (dosync
      (def location (:location (deref state-ref)))
      (def thing-from-location (.get-thing location thing))
      (alter state-ref assoc :inventory
        (conj (:inventory (deref state-ref)) thing-from-location)))))

