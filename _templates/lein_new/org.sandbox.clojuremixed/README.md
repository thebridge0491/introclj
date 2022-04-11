# org.sandbox.clojuremixed
<!-- .md to .html: markdown foo.md > foo.html
                   pandoc -s -f markdown_strict -t html5 -o foo.html foo.md -->

A Leiningen template for mixed Clojure project (choices skeleton: skeleton_clj).

## Installation
source code tarball download:
        # [aria2c --check-certificate=false | wget --no-check-certificate | curl -kOL]
        FETCHCMD='aria2c --check-certificate=false'
        $FETCHCMD https://bitbucket.org/thebridge0491/clojuremixed/[get | archive]/master.zip

version control repository clone:
        git clone https://bitbucket.org/thebridge0491/clojuremixed.git

cd <path> ; lein install

## Usage
		// example
		cd <path> ; [LEIN_JVM_OPTS="-DdataFile=data.yaml -DdataFmt=yaml -Dskeleton=skeleton_clj -Dexecutable=yes"] lein new org.sandbox.clojuremixed <groupid>/<parent>.<module> [--to-dir <parent>.<module> --force]

## Author/Copyright
Copyright (c) 2014 by thebridge0491 <thebridge0491-codelab@yahoo.com>

## License
Licensed under the Apache-2.0 License. See LICENSE for details.
