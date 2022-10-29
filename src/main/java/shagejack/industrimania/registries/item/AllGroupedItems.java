package shagejack.industrimania.registries.item;

import shagejack.industrimania.registries.block.grouped.AsBase;
import shagejack.industrimania.registries.item.grouped.AllOreChunks;
import shagejack.industrimania.registries.item.grouped.AllRockPieces;

public interface AllGroupedItems extends AsBase {

    static void initAll(){
        AllOreChunks.initOreChunks();
        AllRockPieces.initRockPieces();

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
