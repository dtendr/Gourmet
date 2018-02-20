package tehnut.gourmet.core.data;

import com.google.common.collect.Sets;
import com.google.gson.*;
import com.google.gson.annotations.JsonAdapter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import tehnut.gourmet.core.util.GourmetLog;

import javax.annotation.Nullable;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Set;

@JsonAdapter(Harvest.Serializer.class)
public final class Harvest {

    private final int hungerProvided;
    private final float saturationModifier;
    private final String simpleName;
    private final GrowthType growthType;
    private final ConsumeStyle consumptionStyle;
    private final boolean alwaysEdible;
    private final int timeToEat;
    private final String[] oreDictionaryNames;
    private final EatenEffect[] effects;
    @Nullable // Only exists if growthType is CROP
    private final CropGrowth cropGrowth;

    private Harvest(int hungerProvided, float saturationModifier, String simpleName, GrowthType growthType, ConsumeStyle consumptionStyle, boolean alwaysEdible, int timeToEat, String[] oreDictionaryNames, EatenEffect[] effects, CropGrowth cropGrowth) {
        this.hungerProvided = hungerProvided;
        this.saturationModifier = saturationModifier;
        this.simpleName = simpleName;
        this.growthType = growthType;
        this.consumptionStyle = consumptionStyle;
        this.alwaysEdible = alwaysEdible;
        this.timeToEat = timeToEat;
        this.oreDictionaryNames = oreDictionaryNames;
        this.effects = effects;
        this.cropGrowth = cropGrowth;
    }

    public int getHungerProvided() {
        return hungerProvided;
    }

    public float getSaturationModifier() {
        return saturationModifier;
    }

    public String getSimpleName() {
        return simpleName;
    }

    public GrowthType getGrowthType() {
        return growthType;
    }

    public ConsumeStyle getConsumptionStyle() {
        return consumptionStyle;
    }

    public boolean isAlwaysEdible() {
        return alwaysEdible;
    }

    public int getTimeToEat() {
        return timeToEat;
    }

    public String[] getOreDictionaryNames() {
        return oreDictionaryNames;
    }

    public EatenEffect[] getEffects() {
        return effects;
    }

    @Nullable
    public CropGrowth getCropGrowth() {
        return cropGrowth;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Harvest)) return false;

        Harvest harvest = (Harvest) o;

        return simpleName.equals(harvest.simpleName);
    }

    @Override
    public int hashCode() {
        return simpleName.hashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.NO_CLASS_NAME_STYLE)
                .append("hungerProvided", hungerProvided)
                .append("saturationModifier", saturationModifier)
                .append("simpleName", simpleName)
                .append("growthType", growthType)
                .append("consumptionStyle", consumptionStyle)
                .append("alwaysEdible", alwaysEdible)
                .append("timeToEat", timeToEat)
                .append("oreDictionaryNames", oreDictionaryNames)
                .append("effects", effects)
                .append("cropGrowth", cropGrowth)
                .toString();
    }

    public static final class Builder {

        private final int hungerProvided;
        private final float saturationModifier;
        private final String simpleName;
        private GrowthType growthType = GrowthType.NONE;
        private ConsumeStyle consumptionStyle = ConsumeStyle.EAT;
        private boolean alwaysEdible = false;
        private int timeToEat = 32;
        private final Set<String> oreDictionaryNames = Sets.newHashSet();
        private final Set<EatenEffect> effects = Sets.newHashSet();
        private CropGrowth cropGrowth;

        public Builder(String simpleName, int hungerProvided, float saturationModifier) {
            this.simpleName = simpleName;
            this.hungerProvided = hungerProvided;
            this.saturationModifier = saturationModifier;
        }

        public Builder setGrowthType(GrowthType growthType) {
            this.growthType = growthType;
            return this;
        }

        public Builder setConsumptionStyle(ConsumeStyle consumptionStyle) {
            this.consumptionStyle = consumptionStyle;
            return this;
        }

        public Builder setAlwaysEdible() {
            this.alwaysEdible = true;
            return this;
        }

        public Builder setTimeToEat(int timeToEat) {
            this.timeToEat = timeToEat;
            return this;
        }

        public Builder addOreDictionaryNames(String... oreDictionaryNames) {
            Collections.addAll(this.oreDictionaryNames, oreDictionaryNames);
            return this;
        }

        public Builder addEffect(EatenEffect... effects) {
            Collections.addAll(this.effects, effects);
            return this;
        }

        public Builder setCropGrowth(CropGrowth cropGrowth) {
            this.cropGrowth = cropGrowth;
            return this;
        }

        public Harvest build() {

            switch (growthType) {
                case CROP: if (cropGrowth == null) throw new RuntimeException("Harvests with a type of CROP must provide a CropGrowth.");
            }

            Harvest harvest = new Harvest(hungerProvided, saturationModifier, simpleName, growthType, consumptionStyle, alwaysEdible, timeToEat, oreDictionaryNames.toArray(new String[0]), effects.toArray(new EatenEffect[0]), cropGrowth);
            GourmetLog.FOOD_LOADER.info("Constructed as {}", ToStringBuilder.reflectionToString(harvest, ToStringStyle.NO_CLASS_NAME_STYLE));
            return harvest;
        }
    }

    // TODO
    public static class Serializer implements JsonSerializer<Harvest>, JsonDeserializer<Harvest> {
        @Override
        public Harvest deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            return null;
        }

        @Override
        public JsonElement serialize(Harvest src, Type typeOfSrc, JsonSerializationContext context) {
            return null;
        }
    }
}