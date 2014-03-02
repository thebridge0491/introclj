(in-ns 'org.sandbox.introclj.util.core-test)
(require '[org.sandbox.introclj.util.classic :as classic])

(defn in-epsilon
    [tolerance a b]
    (let [delta (Math/abs tolerance)]
        ;(and (<= (- a delta) b) (>= (+ a delta) b))))
        (and (not (< (+ a delta) b)) (not (< (+ b delta) a)))))

(defn genTup2Float
    [gen0 gen1]
    (gen/tuple gen0 gen1))

(def prop-fact
    (prop/for-all [n (gen/choose 0 18)]
        (let [ans (reduce * 1 (range 1 (+ n 1)))]
            (reduce (fn [a f] (and a (= ans (f n)))) true
                [classic/fact-i classic/fact-lp]))))

(def prop-expt
    (prop/for-all [b-n (genTup2Float (gen/choose 1.0 20.0)
            (gen/choose 1.0 10.0))]
        (let [b (get b-n 0) n (get b-n 1) ans (Math/pow b n)]
            (reduce (fn [a f] (and a (in-epsilon (* 0.001 ans) ans (f b n))))
                true [classic/expt-i classic/expt-lp]))))


;(qc/quick-check 100 prop-fact)
;(testqc/defspec fact 100 prop-fact)
(testqc/defspec fact prop-fact)

(testqc/defspec expt prop-expt)
