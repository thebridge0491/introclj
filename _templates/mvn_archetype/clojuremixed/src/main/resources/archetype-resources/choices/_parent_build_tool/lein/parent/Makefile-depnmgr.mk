#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
${symbol_pound} Dependency manager tasks (Ivy, Maven) Makefile script.

depnmgr ?= ivy  ${symbol_pound} ivy or maven
version ?= 0
SCALA_COMPAT ?= 2.9
IVY = java -Dscala.compat=${symbol_dollar}(SCALA_COMPAT) -Divy.cache.ttl.default=eternal -jar ${symbol_dollar}(HOME)/.ant/lib/ivy.jar
MVN = mvn -Dscala.compat=${symbol_dollar}(SCALA_COMPAT)

.PHONY: resolve retrieve publish
resolve: ${symbol_pound}${symbol_pound} resolve depns
	mkdir -p target
	-@if [ "maven" = "${symbol_dollar}(depnmgr)" ] ; then ${symbol_escape}
		${symbol_dollar}(MVN) dependency:resolve ; ${symbol_escape}
		${symbol_dollar}(MVN) dependency:build-classpath -DincludeScope=compile ${symbol_escape}
			-Dmdep.outputFile=target/classpath_compile.txt ; ${symbol_escape}
		${symbol_dollar}(MVN) dependency:build-classpath -DincludeScope=runtime ${symbol_escape}
			-Dmdep.outputFile=target/classpath_runtime.txt ; ${symbol_escape}
		${symbol_dollar}(MVN) dependency:build-classpath -DincludeScope=test ${symbol_escape}
			-Dmdep.outputFile=target/classpath_test.txt ; ${symbol_escape}
	else ${symbol_escape}
		${symbol_dollar}(IVY) -confs compile -cachepath target/classpath_compile.txt ; ${symbol_escape}
		${symbol_dollar}(IVY) -confs runtime -cachepath target/classpath_runtime.txt ; ${symbol_escape}
		${symbol_dollar}(IVY) -confs test -cachepath target/classpath_test.txt ; ${symbol_escape}
	fi

retrieve: resolve ${symbol_pound}${symbol_pound} retrieve depns
	-@if [ "maven" = "${symbol_dollar}(depnmgr)" ] ; then ${symbol_escape}
		${symbol_dollar}(MVN) dependency:copy-dependencies -DoutputDirectory=target/lib ; ${symbol_escape}
	else ${symbol_escape}
		${symbol_dollar}(IVY) -symlink -retrieve "target/lib/[artifact]-[revision](-[classifier]).[ext]" ; ${symbol_escape}
	fi

publish: ${symbol_pound}${symbol_pound} publish project artifact(s)
	-@if [ "maven" = "${symbol_dollar}(depnmgr)" ] ; then ${symbol_escape}
		${symbol_dollar}(MVN) install ; ${symbol_escape}
	else ${symbol_escape}
		${symbol_dollar}(IVY) -revision ${symbol_dollar}(version) -deliverto "target/ivy-[revision].xml" ${symbol_escape}
			-publishpattern "target/[artifact]-[revision](-[classifier]).[ext]" ${symbol_escape}
			-publish local -overwrite ; ${symbol_escape}
	fi
