(in-ns 'org.sandbox.introclj.util.core-test)
(require '[org.sandbox.introclj.util.classic :as classic])

(defn in-epsilon
    [tolerance a b]
    (let [delta (Math/abs tolerance)]
        ;(and (<= (- a delta) b) (>= (+ a delta) b))))
        (and (not (< (+ a delta) b)) (not (< (+ b delta) a)))))

(defn cartesian-prod
    [xs ys]
    ;(for [x xs y ys :when true] (list x y)))
    (mapcat (fn [x] (filter (fn [e] true) (map (fn [y] (list x y)) ys))) xs))

(defn class-fixture1
    "Class setUp/tearDown"
    [func]
    (println "setUpClass ...")
    (func)
    (println "... tearDownClass"))

(defn fixture1
    "Per test setUp/tearDown"
    [func]
    (println "setUp ...")
    (func)
    (println "... tearDown"))

(test/use-fixtures :once class-fixture1)
(test/use-fixtures :each fixture1)

(test/deftest test-fact
    "test-fact"
    (doseq [f [classic/fact-i classic/fact-lp]]
        (test/is (= 120 (f 5)))))

(test/deftest test-expt
    "test-expt"
    (doseq [row (cartesian-prod [2.0 11.0 20.0] [3.0 6.0 10.0])]
        (let [ans (Math/pow (nth row 0) (nth row 1))]
            (doseq [f [classic/expt-i classic/expt-lp]]
                ;(test/is (= ans (f (nth row 0) (nth row 1))))
                (test/is (in-epsilon (* 0.001 ans) ans (f (nth row 0) (nth row 1))))
                ))
        ))
