(in-ns 'org.sandbox.introclj.practice.core-test)
(require '[org.sandbox.introclj.practice.classic :as classic])
(import '[org.sandbox.introclj.practice Classic_java])

(defn genTup2Float
    [gen0 gen1]
    (gen/tuple gen0 gen1))

(def prop-fact
    (prop/for-all [n (gen/choose 0 18)]
        (let [ans (reduce * 1 (range 1 (+ n 1)))]
            (reduce (fn [a f] (and a (= ans (f n)))) true
                [classic/fact-r classic/fact-i #(Classic_java/fact_lp %)
                #(Classic_java/fact_i %)]))))

(def prop-expt
    (prop/for-all [b-n (genTup2Float (gen/choose 1.0 20.0)
            (gen/choose 1.0 10.0))]
        (let [b (get b-n 0) n (get b-n 1) ans (Math/pow b n)]
            (reduce (fn [a f] (and a (util/in-epsilon (* 0.001 ans) ans (f b n))))
                true [classic/expt-r classic/expt-i #(Classic_java/expt_lp %1 %2)
                    #(Classic_java/expt_i %1 %2)]))))


;(qc/quick-check 100 prop-fact)
;(testqc/defspec fact 100 prop-fact)
(testqc/defspec fact-prop prop-fact)

(testqc/defspec expt-prop prop-expt)
