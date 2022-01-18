package shagejack.industrimania.registers.block;

import shagejack.industrimania.registers.block.grouped.AllOres;
import shagejack.industrimania.registers.block.grouped.AllRocks;
import shagejack.industrimania.registers.block.grouped.AsBase;
import shagejack.industrimania.registers.item.AllGroupedItems;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * way to add new grouped block
 * add an interface under {@link shagejack.industrimania.registers.block.grouped} and extends AsBase
 * make this {@link shagejack.industrimania.registers.item.AllGroupedItems} extends it
 * add init function call at {@link AllGroupedItems#initAll}
 */
public interface AllGroupedBlocks extends AsBase, AllOres, AllRocks {

    BlockBuilder asBase();

    static void initAll() {
        AllOres.initOres();
        AllRocks.initRocks();

//        auto-detects and executes
//        var methods = Arrays.stream(AllGroupedBlocks.class.getInterfaces())
//                .filter((clzz) -> clzz.getName().contains("All"))
//                .map((clzz) -> Arrays.stream(clzz.getDeclaredMethods())
//                        .filter((method -> method.getName().contains("init")))
//                        .collect(Collectors.toList())).toList();
//        methods.forEach(outer -> outer.forEach(method -> {
//            try {
//                method.invoke(null);
//            } catch (IllegalAccessException | InvocationTargetException ignored) {
//
//            }
//        }));

    }

}


