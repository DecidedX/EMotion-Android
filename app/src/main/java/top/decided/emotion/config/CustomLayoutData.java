package top.decided.emotion.config;

import java.util.List;

public class CustomLayoutData{

    private String layoutName;
    private List<ViewValues> values;

    public CustomLayoutData(String layoutName, List<ViewValues> values) {
        this.layoutName = layoutName;
        this.values = values;
    }

    public String getLayoutName() {
        return layoutName;
    }

    public void setLayoutName(String layoutName) {
        this.layoutName = layoutName;
    }

    public List<ViewValues> getValues() {
        return values;
    }

    public void setValues(List<ViewValues> values) {
        this.values = values;
    }

    public void addViewValue(int buttonTag, float verticalBias, float horizontalBias, float deltaSize){
        values.add(new ViewValues(buttonTag, verticalBias, horizontalBias, deltaSize));
    }

    public ViewValues getViewValue(int buttonTag){
        for (ViewValues v : values){
            if (v.getButtonTag() == buttonTag){
                return v;
            }
        }
        return null;
    }

    public static class ViewValues{

        private int buttonTag;
        private float vBias;
        private float hBias;
        private float deltaSize;

        public ViewValues(int buttonTag, float vBias, float hBias, float deltaSize) {
            this.buttonTag = buttonTag;
            this.vBias = vBias;
            this.hBias = hBias;
            this.deltaSize = deltaSize;
        }

        public int getButtonTag() {
            return buttonTag;
        }

        public void setButtonTag(int buttonTag) {
            this.buttonTag = buttonTag;
        }

        public float getVBias() {
            return vBias;
        }

        public void setVBias(float vBias) {
            this.vBias = vBias;
        }

        public float getHBias() {
            return hBias;
        }

        public void setHBias(float hBias) {
            this.hBias = hBias;
        }

        public float getDeltaSize() {
            return deltaSize;
        }

        public void setDeltaSize(float deltaSize) {
            this.deltaSize = deltaSize;
        }

    }

}
