(in-ns 'org.sandbox.introclj.foreignc.core-test)
(require '[org.sandbox.introclj.foreignc.classic :as classic])

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
    (doseq [row (util/cartesian-prod [2.0 11.0 20.0] [3.0 6.0 10.0])]
        (let [ans (Math/pow (nth row 0) (nth row 1))]
            (doseq [f [classic/expt-i classic/expt-lp]]
                ;(test/is (= ans (f (nth row 0) (nth row 1))))
                (test/is (util/in-epsilon (* 0.001 ans) ans (f (nth row 0) (nth row 1))))
                ))
        ))
