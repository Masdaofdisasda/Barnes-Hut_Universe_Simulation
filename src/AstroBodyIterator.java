public interface AstroBodyIterator extends java.util.Iterator<AstroBody>{

    // Returns 'true' if the iteration has more elements.
    boolean hasNext();

    // Returns the next element (i.e. body) in the iteration.
    AstroBody next();

}
