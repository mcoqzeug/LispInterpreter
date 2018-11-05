# Lisp Interpreter
To compile, run:
```shel
make
```

To run the interpreter, run:
```shell
java Interpreter
```

Then, type in an s-expressions and finish it with a line containing a single `$`.
The last s-expression should be followed by a line containing `$$`.

Don't include `COND`'s with all bi's evaluating to `NIL`, else the program will throw an error.

## Design
The Interpreter class reads strings from the input stream and extracts s-expressions by searching for lines that contain only `$` or `$$`. It then pass the string to the Input class which handles building the s-expression tree and using the Output to print the dot notation version of the s-expression. The Eval class contains some default atoms such `T`, `NIL`, etc.

Before calling the input routine, it checks whether the first character is `(`. If it's not, there is an error. After
the initial call of the input routine is returned, it checks whether there are characters that haven't been processed. If there is, it would throw an error [e.g. `(1.2) 3` is illegal].

The program only accept identifiers and integers with no more than 10 characters. An identifier should start with a upper case letter and followed by 0 or more upper case letters or digits.

How to define new functions:
`(DEFUN (FUNCTION_NAME pList) Body)` e.g. `(DEFUN (PLUS2 A B) (PLUS A B))`

How to call functions:
`(FUNCTION_NAME ARGUMENT_LIST)` e.g. `(PLUS 1 2)`

## TODO
`LAMBDA`, `LABEL`