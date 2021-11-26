package shagejack.minecraftology.api.storage.io;

import java.util.List;

public interface IReplyContext<T>{
    boolean success();
    List<T> get();
}
