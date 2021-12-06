package shagejack.shagecraft.api;

import java.util.function.Supplier;

public class LazyOptional<T> {
    private T value;
    private Supplier<T> factory;
    public LazyOptional(Supplier<T> factory) {
        this.factory = factory;
    }
    public T get(){
        if (value == null) {
            value = factory.get();
        }
        return this.value;
    }
}
