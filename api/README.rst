Introclj.Practice
===========================================
.. .rst to .html: rst2html5 foo.rst > foo.html
..                pandoc -s -f rst -t html5 -o foo.html foo.rst

Practice sub-package for Clojure Intro examples project.

Installation
------------
source code tarball download:
    
        # [aria2c --check-certificate=false | wget --no-check-certificate | curl -kOL]
        
        FETCHCMD='aria2c --check-certificate=false'
        
        $FETCHCMD https://bitbucket.org/thebridge0491/introclj/[get | archive]/master.zip

version control repository clone:
        
        git clone https://bitbucket.org/thebridge0491/introclj.git

build example with maven:
cd <path> ; mvn [-Djava.library.path=$PREFIX/lib] compile [test]

mvn install

build example with leiningen:
cd <path> ; [JVM_OPTS="-Djava.library.path=$PREFIX/lib"] lein compile [test]

lein install

Usage
-----
        // PKG_CONFIG='pkg-config --with-path=$PREFIX/lib/pkgconfig'
        
        // $PKG_CONFIG --cflags --libs <ffi-lib>
        
        // java [-Djava.library.path=$PREFIX/lib] ...
        
        (require '[org.sandbox.introclj.practice.classic :as classic])
        
        ...
        
        (let [n 5] 
			(classic/fact-i n))

Author/Copyright
----------------
Copyright (c) 2014 by thebridge0491 <thebridge0491-codelab@yahoo.com>

License
-------
Licensed under the Apache-2.0 License. See LICENSE for details.
