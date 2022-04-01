Introclj.Intro
===========================================
.. .rst to .html: rst2html5 foo.rst > foo.html
..                pandoc -s -f rst -t html5 -o foo.html foo.rst

Main app sub-package for Clojure Intro examples project.

Installation
------------
source code tarball download:
    
        # [aria2c --check-certificate=false | wget --no-check-certificate | curl -kOL]
        
        FETCHCMD='aria2c --check-certificate=false'
        
        $FETCHCMD https://bitbucket.org/thebridge0491/introclj/[get | archive]/master.zip

version control repository clone:
        
        git clone https://bitbucket.org/thebridge0491/introclj.git

build example with maven wrapper:
cd <path> ; ./mvnw [-Djava.library.path=$PREFIX/lib] compile [clojure:test]

./mvnw install

build example with leiningen self-install:
cd <path> ; [JVM_OPTS="-Djava.library.path=$PREFIX/lib"] ./leinw compile [test]

./leinw install

Usage
-----
        [env RSRC_PATH=<path>/resources] java -jar introclj.intro-<version>.jar [-h]

Author/Copyright
----------------
Copyright (c) 2014 by thebridge0491 <thebridge0491-codelab@yahoo.com>

License
-------
Licensed under the Apache-2.0 License. See LICENSE for details.
