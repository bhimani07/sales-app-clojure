(ns test.core)
(require '[clojure.string :as str])

(defn Read []
  (def string1 (slurp "cust.txt"))
  (def string2 (str/split-lines string1))
  (def string3 (map #(str/split % #"[|]") string2))
  (def cust (sort-by first (apply merge (map #(hash-map (keyword (first %)) (rest %)) string3))))

  ;(println (apply merge (map #(hash-map (keyword (first %)) (rest %)) string3)))
  ;(run! println cust)

  (doall (map (fn [[k, v]] (println  k" --> [" v "]")) cust))
  ;(doall (map #(println %":["(get cust %)"]") ((sort-by keys cust))))
  ;(def cust1 (apply merge cust))


  ;(def abc (map #(hash-map % 0) (seq "abcdefgh")))
  ;(println (keys abc))
  ;(print (keys {:keys :and, :some :values}))

  (def string11 (slurp "prod.txt"))
  (def string12 (str/split-lines string11))
  (def string13 (map #(str/split % #"[|]") string12))
  (def prod (sort-by first (apply merge (map #(hash-map (first %) (rest %)) string13))))

  (def string21 (slurp "sales.txt"))
  (def string22 (str/split-lines string21))
  (def string23 (map #(str/split % #"[|]") string22))
  (def sale (sort-by first (apply merge (map #(hash-map (first %) (rest %)) string23))))
  )
(Read)
(def Menu [])
(defn PrintCust []
  ;(doseq cust)
  (doall (map println cust))
  ;(doall (map #(println %":["(get cust %)"]") (keys cust)))
  ;(doall (map #(println (first %)":[\""(first (rest %))"\" \""(first(rest (rest %)))"\" \""(first (rest (rest (rest %))))"\"]") string3))
  (Menu))

(defn PrintProd []
  (doall (map println prod))
  (Menu))

(defn PrintSales []
  (doall (map println sale))
  (Menu))
(defn TotalSales []
  (doall (map println sale))
  (Menu))
(defn TotalCount []
  (doall (map println sale))
  (Menu))

(defn Exit []
  (System/exit 0))

(defn Menu []
  (println "***Sales Menu***")
  (println "----------------------")
  (println "1. Display Customer Table")
  (println "2. Display Product Table")
  (println "3. Display Sales Table")
  (println "4. Total Sales For Customer")
  (println "5. Total Count For Product")
  (println "6. Exit")
  (println "Enter an Option :")
  (def option (read-line))

  (case option
    "1" (PrintCust)
    "2" (PrintProd)
    "3" (PrintSales)
    "4" (TotalSales)
    "5" (TotalCount)
    "6" (Exit)
    (Menu)))

(Menu)

