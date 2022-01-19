package shagejack.industrimania.registers.item;

import shagejack.industrimania.registers.block.grouped.AsBase;
import shagejack.industrimania.registers.item.grouped.AllOreChunks;

public interface AllGroupedItems extends AsBase {

    static void initAll(){
        AllOreChunks.initOres();

        //auto-detects and executes
//        var methods = Arrays.stream(AllGroupedItems.class.getInterfaces())
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
