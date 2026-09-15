package exceptions;

// creating custom runtime exception, that we can throw
// means we can replace '.orElseThrow(() -> new NoSuchElementException("Employee not found"))'
// to
// '.orElseThrow(() -> new ResourceNotFoundException("Employee not found"))
public class ResourceNotFoundException extends RuntimeException {

    // constructor (auto created by intellij)
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
