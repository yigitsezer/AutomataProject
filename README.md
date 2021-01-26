## Description
The grammar is read from a single text file 'grammar.txt'. To be able to read the grammar from a text file, the grammar should be in a particular form

* every non-terminal should be written inside of ‘< >’
* if productions are written in the same line, OR symbol ‘|’ should be written between these production rules
- ‘->’ represents ‘A ➜ AB’ representation.

Example Grammar:

\<S> -> \<A>\<B>\
\<A> -> \<C>\<D>|\<C>\<F>\
\<B> -> c\
\<B> -> \<E>\<B>\
\<C> -> a\
\<D> -> b\
\<E> -> c\
\<F> -> \<A>\<D>

## Executable

Execute AutomataProject.jar with an accaptable grammar.txt in the same directory.
