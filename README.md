# javafx-oop
GUI DataFrame Application as OOP exercise from AGH University of Science
To compile program you need:
 - change the pom.xml file dependencies under your OS (now is linux)
 - Install the DataFrame JAR into your local Maven repository as follows: \
    mvn install:install-file \
    -Dfile=path-to-file \
    -DgroupId=agh \
    -DartifactId=data-frame \
    -Dversion=2.0 \
    -Dpackaging=jar \
    -DgeneratePom=true 
 - go to maven projects -> plugins  -> exec  -> exec:java  -> create... -> field 'Comman Line' should be 'compile exec:java -f pom.xml' -> apply  -> run that configuration
