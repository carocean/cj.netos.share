package cj.netos.share;


/**
 * 抽象地理感应器，其集合名为分类id
 */
public class GeoReceptor {
    String id;
    String title;
    String townCode;
    String category;
    String channel;
    String brand;
    String leading;
    String creator;
    LatLng location;
    double radius;
    //更新距离仅在mobiles分类下的感知器有用
    int uDistance;
    long ctime;
    String device;
    GeoCategoryMoveMode moveMode;
    BackgroundMode backgroundMode;
    ForegroundMode foregroundMode;
    String background;
    int delFlag;//0是可用；1为已删除
    int isChanged;//供追链更新感知器信息使用；0为无更新；1为更新了标题；2为更新了图标

    public int getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(int delFlag) {
        this.delFlag = delFlag;
    }

    public int getIsChanged() {
        return isChanged;
    }

    public void setIsChanged(int isChanged) {
        this.isChanged = isChanged;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTownCode() {
        return townCode;
    }

    public void setTownCode(String townCode) {
        this.townCode = townCode;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public GeoCategoryMoveMode getMoveMode() {
        return moveMode;
    }

    public void setMoveMode(GeoCategoryMoveMode moveMode) {
        this.moveMode = moveMode;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public BackgroundMode getBackgroundMode() {
        return backgroundMode;
    }

    public void setBackgroundMode(BackgroundMode backgroundMode) {
        this.backgroundMode = backgroundMode;
    }

    public ForegroundMode getForegroundMode() {
        return foregroundMode;
    }

    public void setForegroundMode(ForegroundMode foregroundMode) {
        this.foregroundMode = foregroundMode;
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public LatLng getLocation() {
        return location;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getLeading() {
        return leading;
    }

    public void setLeading(String leading) {
        this.leading = leading;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public int getuDistance() {
        return uDistance;
    }

    public void setuDistance(int uDistance) {
        this.uDistance = uDistance;
    }

    public long getCtime() {
        return ctime;
    }

    public void setCtime(long ctime) {
        this.ctime = ctime;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }
}
