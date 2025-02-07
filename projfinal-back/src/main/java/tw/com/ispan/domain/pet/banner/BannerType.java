package tw.com.ispan.domain.pet.banner;

public enum BannerType {
    LOST("遺失", 1),
    ADOPTION("認養", 2),
    RESCUE("救援", 3);

    private final String displayName;
    private final Integer typeCode;

    // Constructor
    BannerType(String displayName, Integer typeCode) {
        this.displayName = displayName;
        this.typeCode = typeCode;
    }

    // Getter for typeCode
    public Integer getCode() {
        return typeCode;
    }

    // Getter for displayName
    public String getName() {
        return displayName;
    }

    // Static method to find BannerType by typeCode
    public static BannerType fromCode(Integer typeCode) {
        for (BannerType type : BannerType.values()) {
            if (type.typeCode.equals(typeCode)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid BannerType typeCode: " + typeCode);
    }

    // Static method to find BannerType by displayName
    public static BannerType fromName(String displayName) {
        for (BannerType type : BannerType.values()) {
            if (type.displayName.equals(displayName)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid BannerType displayName: " + displayName);
    }
}