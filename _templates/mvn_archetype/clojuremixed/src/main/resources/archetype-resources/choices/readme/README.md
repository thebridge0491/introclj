#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
${symbol_pound} ${parent.substring(0,1).toUpperCase()}${parent.substring(1)}-${name.substring(0,1).toUpperCase()}${name.substring(1)}
<!-- .md to .html: markdown foo.md > foo.html
                   pandoc -s -f markdown_strict -t html5 -o foo.html foo.md -->

${description}

${symbol_pound}${symbol_pound} Installation
source code tarball download:
    
        ${symbol_pound} [aria2c --check-certificate=false | wget --no-check-certificate | curl -kOL]
        FETCHCMD='aria2c --check-certificate=false'
        ${symbol_dollar}FETCHCMD https://${repohost}/${repoacct}/${parent}/[get | archive]/master.zip

version control repository clone:
        
        git clone https://${repohost}/${repoacct}/${parent}.git


build example with maven wrapper:
cd <path> ; ./mvnw [-Djava.library.path=$PREFIX/lib] compile [clojure:test]
./mvnw install

build example with leiningen self-install:
cd <path> ; [JVM_OPTS="-Djava.library.path=$PREFIX/lib"] ./leinw do jar[, test]
./leinw install

${symbol_pound}${symbol_pound} Usage
#{if}("yes" == ${executable})
        [env RSRC_PATH=<path>/resources] java -jar ${artifactId}-<version>.jar [-h]
#{else}
        // PKG_CONFIG='pkg-config --with-path=${symbol_dollar}PREFIX/lib/pkgconfig'
        // ${symbol_dollar}PKG_CONFIG --cflags --libs <ffi-lib>
        // java [-Djava.library.path=${symbol_dollar}PREFIX/lib] ...
        
        (require '[org.sandbox.intro_jvm.util.core :as core])
        ...
        (let [xs (list 0 1 2) ys (list 10 20 30)]
			(core/cartesian-prod xs ys))
#{end}

${symbol_pound}${symbol_pound} Author/Copyright
Copyright (c) ${date.split("-")[0]} by ${author} <${email}>


${symbol_pound}${symbol_pound} License
Licensed under the ${license} License. See LICENSE for details.

