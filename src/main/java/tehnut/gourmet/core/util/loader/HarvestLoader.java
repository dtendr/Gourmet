package tehnut.gourmet.core.util.loader;

import com.google.common.base.Stopwatch;
import com.google.common.collect.Lists;
import net.minecraftforge.fml.common.discovery.ASMDataTable;
import tehnut.gourmet.core.util.GourmetLog;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Set;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface HarvestLoader {

    class Gather {
        public static List<IHarvestLoader> gather(ASMDataTable dataTable) {
            Stopwatch stopwatch = Stopwatch.createStarted();
            List<IHarvestLoader> loaders = Lists.newArrayList();
            Set<ASMDataTable.ASMData> discoveredLoaders = dataTable.getAll(HarvestLoader.class.getName());

            for (ASMDataTable.ASMData data : discoveredLoaders) {
                try {
                    Class<?> asmClass = Class.forName(data.getClassName());
                    Field potentialLoader = asmClass.getDeclaredField(data.getObjectName());
                    if (!Modifier.isStatic(potentialLoader.getModifiers())) {
                        GourmetLog.FOOD_LOADER.error("Field at {}.{} was annotated with @HarvestLoader but is not static.", data.getClassName(), data.getObjectName());
                        continue;
                    }

                    if (potentialLoader.getType() != IHarvestLoader.class) {
                        GourmetLog.FOOD_LOADER.error("Field at {}.{} was annotated with @HarvestLoader but is not an IHarvestLoader.", data.getClassName(), data.getObjectName());
                        continue;
                    }

                    GourmetLog.FOOD_LOADER.info("Discovered a Harvest loader at {}.{}", data.getClassName(), data.getObjectName());
                    loaders.add((IHarvestLoader) potentialLoader.get(null));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            GourmetLog.FOOD_LOADER.info("Discovered {} harvest loader(s) in {}", loaders.size(), stopwatch.stop());
            return loaders;
        }
    }
}
