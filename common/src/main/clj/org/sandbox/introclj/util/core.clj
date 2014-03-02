(ns ^{:doc "Core library"} org.sandbox.introclj.util.core
    ;(:require
    ;   [clojure.tools.logging :as log]
    ;   )
    ;(:import 
    ;   [org.slf4j LoggerFactory]
    ;   )
    )

;(def ^{:private true} pracLogger (LoggerFactory/getLogger "prac"))

(defn echo-invoke
    [func & args]
    (let [res (apply func args)]
        (format "%s: %s%n" (cons (class func) args) 
                (if (= clojure.lang.LazySeq (class res)) (pr-str res) res))))

(defn display-ncoll
    [sep stop ncoll]
    (doseq [el ncoll] 
        (if (coll? el) 
            (display-ncoll sep stop el)
            (printf "%s%s" el sep)))
    (printf stop))

(defn mkstring-init
    [beg sep stop colls]
    (str
        (reduce 
            (fn [acc el]
                (if (coll? el) 
                    (format "%s%s" acc (mkstring-init "" sep "\n" el))
                    (format "%s%s%s" acc el sep)))
            beg
            colls)
        stop))

(defn mkstring
    [colls]
    (mkstring-init "(list " " " ")" colls))

(defn in-epsilon
    [tolerance a b]
    (let [delta (Math/abs tolerance)]
        ;(and (<= (- a delta) b) (>= (+ a delta) b))))
        (and (not (< (+ a delta) b)) (not (< (+ b delta) a)))))

(defn cartesian-prod
    [xs ys]
    ;(for [x xs y ys :when true] (list x y)))
    (mapcat (fn [x] (filter (fn [e] true) (map (fn [y] (list x y)) ys))) xs))


(defn -main
    [& argv]
    (let [xs (list 0 1 2) ys (list 10 20 30)]
        (printf "(cartesian-prod %s %s): %s\n" xs ys
            (pr-str (cartesian-prod xs ys)))
        (flush)))

(when (and (.isFile (clojure.java.io/file *file*)) (re-find  #"core.clj" *file*))
    (apply -main *command-line-args*))
