JFLAGS = -g
JC = javac
JVM= java 
FILE=

.SUFFIXES: .java .class

.java.class:
		$(JC) $(JFLAGS) $*.java

CLASSES = \
		Eval.java \
		Input.java \
		Interpreter.java \
		Node.java \
		Output.java \
		Test.java

MAIN = Interpreter

default: classes

classes: $(CLASSES:.java=.class)

run: $(MAIN).class
	$(JVM) $(MAIN)

clean:
		$(RM) *.class

