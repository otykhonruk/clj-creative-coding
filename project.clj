(defproject creative-coding "0.1.0-SNAPSHOT"
  :description "Sketches in clojure/quil"
  :url "https://github.com/otykhonruk/clj-creative-coding"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.12.1"]
                 [quil "4.3.1563"]
                 [generateme/fastmath "2.4.0"
                  :exclusions [com.github.haifengl/smile-mkl
                               org.bytedeco/openblas]]])
