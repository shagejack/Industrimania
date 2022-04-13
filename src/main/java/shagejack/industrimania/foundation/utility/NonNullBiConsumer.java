package shagejack.industrimania.foundation.utility;

import javax.annotation.Nonnull;
import java.util.function.BiConsumer;

/**
 * Equivalent to {@link BiConsumer}, except with nonnull contract.
 *
 * @see BiConsumer
 */
@FunctionalInterface
public interface NonNullBiConsumer<T,U> {
    void accept(@Nonnull T t, @Nonnull U u);
}
