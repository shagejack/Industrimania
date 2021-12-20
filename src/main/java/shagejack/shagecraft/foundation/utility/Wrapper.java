package shagejack.shagecraft.foundation.utility;

import java.util.function.Supplier;

public class Wrapper<T> implements Supplier<T> {
    private Supplier<T> content;
    Wrapper(Supplier<T> content){
        this.content = content;
    }

    public Wrapper(){}

    public void set(Supplier<T> content){
        this.content = content;
    }

    @Override
    public T get() {
        return content.get();
    }
}
