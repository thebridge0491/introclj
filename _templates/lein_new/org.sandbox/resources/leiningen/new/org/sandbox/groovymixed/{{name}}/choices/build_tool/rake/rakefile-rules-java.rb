# Java/Groovy rules rakefile script.

class SharedVars
  attr_accessor :javac_opts, :groovyc_opts, :java_args,  
    :classpath_compile, :classpath_runtime, :classpath_test
end

if 'Linux' == `sh -c 'uname -s 2>/dev/null || echo not'`.chomp
  JAVA_LIB = '/usr/share/java'
  GROOVY_HOME = '/usr/share/groovy'
else
  JAVA_LIB = '/usr/local/share/java/classes'
  GROOVY_HOME = '/usr/local/share/java/groovy'
end

if '1' == DEBUG
  ENV['JAVAC_OPTS'] = "#{ENV['JAVAC_OPTS']} -g"
  ENV['GROOVYC_OPTS'] = "#{ENV['GROOVYC_OPTS']} -F-g"
else
  ENV['GROOVYC_OPTS'] = "#{ENV['GROOVYC_OPTS']}"
end

JAVA_LIB_PATH = ENV['JAVA_LIB_PATH']
GROOVY_LIB = "#{GROOVY_HOME}/lib"
#SCALA_COMPAT = ENV['SCALA_COMPAT'] ? ENV['SCALA_COMPAT'] : '2.9'
VARS.javac_opts = "#{ENV['JAVAC_OPTS']} -Xlint:all -deprecation" # -target 1.7 -source 1.7
VARS.groovyc_opts = "#{ENV['GROOVYC_OPTS']} -j"
VARS.java_args = "-esa -ea -Xmx1024m -Xms16m -Xss16m"

CLASSES_DIR, CLASSES_TEST_DIR = "target/classes/main", "target/classes/test"

VARS.classpath_compile = File.exists?("target/classpath_compile.txt") ?
  File.foreach("target/classpath_compile.txt").first.chomp : ''
VARS.classpath_runtime = File.exists?("target/classpath_runtime.txt") ? 
  File.foreach("target/classpath_runtime.txt").first.chomp : ''
VARS.classpath_test = File.exists?("target/classpath_test.txt") ? 
  File.foreach("target/classpath_test.txt").first.chomp : ''

COMPILE_java = "javac #{VARS.javac_opts}"
COMPILE_groovy = "groovyc #{VARS.groovyc_opts}"

rule '-tests.jar' do |t|				# rule '*.{java,groovy}' => '-tests.jar'
  rm_rf(t.name)
  sh "echo -cp #{VARS.classpath_test}:#{CLASSES_DIR}:#{CLASSES_TEST_DIR} -d #{CLASSES_TEST_DIR} > target/.argfile", {verbose: false}
  sh "#{COMPILE_groovy} `cat target/.argfile` #{t.prerequisites.join(' ')}"
  #sh "#{COMPILE_java} #{t.prerequisites.select{|e| e =~ /\.java/}.join(' ')} @target/.argfile"
  sh "echo 'Class-Path: ' > target/test_manifest.mf"
  ((VARS.classpath_test.split(':').map { |i| "lib/" + File.basename(i) }) + ["#{VARS.proj}-#{VARS.version}.jar"]).each { |strX|
    sh "echo '  '#{strX} >> target/test_manifest.mf", {verbose: false}
  }
  sh "jar -cfme #{t.name} target/test_manifest.mf #{VARS.groupid}.#{VARS.parent}.#{VARS.pkg}.Ts_Main -C #{CLASSES_TEST_DIR} ."
end

rule '.jar' do |t|				    # rule '*.{java,groovy}' => '.jar'
  rm_rf(t.name)
  sh "echo -cp #{VARS.classpath_compile}:#{CLASSES_DIR} -d #{CLASSES_DIR} > target/.argfile", {verbose: false}
  sh "#{COMPILE_groovy} `cat target/.argfile` #{t.prerequisites.join(' ')}"
  #sh "#{COMPILE_java} #{t.prerequisites.select{|e| e =~ /\.java/}.join(' ')} @target/.argfile"
  sh "echo 'Class-Path: ' > target/manifest.mf"
  (VARS.classpath_runtime.split(':').map { |i| "lib/" + File.basename(i) }).each { |strX|
    sh "echo '  '#{strX} >> target/manifest.mf", {verbose: false}
  }
  sh "jar -cfm #{t.name} target/manifest.mf -C #{CLASSES_DIR} ."
  sh "jar -ufe #{t.name} #{VARS.main_class} -C src/main/resources ." \
    if '' != VARS.main_class
end
