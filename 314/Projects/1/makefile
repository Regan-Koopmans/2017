make:
	javac -cp . bao/*.java
	javac Main.java

run:
	java Main

style:
	astyle Main.java
	astyle bao/*.java
	rm *.orig
	rm bao/*.orig

clean:
	rm *.class
	rm bao/*.class
