org.sandbox
===========================================
.. .rst to .html: rst2html5 foo.rst > foo.html
..                pandoc -s -f rst -t html5 -o foo.html foo.rst

A Leiningen template for mixed JVM project (choices skeleton: clojuremixed, groovymixed, javamixed, scalamixed).

Installation
------------
source code tarball download:
        # [aria2c --check-certificate=false | wget --no-check-certificate | curl -kOL]
        
        FETCHCMD='aria2c --check-certificate=false'
        
        $FETCHCMD https://bitbucket.org/thebridge0491/lein-template/[get | archive]/master.zip

version control repository clone:
        
        git clone https://bitbucket.org/thebridge0491/lein-template.git

cd <path> ; lein install

Usage
-----
		// example
		cd <path> ; [LEIN_JVM_OPTS="-Dskeleton=clojuremixed -Dexecutable=yes"] lein new org.sandbox <groupid>/<parent>.<module> [--to-dir <parent>.<module> --force]

Author/Copyright
----------------
Copyright (c) 2014 by thebridge0491 <thebridge0491-codelab@yahoo.com>

License
-------
Licensed under the Apache-2.0 License. See LICENSE for details.
