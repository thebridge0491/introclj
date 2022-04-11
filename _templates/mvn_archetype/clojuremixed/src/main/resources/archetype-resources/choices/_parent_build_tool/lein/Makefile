#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
${symbol_pound} Multi-package project Makefile script.
.POSIX:
help:

${symbol_pound}MAKE = make ${symbol_pound} (GNU make variants: make (Linux) gmake (FreeBSD)

parent = ${parent}
version = ${version}
SUBDIRS = common app

.PHONY: all clean compile help install test
help: ${symbol_dollar}(SUBDIRS)
	-for dirX in ${symbol_dollar}^ ; do ${symbol_dollar}(MAKE) -C ${symbol_dollar}${symbol_dollar}dirX ${symbol_dollar}@ ; done
	@echo "${symbol_pound}${symbol_pound}${symbol_pound}${symbol_pound}${symbol_pound} Top-level multiproject: ${symbol_dollar}(parent) ${symbol_pound}${symbol_pound}${symbol_pound}${symbol_pound}${symbol_pound}"
	@echo "       ${symbol_dollar}(MAKE) [SUBDIRS="${symbol_dollar}(SUBDIRS)"] configure [OPTS=??]"
	@echo "Usage: ${symbol_dollar}(MAKE) [SUBDIRS="${symbol_dollar}(SUBDIRS)"] [target]"
all compile: ${symbol_dollar}(SUBDIRS)
	-for dirX in ${symbol_dollar}^ ; do (cd ${symbol_dollar}${symbol_dollar}dirX ; ./leinw compile :all) ; done
test: ${symbol_dollar}(SUBDIRS)
	-for dirX in ${symbol_dollar}^ ; do (cd ${symbol_dollar}${symbol_dollar}dirX ; ./leinw test) ; done
install: ${symbol_dollar}(SUBDIRS)
	-for dirX in ${symbol_dollar}^ ; do (cd ${symbol_dollar}${symbol_dollar}dirX ; ./leinw install) ; done
clean: ${symbol_dollar}(SUBDIRS)
	-for dirX in ${symbol_dollar}^ ; do (cd ${symbol_dollar}${symbol_dollar}dirX ; ./leinw clean) ; done
	-rm -fr core* *~ .*~ target/* *.log */*.log

${symbol_pound}----------------------------------------
FMTS ?= tar.gz,zip
distdir = ${symbol_dollar}(parent)-${symbol_dollar}(version)

target/${symbol_dollar}(distdir) :
	-@mkdir -p target/${symbol_dollar}(distdir) ; cp -f exclude.lst target/
#	#-zip -9 -q --exclude @exclude.lst -r - . | unzip -od target/${symbol_dollar}(distdir) -
	-tar --format=posix --dereference --exclude-from=exclude.lst -cf - . | tar -xpf - -C target/${symbol_dollar}(distdir)

.PHONY: dist doc lint cover run
dist | target/${symbol_dollar}(distdir): ${symbol_dollar}(SUBDIRS)
	-@for fmt in `echo ${symbol_dollar}(FMTS) | tr ',' ' '` ; do \
		case ${symbol_dollar}${symbol_dollar}fmt in \
			7z) echo "${symbol_pound}${symbol_pound}${symbol_pound} target/${symbol_dollar}(distdir).7z ${symbol_pound}${symbol_pound}${symbol_pound}" ; \
				rm -f target/${symbol_dollar}(distdir).7z ; \
				(cd target ; 7za a -t7z -mx=9 ${symbol_dollar}(distdir).7z ${symbol_dollar}(distdir)) ;; \
			zip) echo "${symbol_pound}${symbol_pound}${symbol_pound} target/${symbol_dollar}(distdir).zip ${symbol_pound}${symbol_pound}${symbol_pound}" ; \
				rm -f target/${symbol_dollar}(distdir).zip ; \
				(cd target ; zip -9 -q -r ${symbol_dollar}(distdir).zip ${symbol_dollar}(distdir)) ;; \
			*) tarext=`echo ${symbol_dollar}${symbol_dollar}fmt | grep -e '^tar${symbol_dollar}${symbol_dollar}' -e '^tar.xz${symbol_dollar}${symbol_dollar}' -e '^tar.zst${symbol_dollar}${symbol_dollar}' -e '^tar.bz2${symbol_dollar}${symbol_dollar}' || echo tar.gz` ; \
				echo "${symbol_pound}${symbol_pound}${symbol_pound} target/${symbol_dollar}(distdir).${symbol_dollar}${symbol_dollar}tarext ${symbol_pound}${symbol_pound}${symbol_pound}" ; \
				rm -f target/${symbol_dollar}(distdir).${symbol_dollar}${symbol_dollar}tarext ; \
				(cd target ; tar --posix -L -caf ${symbol_dollar}(distdir).${symbol_dollar}${symbol_dollar}tarext ${symbol_dollar}(distdir)) ;; \
		esac \
	done
	-@rm -r target/${symbol_dollar}(distdir)
	-for dirX in ${symbol_dollar}^ ; do ${symbol_dollar}(MAKE) -C ${symbol_dollar}${symbol_dollar}dirX ${symbol_dollar}@ ; done
doc: ${symbol_dollar}(SUBDIRS)
	-for dirX in ${symbol_dollar}^ ; do (cd ${symbol_dollar}${symbol_dollar}dirX ; ./leinw codox) ; done
lint: ${symbol_dollar}(SUBDIRS)
	-for dirX in ${symbol_dollar}^ ; do (cd ${symbol_dollar}${symbol_dollar}dirX ; ./leinw eastwood) ; done
cover: ${symbol_dollar}(SUBDIRS)
	-for dirX in ${symbol_dollar}^ ; do (cd ${symbol_dollar}${symbol_dollar}dirX ; ./leinw cloverage) ; done
run: app
	-(cd app ; ./leinw run)
