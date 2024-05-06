call mvn test exec:java -Dexec.mainClass="com.impactqa.utilities.PostExecutionClass" -Dexec.classpathScope=test -e -Dmaven.test.failure.ignore=true
echo press any key to exit
pause>nul