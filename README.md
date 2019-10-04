# About
This is a command line calculator written in Java. For more information, launch the program and type "help" at the prompt.

# Compiling
##### _Notice: to compile this program you need to have the Java 8 SDK on your computer. Visit [here](https://www.java.com/en/download/) to download the latest version._
To compile the calculator, run the following commands at the command-line prompt:
```bash
mkdir obj
javac -d obj/ src/*.java
cd obj
jar -cfe ../InvisiCalc.jar UserInterface *
```

# Launching
##### _Notice: to run this program you need to have the Java 8 JRE on your computer. Visit [here](https://www.java.com/en/download/) to download the latest version._
To launch the calculator, run the following command at the command-line prompt:
```bash
java -jar InvisiCalc.jar
```
