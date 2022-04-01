# Multi-package project Makefile script.
.POSIX:
help:

#MAKE = make # (GNU make variants: make (Linux) gmake (FreeBSD)

parent = introclj
version = 0.1.0
SUBDIRS = common foreignc api app

.PHONY: all clean compile help install test
help: $(SUBDIRS)
	-for dirX in $^ ; do (cd $$dirX ; lein help) ; done
	@echo "##### Top-level multiproject: $(parent) #####"
	@echo "Usage: $(MAKE) [SUBDIRS="$(SUBDIRS)"] [target]"
all compile: $(SUBDIRS)
	-for dirX in $^ ; do (cd $$dirX ; lein compile :all) ; done
test: $(SUBDIRS)
	-for dirX in $^ ; do (cd $$dirX ; lein test) ; done
install: $(SUBDIRS)
	-for dirX in $^ ; do (cd $$dirX ; lein install) ; done
clean: $(SUBDIRS)
	-for dirX in $^ ; do (cd $$dirX ; lein clean) ; done
	-rm -fr core* *~ .*~ target/* *.log */*.log

#----------------------------------------
FMTS ?= tar.gz,zip
distdir = $(parent)-$(version)

target/$(distdir) : 
	-@mkdir -p target/$(distdir) ; cp -f exclude.lst target/
#	#-zip -9 -q --exclude @exclude.lst -r - . | unzip -od target/$(distdir) -
	-tar --format=posix --dereference --exclude-from=exclude.lst -cf - . | tar -xpf - -C target/$(distdir)

.PHONY: dist doc lint cover run
distt | target/$(distdir): $(SUBDIRS)
	-@for fmt in `echo $(FMTS) | tr ',' ' '` ; do \
		case $$fmt in \
			7z) echo "### target/$(distdir).7z ###" ; \
				rm -f target/$(distdir).7z ; \
				(cd target ; 7za a -t7z -mx=9 $(distdir).7z $(distdir)) ;; \
			zip) echo "### target/$(distdir).zip ###" ; \
				rm -f target/$(distdir).zip ; \
				(cd target ; zip -9 -q -r $(distdir).zip $(distdir)) ;; \
			*) tarext=`echo $$fmt | grep -e '^tar$$' -e '^tar.xz$$' -e '^tar.zst$$' -e '^tar.bz2$$' || echo tar.gz` ; \
				echo "### target/$(distdir).$$tarext ###" ; \
				rm -f target/$(distdir).$$tarext ; \
				(cd target ; tar --posix -L -caf $(distdir).$$tarext $(distdir)) ;; \
		esac \
	done
	-@rm -r target/$(distdir)
	-for dirX in $^ ; do $(MAKE) -C $$dirX $@ ; done
doc: $(SUBDIRS)
	-for dirX in $^ ; do (cd $$dirX ; lein codox) ; done
lint: $(SUBDIRS)
	-for dirX in $^ ; do (cd $$dirX ; lein eastwood) ; done
cover: $(SUBDIRS)
	-for dirX in $^ ; do (cd $$dirX ; lein cloverage) ; done
run: app
	-(cd app ; lein run)
