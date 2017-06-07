# 203676945 305720054
# gilhaim dunetzn
compile: bin
	find src -name "*.java" > sources.txt
	javac -d bin -cp biuoop-1.4.jar @sources.txt
run: jar
	java -jar space-invaders.jar
jar: 
	jar cfm space-invaders.jar Manifest.mf -C bin . -C resources .
bin:
	mkdir bin
