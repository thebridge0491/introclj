#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
${parent.substring(0,1).toUpperCase()}${parent.substring(1)}.${name.substring(0,1).toUpperCase()}${name.substring(1)}
===========================================
.. .rst to .html: rst2html5 foo.rst > foo.html
..                pandoc -s -f rst -t html5 -o foo.html foo.rst

${description}

Installation
------------
source code tarball download:
    
        ${symbol_pound} [aria2c --check-certificate=false | wget --no-check-certificate | curl -kOL]
        
        FETCHCMD='aria2c --check-certificate=false'
        
        ${symbol_dollar}FETCHCMD https://${repohost}/${repoacct}/${parent}/[get | archive]/master.zip

version control repository clone:
        
        git clone https://${repohost}/${repoacct}/${parent}.git

#{if}("maven" == ${buildTool})
cd <path> ; mvn [-Djava.library.path=$PREFIX/lib] compile [test]

mvn install
#{else}
cd <path> ; [JVM_OPTS="-Djava.library.path=${symbol_dollar}PREFIX/lib"] lein compile [test]

lein install
#{end}

Usage
-----
#{if}("yes" == ${executable})
        [env RSRC_PATH=<path>/resources] java -jar ${parent}.${name}-<version>.jar [-h]
#{else}
        // PKG_CONFIG='pkg-config --with-path=${symbol_dollar}PREFIX/lib/pkgconfig'
        
        // ${symbol_dollar}PKG_CONFIG --cflags --libs <ffi-lib>
        
        // java [-Djava.library.path=${symbol_dollar}PREFIX/lib] ...
        
        (require '[${package}.core :as core])
        
        ...
        
        (let [xs (list 0 1 2) ys (list 10 20 30)] 
			(core/cartesian-prod xs ys))
#{end}

Author/Copyright
----------------
Copyright (c) ${date.split("-")[0]} by ${author} <${email}>


License
-------
Licensed under the ${license} License. See LICENSE for details.

