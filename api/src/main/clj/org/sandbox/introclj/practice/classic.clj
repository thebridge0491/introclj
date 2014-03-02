(ns ^{:doc "Classic library"} org.sandbox.introclj.practice.classic
    (:require
       [clojure.tools.logging :as log]
       )
    (:import 
       [org.slf4j LoggerFactory]
       )
    )

(def ^{:private true} pracLogger (LoggerFactory/getLogger "prac"))

(defn fact-r
    [n]
    (if (< n 2)
        1
        (* n (fact-r (- n 1)))))

(defn fact-i
    [n]
    (.debug pracLogger "fact-i")
    (letfn [
        (iter
            [prod ct]
            (if (> ct n)
                prod
                (iter (* ct prod) (+ ct 1))))]
        (iter 1 1)))

(defn expt-r
    [b n]
    (if (>= 0.0 n)
        1
        (* b (expt-r b (- n 1)))))

(defn expt-i
    [b n]
    (letfn [
        (iter
            [prod ct]
            (cond (>= 0.0 n) 1
                (>= ct n) prod
                :else (iter (* b prod) (+ ct 1))))]
        (iter b 1)))


(defn -main
    [& argv]
    (let [n 5]
        (printf "(fact %d): %d\n" n (fact-r n))
        (flush)))

(when (and (.isFile (clojure.java.io/file *file*)) (re-find  #"classic.clj" *file*))
    (apply -main *command-line-args*))
