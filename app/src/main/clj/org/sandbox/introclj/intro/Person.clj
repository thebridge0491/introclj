(ns ^{:doc "Person class"} org.sandbox.introclj.intro.Person
    (:require
        [clojure.tools.logging :as log]
        )
    (:import 
        [org.slf4j LoggerFactory]
        )
    (:gen-class ;:implements Ifc
            :extends Object
            ;:name org.sandbox.introclj.intro.Person
            :state state
            :init init
            :post-init post-init
            :prefix "-"
            :main false
            :constructors {[String Long][] [][]}
            :methods [[getName [] String], [setName [String] void]
                [getAge [] Long], [setAge [Long] void]
                ]
        )
    )

(def ^{:private true} pracLogger (LoggerFactory/getLogger "prac"))

(defn- setfield
    [this key value]
    (swap! (.state this) into {key value}))

(defn- getfield
    [this key]
    (@(.state this) key))

(defn -init
    ([name age]
        [[] (atom {:name name, :age age})])
    ([]
        [[] (atom {:name "Name", :age 18})])
    )

(defn -post-init
    [this name age]
    ;(log/info "Person()")
    (.info pracLogger "Person()")
    )

(defn -setName
    [this name]
    (setfield this :name name))

(defn -setAge
    [this age]
    (setfield this :age age))

(defn -getName
    [this]
    (getfield this :name))

(defn -getAge
    [this]
    (getfield this :age))

(defn -hashCode
    [this]
    ;(.hashCode @(.state this)))
    (hash @(.state this)))

(defn -equals
    [this obj]
    (cond 
        (identical? obj this) true
        (or (nil? obj) (not= (.getClass this) (.getClass obj))) false
        :else (and 
            (= (getfield obj :name) (getfield this :name)) 
            (= (getfield obj :age) (getfield this :age)))))

(defn -toString
    [this]
    ;(pr-str @(.state this)))
    (.toString @(.state this)))
    ;(format "{:name %s, :age %d}" (getfield this :name) (getfield this :age))
